package antclustering;

import java.util.Random;

/*******
 * 0：配置なし
 * 1~kind：オブジェクト
 * kind+1：蟻
 * kind+2~kind*2+1：オブジェクトと蟻
 ******/

public class Field {
    //現在の状態(存在している物体の種類)
    public static Grand grand ;
    //蟻
    public static ant[] ant;
    //閾値
    static double Kcrowd = 2;
    static double Kpick = 2;
    static double Kdrop = 2;
    static int pickCount = 3;
    static int pickCounts = 4;
    static int Range = 2;
    

    //  乱数によって動かす方向を決定するための配列
    static final int[] movex = {-1, 0, 1,-1, 1,-1, 0, 1};
    static final int[] movey = {-1,-1,-1, 0, 0, 1, 1, 1};
    static final int[] moveX = {-2,-1, 0, 1, 2,-2,-1, 0, 1, 2,-2,-1, 1, 2,-2,-1, 0, 1, 2,-2,-1, 0, 1, 2};
    static final int[] moveY = {-2,-2,-2,-2,-2,-1,-1,-1,-1,-1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2};
    
    //*****************************************************************************//
    static int MAX_size;
    static int MAX_object;
    static int MAX_kind;
    static int MAX_state;
    static int MAX_ant;
    static int ANT;
    //繰り返し回数
    static int Iteration;
    //繰り返し回数の半分
    static int HalfIteration;
    //繰り返し回数の三分の一
    static int ThirdIteration;
    static int limitMoveTime;
    static int limitKeepTime;
    static int limitCount;
    //*****************************************************************************//
    
//**********************************************************************************************//
    //***数値の初期化***//
    public void set(int size,int object,int kind,int ant,int iteration,int limitmovetime){
        MAX_size = size;
        MAX_object = object;
        MAX_kind = kind;
        MAX_ant = ant;
        MAX_state = kind*2+1;
        ANT = kind +1;
        grand.setting(ANT);
        Iteration = iteration;
        limitMoveTime = limitmovetime;
        limitKeepTime = (int) (limitmovetime*1.5);
        limitCount = iteration / 100;
        HalfIteration = iteration/2;
        ThirdIteration = iteration/3;
        
        
    }
    //***初期配置を行う***//
    public void Fieldset(){
        Random rnd = new Random();
        grand.state = new int[MAX_size][MAX_size];
        ant = new ant[MAX_ant];
        int object_x,object_y,object_kind,ant_size=0,object_size=0;
        //蟻の作成
        for(int i = 0; i < MAX_ant; i++){
            ant[i] = new ant(); 
        }
        //配置
        while(object_size<MAX_object||ant_size<MAX_ant){
            object_x = rnd.nextInt(MAX_size);
            object_y = rnd.nextInt(MAX_size);
            object_kind = rnd.nextInt(MAX_kind+2);

            if(object_kind!=0){
                //オブジェクトの配置
                if(grand.state[object_y][object_x]==0 && object_kind<MAX_kind+1 && object_size<MAX_object){
                    grand.setState(object_x,object_y,object_kind);
                    object_size++;
                //蟻の配置
                }else if(grand.state[object_y][object_x]<ANT && object_kind == ANT && ant_size<MAX_ant){
                    ant[ant_size].set(object_x,object_y,ant_size,0 );
                    grand.setAnt(object_x,object_y);
                    ant_size++;
                }
            }
        }
    }
//**********************************************************************************************//
    //***クラスタリングの実行***//
    public void Clustering(){
    /*    double  P=0,pa=0;
        Random rnd2 = new Random();
    */
        int x,y;
        //「Interation」の数だけ繰り返し
        for(int i=0;i<Iteration;i++){
            //回数表示
            Print(i);
            
            //クローンの作成
            grand.setCloneState();
            ant[] antClone = ant.clone();
            //すべての蟻で実行
            for(int an=0;an<MAX_ant;an++){
//                pa = rnd2.nextDouble();
                x = ant[an].Location.x;
                y = ant[an].Location.y;
                double F = Neight(an);
                //持ち上げられる状態か
                if(pick(an)){
                    Around8(an);
                    if(pickObject(an,i)){
                        //蟻がオブジェクトを持ち上げる
                        antClone[an].State = grand.cloneState[y][x]-ANT;
                        grand.cloneState[y][x] = ANT;
                    }else
                        antClone[an].time++;
                }
                //降ろせる状態か
                else if(drop(an)){                    
                    Around8(an);
                    if(dropObject(an,i)){
                        //蟻がオブジェクトを下す
                        grand.cloneState[y][x] = antClone[an].State+ANT;
                        antClone[an].State = 0;
                        antClone[an].time = 0;
                    }else
                        antClone[an].time ++;
                    if(limitKeepTime<antClone[an].time&&antClone[an].around>0){
                       //蟻がオブジェクトを下す
                        grand.cloneState[y][x] = antClone[an].State+ANT;
                        antClone[an].State = 0;
                        antClone[an].time = 0;
                    }
                }
            }
            //state,antの更新
            ant = antClone;
            grand.resetState();

            //ランダム移動
            wander();
        }
        System.out.println("\r ");
    }
//**********************************************************************************************//
    //***周りがpickCount以下なら持ち上げる***//
    public static boolean pickObject(int an,int iteration){
        if(iteration<ThirdIteration)
            return ant[an].around<=pickCount;
        else if(iteration<HalfIteration)
            return ant[an].around<=pickCount+1;
        else
            return ant[an].around<=pickCount+1;
    }
    //***周りがpickCount以上なら降ろす***//
    public static boolean dropObject(int an,int iteration){
        if(iteration<ThirdIteration)
            return ant[an].around>=pickCount;
        else if(iteration<HalfIteration)
            return ant[an].around>=pickCount+1;
        else
            return ant[an].around>=pickCount+1;
    }

    //***オブジェクトの持ち上げれるか***//
    public static boolean pick(int an){
        // 蟻がオブジェクトを所持していない　かつ　場に蟻とオブジェクトが存在
        return ant[an].State==0&&grand.state[ant[an].Location.y][ant[an].Location.x]>ANT;
    }
    //***オブジェクトを降ろせるか***//
    public static boolean drop(int an){
        //　蟻がオブジェクトを所持している　かつ　場にオブジェクトが存在しない
        return ant[an].State!=0&&grand.state[ant[an].Location.y][ant[an].Location.x]==ANT;
    }
    
    //***オブジェクトを持ち上げる確率***//
    public static double P_pick(int an,double X,double F){
        return (1-X)*Math.pow(Kpick/(Kpick+F), 2);
    }
    //***オブジェクトを下す確率***//
    public static double P_drop(int an,double X,double F){
        return X*Math.pow(Kdrop/(Kdrop+F),2);
    }
//**********************************************************************************************//    
    //***周りとの類似度***//
    public static double Neight(int an){
        double result = 0,count=0,objectCount=0,anotherCount=0;
        int X,Y,Xend,Yend,
            x = ant[an].Location.x,
            y = ant[an].Location.y;
        int object = ant[an].State;
        //蟻が保持しているオブジェクトが0(保持なし)ならその場に落ちているオブジェクトで判断する。
        if(object ==0)
            object = grand.state[y][x];
        //蟻が認識する範囲(XからXendまで、YからYendまで)
        X = Math.max(0,x-Range);
        Xend = Math.min(x+Range,MAX_size);
        Y = Math.max(0,y-Range);
        Yend = Math.min(y+Range,MAX_size);
        //同一オブジェクト同士の類似
        for(int i =X ;i<Xend;i++)
            for(int j = Y;j<Yend;j++){
                //オブジェクトの一致
                if(grand.state[j][i]>0
                        //&&grand.state[j][i]<ANT
                        ){
                    objectCount++;
                    if(!(i==x&&j==y)&&grand.state[j][i] == object||grand.state[j][i]-ANT == object)
                        count++;
                    
                }                    
            }
        if(count==0)return 0;        
        return count / objectCount;
    }
    //***周囲8マスの物体の数***//
    public static void Around8(int an){
        int x = ant[an].Location.x,
            y = ant[an].Location.y,
            X,Y,Xend,Yend,count=0,
            object = ant[an].State,objectCount=0;
        if(object == 0)return;
        
        //蟻が認識する範囲
        X = Math.max(0,x-1);
        Xend = Math.min(x+1,MAX_size-1);
        Y = Math.max(0,y-1);
        Yend = Math.min(y+1,MAX_size-1);
        //同一オブジェクト同士の類似
        for(int i =X ;i<=Xend;i++)
            for(int j = Y;j<=Yend;j++){
                //オブジェクトの一致
                if(grand.checkObject(i,j,object))
                    count++;
                if(grand.state[j][i]>0&&grand.state[j][i]<ANT)
                    objectCount++;
            }
        ant[an].around = count;
    }
//**********************************************************************************************//
    //***蟻の移動　ランダム***//
    public void wander(){   
        int oldx,oldy;
        //蟻すべてがランダムに移動
        for(int an=0;an<MAX_ant;an++){
            //移動が完了するまで
            oldx = ant[an].Location.x;
            oldy = ant[an].Location.y;
            if(ant[an].time < limitMoveTime){
                Move(an,oldx,oldy);
            }else{
                Move2(an,oldx,oldy);
            }
        }
    }
    //***ランダム移動　1マス***//
    public void Move(int an,int oldx,int oldy){
        int k,x,y,count=0;
        Random rnd = new Random();
        while(count<30){
            count++;
            k = rnd.nextInt(movex.length);
            x = oldx + movex[k];
            y = oldy + movey[k];
            //移動先が存在する　かつ　移動先に蟻がいない
            if((x>0&&x<MAX_size)&&(y>0&&y<MAX_size)&&grand.state[y][x]<ANT){
                //以前の位置から蟻を削除                   
                grand.removeAnt(oldx,oldy);
                //蟻の移動
                ant[an].Location.x = x;
                ant[an].Location.y = y;
                ant[an].oldX = x;
                ant[an].oldY = y;
                //移動後の位置に蟻を追加
                grand.setAnt(x, y);
                //ループ強制抜け
                break;
            }
        }        
    }
    //***ランダム移動　2マス***//
    public void Move2(int an,int oldx,int oldy){
        int k,x,y,count=0;
        Random rnd = new Random();
        while(count<40){
                count++;
                k = rnd.nextInt(moveX.length);
                x = oldx + moveX[k];
                y = oldy + moveY[k];
                //移動先が存在する　かつ　移動先に蟻がいない
                if((x>0&&x<MAX_size)&&(y>0&&y<MAX_size)&&grand.state[y][x]<ANT){
                    //以前の位置から蟻を削除                   
                    grand.removeAnt(oldx,oldy);
                    //蟻の移動
                    ant[an].Location.x = x;
                    ant[an].Location.y = y;
                    ant[an].oldX = x;
                    ant[an].oldY = y;
                    //移動後の位置に蟻を追加
                    grand.setAnt(x, y);
                    //ループ強制抜け
                    break;
                }
            }
    }
//**********************************************************************************************//
    //***配置状態の表示(蟻有)***//
    public void Check(){    
        int object[]=new int[MAX_state+1];
        int object2[]=new int[ANT];
        for (int[] loop : grand.state) {
            for (int j = 0; j<grand.state.length; j++) {
                System.out.print(loop[j] + " ");
                object[loop[j]]++;                    
            }
            System.out.println("");
        }
        //オブジェクトを持ち上げている蟻の表示
        for(int an=0;an<MAX_ant;an++){
            if(ant[an].State!=0){
                System.out.println("antNo."+(an+1)+" ("+ant[an].Location.x+","+ant[an].Location.y+") = "+ant[an].State);
                object2[ant[an].State]++;
            }
        }
        for(int b=1;b<object.length;b++)
            if(b<=MAX_kind)
                System.out.println("object "+b+" = "+object[b]);
            else if(b==MAX_kind+1)
                System.out.println("ant = "+object[b]);
            else
                System.out.println("object "+(b-MAX_kind-1)+" &ant = "+object[b]);
        for(int b=0;b<=MAX_kind;b++)
            if(object2[b]!=0)
                System.out.println(b+" objectを持つあり = "+object2[b]);
    }
    //***配置状態の表示(蟻無)***//
    public void NotAntCheck(){
        for (int[] loop : grand.state) {
            for (int j = 0; j<grand.state.length; j++) {
                if (loop[j] < ANT) {
                    System.out.print(loop[j] + " ");
                } else {
                    System.out.print(loop[j] - ANT + " ");
                }
            }
            System.out.println("");
        }
    }
//**********************************************************************************************//    
    //***ユークリッド距離の計算***//
    public static double EuclideanLenght(int x1,int y1,int x2,int y2){
        return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }
    //***座標表示関数**//
    public static String coordinate(int an){
        int x = ant[an].Location.x;
        int y = ant[an].Location.y;
        
        return ("("+x+","+y+")") ;
    }
    
    public void Print(int i){
        if(i==ThirdIteration){
                System.out.print("\r");
                NotAntCheck();
                System.out.println("Change");
                System.out.print("--------------------"+i+"--------------------");
            }
            else if(Iteration>=1000000&&i==0){
                System.out.println("\n");
                System.out.print("--------------------"+i+"--------------------");
            }else if(i%limitCount==0){
                System.out.print("\r");
                System.out.print("--------------------"+i+"--------------------");
            }else if(i==(limitCount-1)){
                System.out.println("\r");
            }
    }
            
}
    