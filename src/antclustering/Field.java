package antclustering;

import static antclustering.antOperation.dropObject;
import static antclustering.antOperation.has_object;
import static antclustering.antOperation.is_carryingObject;
import static antclustering.antOperation.is_cellEmpty;
import static antclustering.antOperation.is_stayingAnt;
import static antclustering.antOperation.is_unloading;
import static antclustering.antOperation.pickObject;
import java.awt.Point;


//

public class Field {
    static Grand grand ;
    //蟻
    static int ant_E;
    static ant[] ant; 
    //*****************************************************************************//
    static int MAX_kind;
    static int ANT;
    //繰り返し回数
    static int Iteration;
    static int limitMoveTime;
    static int limitKeepTime;
    static int limitCount;
    static int count;
    public static void local_initial(Data data){     
//        grand.Check();
        setData(data);
        NotAntCheck(grand);
        System.out.println("\nクラスタリング開始");
        Clustering(grand,ant);
        System.out.println("クラスタリング終了");
        System.out.println("\n/****************************************************************************/");
        Check(grand,ant);
    }

//**********************************************************************************************//
    //***クラスタリングの実行***//
    public static void Clustering(Grand grand,ant[] ant){
        int Memo;
        Point P = new Point();
        
        grand.set(ant.length);
        //「Interation」の数だけ繰り返し
        for(int i=0;i<Iteration;i++){
            //回数表示
//            Print(i,grand);
//            antOperation.c(ant);
            //クローンの作成
            //すべての蟻で実行
            for(int an=0;an<ant.length;an++){
                //蟻の行動
                //drop
                if(is_carryingObject(ant[an])&&is_cellEmpty(ant[an].Location,grand.state))
                    if(dropObject(grand.AroundR(ant[an],1),i)){
                        grand.state[ant[an].Location.y][ant[an].Location.x]=ant[an].State;
                        ant[an].State=0;
                        ant[an].time=0;
                        ant[an].Memory.set_memory(ant[an].Location,ant[an].State);
                    }else
                        ant[an].time++;
                //pick
                else if(is_unloading(ant[an])&&has_object(ant[an].Location,grand.state))
                    if(pickObject(grand.AroundR(ant[an],1),i)){
                        ant[an].State=grand.state[ant[an].Location.y][ant[an].Location.x];
                        grand.state[ant[an].Location.y][ant[an].Location.x]=0;
                        ant[an].time=0;
                        Memo=ant[an].Memory.serch_memory(ant[an].State);
                        //該当なし
                        if(Memo!=-1&&!grand.MovingANT(ant[an].Memory.P[Memo].x,ant[an].Memory.P[Memo].y, ant[an])){
                            int x = ant[an].Memory.P[Memo].x,y = ant[an].Memory.P[Memo].y;
                            //ジャンプ先予定地に蟻がいなければジャンプ
                            if(is_stayingAnt(grand.ant,ant[an].Memory.P[Memo]))
                                if(!grand.MovingANT(x,y,ant[an]))
                                    System.out.println("s");
                            else//予定地に方向に向かって移動？ 
                                System.out.println("dame? ("+x+","+y+") "+grand.state[y][x]);

                            //ジャンプ後一回数移動を試みる
                            M.Moves(ant[an],grand);
                        }else{
                            P=antOperation.Memory(grand.ant,ant[an],ant);
                            if(0<P.x&&0<P.y&&is_stayingAnt(grand.ant,P)){
                                if(!grand.MovingANT(P.x,P.y,ant[an]))
                                    System.out.println("s");
                                M.Moves(ant[an],grand);
                            }else if(0<P.x&&0<P.y)//予定地に方向に向かって移動？ 
                                System.out.println("dame? ("+P.x+","+P.y+") "+grand.state[P.y][P.x]);
                        }
                    }else
                        ant[an].time++;
                //蟻の移動
                    M.Moves(ant[an],grand);
                
            }
        }
        System.out.println("\r ");
    }
//**********************************************************************************************//    
    //***配置状態の表示(蟻有)***//
    public static void Check(Grand grand,ant[] ant){    
        int object[]=new int[MAX_kind+1];
        int object2[]=new int[ANT];
        int[][] A = new int[grand.state.length][grand.state.length];
        int[][] B = new int[grand.state.length][grand.state.length];
        for(int k=0;k<ant.length;k++){
            A[ant[k].Location.y][ant[k].Location.x]++;
        }
        for(int k=0;k<ant.length;k++){
            if(ant[k].State!=0)
                B[ant[k].Location.y][ant[k].Location.x]=ant[k].State;
        }
        for (int i=0;i<grand.state.length;i++) {
            //要素の様子
            for(int j=0;j<grand.state.length;j++){
                if(grand.state[i][j]!=0){
                    System.out.print(grand.state[i][j] + " ");
                    
                    object[grand.state[i][j]]++;
                }else if(B[i][j]!=0)
                    System.out.print(B[i][j] + " ");
                else
                    System.out.print(grand.state[i][j] + " ");
            }
            System.out.print("        ");
            //蟻の様子
            for(int j=0;j<grand.state.length;j++){
                if(grand.ant[i][j]!=0&&grand.ant[i][j]<10)
                    System.out.print(0+""+grand.ant[i][j] + " ");
                else if(grand.ant[i][j]!=0)
                    System.out.print(grand.ant[i][j] + " ");
                else
                    System.out.print(0+""+0 + " ");
            }
            System.out.print("        ");
            //蟻の様子2
            for(int j=0;j<grand.state.length;j++){
                System.out.print(A[i][j]+" ");
            }
            System.out.print("        ");
            //置かれていない要素
            for(int j=0;j<grand.state.length;j++){
                System.out.print(B[i][j]+" ");
            }
            System.out.println("");
        }
        System.out.println("");
        //オブジェクトを持ち上げている蟻の表示
        for(int an=0;an<ant.length;an++){
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
    public static void NotAntCheck(Grand grand){
        for (int[] loop : grand.state) {
            for (int j = 0; j<grand.state.length; j++) {
                System.out.print(loop[j] + " ");   
            }
            System.out.println("");
        }
    }
    public static void CheckAnt(Grand grand){
        for (int[] loop : grand.ant) {
            for (int j = 0; j<grand.ant.length; j++) {
                System.out.print(loop[j] + " ");
            }
            System.out.println("");
        }       
    }
    public static void CheckAnt2(Grand grand,ant[] ant){
        int[][] A = new int[grand.state.length][grand.state.length];
        for(int k=0;k<(int)(ant.length*0.2);k++){
            A[ant[k].Location.y][ant[k].Location.x]++;
        }
        for (int i=0;i<grand.state.length;i++) {
            for(int j=0;j<grand.state.length;j++){
                System.out.print(A[j][i]+" ");
            }
            System.out.println();
        }
        
    }
    public static void CheckP(Grand grand){
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
    public static void Print(int i,Grand grand){
/*        if(i==Iteration/3){
            System.out.print("\r途中結果\n");
            NotAntCheck(grand);
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
        }*/
        count++;
        if(count/100000==1){
            int[][] A = new int[grand.state.length][grand.state.length];
            for(int k =0 ;k<ant.length;k++){
                A[ant[k].Location.y][ant[k].Location.x]++;
            }
            for(int y=0;y<grand.state.length;y++){
                for(int x=0;x<grand.state.length;x++)
                    System.out.print(grand.ant[y][x]+" ");
                System.out.print("        ");
                for(int x=0;x<grand.state.length;x++)
                    System.out.print(A[y][x]+" ");
                System.out.println("");
            }
            System.out.println("-----------------------------------------------");
            count=0;
        }
    }
//**********************************************************************************************//    
    //***ユークリッド距離の計算***//
    public static double EuclideanLenght(int x1,int y1,int x2,int y2){
        return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }

//**********************************************************************************************//
    private static void setData(Data data) {
        grand = data.grand;
    //蟻
        ant_E = data.ant_E;
        ant = data.ant.clone(); 
    //*****************************************************************************//
        MAX_kind = data.MAX_kind;
        ANT = data.ANT;
    //繰り返し回数
        Iteration = data.Iteration;
        limitMoveTime = data.limitMoveTime;
        limitKeepTime = data.limitKeepTime;
        limitCount = data.limitCount; 
    }
}
    