package antclustering;

import java.util.Random;
import antclustering.Grand;
import antclustering.ant;

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
        grand = new Grand();
        MAX_size = size;
        MAX_object = object;
        MAX_kind = kind;
        MAX_ant = ant;
        MAX_state = kind*2+1;
        ANT = kind +1;
        grand.setting(ANT,MAX_size);
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
                x = ant[an].Location.x;
                y = ant[an].Location.y;
                double F = grand.Neight(ant[an],Range);
                //持ち上げられる状態か
                if(antOperation.pick(ant[an],antClone[an],grand,i,x,y));
                //降ろせる状態か
                else if(antOperation.drop(ant[an],antClone[an],grand,i,x,y));
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
    //***蟻の移動　ランダム***//
    public void wander(){   
        //蟻すべてがランダムに移動
        for(int an=0;an<(int)(MAX_ant);an++){
            //移動が完了するまで
            if(ant[an].time < limitMoveTime){
                Move(ant[an]);
            }else{
                Move2(ant[an]);
            }
        }
        int result=antOperation.Punctuation(ant);
/*        for(int an=(int) (MAX_ant*0.9);an<MAX_ant;an++){
            if(ant[an].time < limitMoveTime){
                Moves(ant[an]);
            }else{
                Moves2(ant[an]);
            }
        }*/
    }
    //***ランダム移動　1マス***//
    public void Move(ant an){
        int k,x,y,count=0;
        Random rnd = new Random();
        while(count<30){
            count++;
            k = rnd.nextInt(movex.length);
            x = an.old.x + movex[k];
            y = an.old.y + movey[k];
            //移動先が存在する　かつ　移動先に蟻がいない
            if((x>0&&x<MAX_size)&&(y>0&&y<MAX_size)&&grand.state[y][x]<ANT){
                //以前の位置から蟻を削除                   
                //蟻の移動
                an.Location.x = x;
                an.Location.y = y;
                //移動後の位置に蟻を追加
                grand.resetAnt(an);
                an.old.x = x;
                an.old.y = y;
                //ループ強制抜け
                break;
            }
        }        
    }
    //***ランダム移動　2マス***//
    public void Move2(ant an){
        int k,x,y,count=0;
        Random rnd = new Random();
        while(count<40){
                count++;
                k = rnd.nextInt(moveX.length);
                x = an.old.x + moveX[k];
                y = an.old.y + moveY[k];
                //移動先が存在する　かつ　移動先に蟻がいない
                if((x>0&&x<MAX_size)&&(y>0&&y<MAX_size)&&grand.state[y][x]<ANT){
                    //以前の位置から蟻を削除                   
                    //蟻の移動
                    an.Location.x = x;
                    an.Location.y = y;
                    //移動後の位置に蟻を追加
                    grand.resetAnt(an);
                    an.old.x = x;
                    an.old.y = y;
                    //ループ強制抜け
                    break;
                }
            }
    }
    public void Moves(ant an){
        int k,x,y,count=0;
        Random rnd = new Random();
        while(count<30){
            count++;
            k = rnd.nextInt(movex.length);
            x = an.old.x + movex[k];
            y = an.old.y + movey[k];
            //移動先が存在する　かつ　移動先に蟻がいない
            if((x>0&&x<MAX_size)&&(y>0&&y<MAX_size)&&grand.state[y][x]<ANT){
                //以前の位置から蟻を削除                   
                //蟻の移動
                an.Location.x = x;
                an.Location.y = y;
                //移動後の位置に蟻を追加
                grand.resetAnt(an);
                an.old.x = x;
                an.old.y = y;
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
    public void CheckAnt(){
        for (int[] loop : grand.state) {
            for (int j = 0; j<grand.state.length; j++) {
                if (loop[j] < ANT) {
                    System.out.print(0 + " ");
                } else {
                    System.out.print(ANT + " ");
                }
            }
            System.out.println("");
        }
    }
    //***表示***//
    public void Print(int i){
        if(i==ThirdIteration){
                System.out.print("\r");
                NotAntCheck();
                System.out.println("Change");
                System.out.print("--------------------"+i+"--------------------");
            }
            else if(Iteration>=1000000&&i==0){
                System.out.println("\r");
                System.out.print("--------------------"+i+"--------------------");
            }else if(i%limitCount==0){
                System.out.print("\r");
                System.out.print("--------------------"+i+"--------------------");
            }else if(i==(limitCount-1)){
                System.out.println("\r");
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
    //**********************************************************************************************//
    //***オブジェクトを持ち上げる確率***//
    public static double P_pick(int an,double X,double F){
        return (1-X)*Math.pow(Kpick/(Kpick+F), 2);
    }
    //***オブジェクトを下す確率***//
    public static double P_drop(int an,double X,double F){
        return X*Math.pow(Kdrop/(Kdrop+F),2);
    }
            
}
    