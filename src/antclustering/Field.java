package antclustering;

import java.util.Random;
import java.awt.Point;

public class Field {
    //現在の状態(存在している物体の種類)
    public static Grand grand ;
    //蟻
    public static int ant_E;
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
    
    
    static Point[] S = new Point[4];
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
        Iteration = iteration;
        limitMoveTime = limitmovetime;
        limitKeepTime = (int) (limitmovetime*1.5);
        limitCount = iteration / 100;
        HalfIteration = iteration/2;
        ThirdIteration = iteration/3;
       // setS();
         
    }
    public void setS(){
        S[0] = new Point();
        S[1] = new Point();
        S[2] = new Point();
        S[3] = new Point();
        S[0].x=MAX_size/4;
        S[0].y=MAX_size/4;
        S[1].x=3*MAX_size/4;
        S[1].y=MAX_size/4;
        S[2].x=MAX_size/4;
        S[2].y=3*MAX_size/4;
        S[3].x=3*MAX_size/4;
        S[3].y=3*MAX_size/4;
    }
    //***初期配置を行う***//
    public void Fieldset(){
        Random rnd = new Random();
        grand.state = new int[MAX_size][MAX_size];
        grand.ant = new int[MAX_size][MAX_size];
        grand.pheromone = new double[MAX_kind+1][MAX_size][MAX_size];
        grand.C = new C[MAX_kind];
        ant_E = (int) (MAX_ant*0.2);
        ant = new ant[MAX_ant];
        int object_x,object_y,object_kind,ant_size=0,object_size=0;
        //蟻の作成
/*        for(int i=0;i<ant.length;i++){
            ant[i] = new ant(); 
        }
*/        
        
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
                }else if(object_kind == ANT && ant_size<MAX_ant){
                    ant[ant_size] = new ant(); 
                    ant[ant_size].set(object_x,object_y,ant_size,0);
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
//                double F = grand.Neight(ant[an],Range);
                //持ち上げられる状態か
                if(antOperation.pick(ant[an],antClone[an],grand,i,x,y));
                //降ろせる状態か
                else if(antOperation.drop(ant[an],antClone[an],grand,i,x,y));
            }
            //state,antの更新
            ant = antClone;
            grand.resetState();
            //ランダム移動
            M.wander(i,ant,grand);
            grand.setPheromone(ant);
            grand.lostP();
        }
        System.out.println("\r ");
    }
//**********************************************************************************************//    
    //***配置状態の表示(蟻有)***//
    public void Check(){    
        int object[]=new int[MAX_kind+1];
        int object2[]=new int[ANT];
        int[][] A = new int[MAX_size][MAX_size];
        for(int k=0;k<(int)(MAX_ant*0.2);k++){
            A[ant[k].Location.y][ant[k].Location.x]++;
        }
        for (int i=0;i<grand.state.length;i++) {
            //要素の様子
            for(int j=0;j<grand.state.length;j++){
                System.out.print(grand.state[i][j] + " ");
                if(grand.state[i][j]!=0)
                    object[grand.state[i][j]]++;
            }
            System.out.print("        ");
            //蟻の様子
            for(int j=0;j<grand.state.length;j++){
                System.out.print(grand.ant[i][j] + " ");
            }
            System.out.print("        ");
            //エージェント蟻の様子
            for(int j=0;j<grand.state.length;j++){
                System.out.print(A[j][i]+" ");
            }
            
            System.out.println("");
        }
        System.out.println("");
        CheckP();
        //オブジェクトを持ち上げている蟻の表示
        for(int an=0;an<MAX_ant;an++){
            if(ant[an].State!=0){
                System.out.println("antNo."+(an+1)+" ("+ant[an].Location.x+","+ant[an].Location.y+") = "+ant[an].State);
                object2[ant[an].State]++;
            }
        }
        for(int b=1;b<object.length;b++)
            System.out.println("object "+b+" = "+object[b]);
        for(int b=0;b<=MAX_kind;b++)
            if(object2[b]!=0)
                System.out.println(b+" objectを持つあり = "+object2[b]);
    }
    //***配置状態の表示(蟻無)***//
    public void NotAntCheck(){
        for (int[] loop : grand.state) {
            for (int j = 0; j<grand.state.length; j++) {
                System.out.print(loop[j] + " ");   
            }
            System.out.println("");
        }
    }
    public void CheckAnt(){
        for (int[] loop : grand.ant) {
            for (int j = 0; j<grand.ant.length; j++) {
                System.out.print(loop[j] + " ");
            }
            System.out.println("");
        }       
    }
    public void CheckAnt2(){
        int[][] A = new int[MAX_size][MAX_size];
        for(int k=0;k<(int)(MAX_ant*0.2);k++){
            A[ant[k].Location.y][ant[k].Location.x]++;
        }
        for (int i=0;i<grand.state.length;i++) {
            for(int j=0;j<grand.state.length;j++){
                System.out.print(A[j][i]+" ");
            }
            System.out.println();
        }
        
    }
    public void CheckP(){
        for(int i=0;i<grand.state.length;i++){
            for(int k=1;k<=MAX_kind;k++){
                for(int j=0;j<grand.state.length;j++){
                    int word = (int) (grand.pheromone[k][i][j]);
                    System.out.print(word + " ");
                }
                System.out.print("        ");
            }
            System.out.println();
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
                System.out.print("\r");
                System.out.print("--------------------"+i+"--------------------");
            }else if(i%limitCount==0){
                System.out.print("\r");
                System.out.print("--------------------"+i+"--------------------");
            }else if(i==(limitCount-1)){
                System.out.print("\r");
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
    //***蟻の移動　ランダム***//
    public void wander(int i){   
        //蟻すべてがランダムに移動
        int an=0;
        for(;an<ant_E;an++){
            //移動が完了するまで
            if(ant[an].time < limitMoveTime){
                Moves(ant[an]);
            }else{
                Moves2(ant[an]);
            }
        }
        grand.C=antOperation.PAround(grand.pheromone);
        for(;an<MAX_ant;an++){
            if(ant[an].State!=0&&i>HalfIteration){
                Carry(ant[an]);
            }else if(ant[an].time < limitMoveTime){
                Move(ant[an]);
            }else{
                Move2(ant[an]);
            }
        }
    }
    //***ランダム移動***//
    public void Move(ant an){
        int k,x,y,count=0;
        Random rnd = new Random();
        while(count<30){
            count++;
            k = rnd.nextInt(M.move_x[8].length);
            x = an.old.x + M.move_x[8][k];
            y = an.old.y + M.move_y[8][k];
            //移動先が存在する
            if(grand.MovingANT(x, y, an))
                break;
        }        
    }
    public void Move2(ant an){
        int k,x,y,count=0;
        Random rnd = new Random();
        while(count<40){
                count++;
                k = rnd.nextInt(moveX.length);
                x = an.old.x + moveX[k];
                y = an.old.y + moveY[k];
                if(grand.MovingANT(x, y, an))
                    break;
            }
    }
    public void Moves(ant an){
        int k,x,y,count=0;
        int[] Q = antOperation.RandomQ(an, grand.ant,1);
        if(Q[0]!=-1){
            Random rnd = new Random();
            k = Q[rnd.nextInt(Q.length)];
        }
        else{
            Move(an);
            return;
        }
        x = an.old.x + M.move_x[8][k];
        y = an.old.y + M.move_y[8][k];
        if(grand.MovingANT(x, y, an))
            return;
    }
    public void Moves2(ant an){
        int k,x,y,count=0;
        int[] Q = antOperation.RandomQ(an, grand.ant,2);
        if(Q[0]!=-1){
            Random rnd = new Random();
            k = Q[rnd.nextInt(Q.length)];
        }
        else{
            Moves(an);
            return;
        }
        x = an.old.x + M.move_X[8][k];
        y = an.old.y + M.move_Y[8][k];
        //移動先が存在する
        if(grand.MovingANT(x, y, an))
            return;
    }
    //***物を持っている時***//
    public void Carry(ant an){
        int k,x,y,count=0;
        Random rnd = new Random();
        int Q = antOperation.CarryAround(an, grand.C);
        while(count<30){
            count++;
            k = rnd.nextInt(M.move_x[Q].length);
            x = an.old.x + M.move_x[Q][k];
            y = an.old.y + M.move_y[Q][k];
            //移動先が存在する
            if((x>0&&x<MAX_size)&&(y>0&&y<MAX_size)){
                //以前の位置から蟻を削除                   
                //蟻の移動
                Point P  = an.old;
                an.Move(x, y);
                //移動後の位置に蟻を追加
                grand.resetAnt(an,P);
                //ループ強制抜け
                break;
            }
        }        
    }
//**********************************************************************************************//
}
    