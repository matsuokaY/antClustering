
package antclustering;

import java.awt.Point;
import java.util.Random;

class Memory{
    Point[] P;
    int[] state;
    int number;
}
public class normal {
//現在の状態(存在している物体の種類)
    public static Grand grand ;
    //蟻
    public static int ant_E;
    public static ant[] ant;
    //閾値
    static final double A = 0.5;
    static final double K1 = 0.5;
    static final double K2 = 0.5;
    static int Range = 2;
    static int V = 3;
    static final int Memory_size = 6;
    
    //*****************************************************************************//
    static int MAX_size;
    static int MAX_object;
    static int MAX_kind;
    static int MAX_state;
    static int MAX_ant;
    static int ANT;
    //繰り返し回数
    static int Iteration;
    static int limitMoveTime;
    static int limitKeepTime;
    static int limitCount;
    //繰り返し回数の半分
    static int HalfIteration;
    //繰り返し回数の三分の一
    static int ThirdIteration;
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
        limitMoveTime = 8;
        limitKeepTime = (int) (limitmovetime*1.5);
        limitCount = iteration / 100;
        HalfIteration = iteration/2;
        ThirdIteration = iteration/3;
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
        
        
        //配置
    //オブジェクトの配置
        while(object_size<MAX_object){
            object_x = rnd.nextInt(MAX_size);
            object_y = rnd.nextInt(MAX_size);
            object_kind = rnd.nextInt(MAX_kind+1);
            if(object_kind!=0){
                if(grand.state[object_y][object_x]==0){
                    grand.setState(object_x,object_y,object_kind);
                    object_size++;
                }
            }
        }
    //蟻の配置
        while(ant_size<MAX_ant){
            object_x = rnd.nextInt(MAX_size);
            object_y = rnd.nextInt(MAX_size);
            if(grand.ant[object_y][object_x]==0){
                ant[ant_size] = new ant();
                ant[ant_size].set(object_x,object_y,ant_size,0);
                ant[ant_size].Memory.state = new int[Memory_size];
                for(int i=0;i<Memory_size;i++){
                    ant[ant_size].Memory.P[i] = new Point();
                }
                grand.setAnt(object_x,object_y);
                ant_size++;
            }
        }
    }
    public void Clustering() {
        int Memo=0,k;
        Random rnd = new Random();
        //「Interation」の数だけ繰り返し
        for(int t=0;t<Iteration;t++){
            //回数表示
            Print(t);
            //すべての蟻で実行
            for(int an=0;an<ant.length;an++){
                double F = f(ant[an],grand.state);
                if(is_carryingObject(ant[an])&&is_cellEmpty(ant[an].Location,grand.state)){
                    double R = Math.random();
                    if(Pdrop(F)>R){
                        ant[an].Memory = set_memory(ant[an].Memory,ant[an].Location,ant[an].State);
                        grand.state[ant[an].Location.y][ant[an].Location.x]=ant[an].State;
                        ant[an].State=0;
                    }
                }else if(is_unloading(ant[an])&&has_object(ant[an].Location,grand.state)){
                    double R = Math.random()*4+1;
                    if(Ppick(F)>R){
                        ant[an].State=grand.state[ant[an].Location.y][ant[an].Location.x];
                        grand.state[ant[an].Location.y][ant[an].Location.x]=0;
                        Memo=serch_memory(ant[an].Memory,ant[an].State);
                        //該当なし
                        if(Memo==-1)
                            break;
                        if(!grand.MovingANT(ant[an].Memory.P[Memo].x,ant[an].Memory.P[Memo].y, ant[an])){
                            int x = ant[an].Memory.P[Memo].x,y = ant[an].Memory.P[Memo].y;                   
                            //一定回数移動を試みる
                            for(int mt=0;mt<limitMoveTime;mt++){
                                if(grand.MovingANT(x, y, ant[an]))
                                    break;
                                k = rnd.nextInt(M.moveX.length);
                                x += M.moveX[k];
                                y += M.moveY[k];
                            }
                        }
                    }
                }
                //ランダム移動
                wanderBeta(t,ant[an],grand);
            }
            
        }
        System.out.println("\r ");   
    }
    private double Pdrop(double F){
        return Math.pow(K1/(K1+F), 2);
    }
    private double Ppick(double F){
        if(F<K2)
            return 2*F;
        else
            return 1;
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
    private void wanderBeta(int t, ant ant, Grand grand) {
        M.Moves(ant, grand);
    }
    private void wander(int t, ant ant, Grand grand) {
        int k,count=0;
        Random rnd = new Random();
        while(count<40){
            count++;
            int x = ant.Location.x+rnd.nextInt(2*V)-V;
            int y = ant.Location.y+rnd.nextInt(2*V)-V;
            //移動先に蟻がいない　かつ　移動先が存在する
                if(grand.MovingANT(x, y, ant)&&grand.ant[y][x]==0)
                    return;
        }        
    }
    //何か持っているか
    private boolean is_carryingObject(ant ant) {
        return ant.State!=0;
    }
    //何も持っていないか
    private boolean is_unloading(ant ant) {
        return ant.State==0;
    }
    //置ける状態か
    private boolean is_cellEmpty(Point P,int[][] A) {
        return A[P.y][P.x]==0;
    }
    //持ち上げれる状態か
    private boolean has_object(Point P,int[][] A) {
        return A[P.y][P.x]!=0;
    }
    //***ユークリッド距離の計算***//
    private double EuclideanLenght(int x1,int y1,int x2,int y2){
        return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }
    private double f(ant ant,int[][] grand) {
        double result = 0;
        for(int i=ant.Location.y-Range;i<ant.Location.y+Range;i++)
            for(int j=ant.Location.x-Range;j<ant.Location.x+Range;j++)
                if(((i>=0&&i<MAX_size)&&(j>=0&&j<MAX_size))&&ant.State==grand[i][j])
                    result += 1-EuclideanLenght(ant.Location.x,ant.Location.y,j,i)/Threshold();
        //蟻が知覚できる範囲のマスで割る
        result /= 4*Range*Range+4*Range+1;
        return Math.max(0, result);
    }
    //閾値
    private double Threshold(){
        return A*(1+V-1/V);
    }

    private Memory set_memory(Memory M, Point P,int State) {
        if(M.number == M.state.length)
            M.number = 0;
        M.P[M.number] = P;
        M.state[M.number]=State;
        M.number++;
        return M;
    }

    private int serch_memory(Memory M, int State) {
        int k=M.number;
        for(int i=0;i<M.state.length;i++){
            k-=i;
            if(k<0&&M.state[k+M.state.length]==State)
                return k;
            else if(M.state[k]==State)
                return k;
        }
        return -1;
    }

}
