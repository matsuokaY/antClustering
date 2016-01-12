package antclustering;

import java.awt.Point;
import static antclustering.antOperation.has_object;
import static antclustering.antOperation.is_carryingObject;
import static antclustering.antOperation.is_cellEmpty;
import static antclustering.antOperation.is_unloading;

public class Field {
    public static void local_initial(Grand grand,ant[] ant){     
//        grand.Check();
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
        int x,y;
        //「Interation」の数だけ繰り返し
        for(int i=0;i<Data.Iteration;i++){
            //回数表示
            Print(i,grand);           
            //クローンの作成
            //すべての蟻で実行
            for(int an=0;an<Data.MAX_ant;an++){
                //蟻の行動
                if(is_carryingObject(ant[an])&&is_cellEmpty(ant[an].Location,grand.state))
                    if(antOperation.dropObject(grand.AroundR(ant[an],1),i)){
                        grand.state[ant[an].Location.y][ant[an].Location.x]=ant[an].State;
                        ant[an].State=0;
                        ant[an].time=0;
                    }else
                        ant[an].time++;
                else if(is_unloading(ant[an])&&has_object(ant[an].Location,grand.state))
                    if(antOperation.pickObject(grand.AroundR(ant[an],1),i)){
                        ant[an].State=grand.state[ant[an].Location.y][ant[an].Location.x];
                        grand.state[ant[an].Location.y][ant[an].Location.x]=0;
                        ant[an].time=0; 
                    }else
                        ant[an].time++;
                //蟻の移動
                if(ant[an].time < Data.limitMoveTime)
                    M.Moves(ant[an],grand);
                else
                    M.Moves2(ant[an],grand);
                
            }
            //state,antの更新

            //ランダム移動
//            M.wander(i,ant,grand);
//            grand.setPheromone(ant);
//            grand.lostP();
        }
        System.out.println("\r ");
    }
//**********************************************************************************************//    
    //***配置状態の表示(蟻有)***//
    public static void Check(Grand grand,ant[] ant){    
        int object[]=new int[Data.MAX_kind+1];
        int object2[]=new int[Data.ANT];
        int[][] A = new int[Data.MAX_size][Data.MAX_size];
        for(int k=0;k<(int)(Data.MAX_ant*0.2);k++){
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
//        CheckP();
        //オブジェクトを持ち上げている蟻の表示
        for(int an=0;an<Data.MAX_ant;an++){
            if(ant[an].State!=0){
                System.out.println("antNo."+(an+1)+" ("+ant[an].Location.x+","+ant[an].Location.y+") = "+ant[an].State);
                object2[ant[an].State]++;
            }
        }
        for(int b=1;b<object.length;b++)
            System.out.println("object "+b+" = "+object[b]);
        for(int b=0;b<=Data.MAX_kind;b++)
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
        int[][] A = new int[Data.MAX_size][Data.MAX_size];
        for(int k=0;k<(int)(Data.MAX_ant*0.2);k++){
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
            for(int k=1;k<=Data.MAX_kind;k++){
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
        if(i==Data.ThirdIteration){
                System.out.print("\r");
                NotAntCheck(grand);
                System.out.println("Change");
                System.out.print("--------------------"+i+"--------------------");
            }
            else if(Data.Iteration>=1000000&&i==0){
                System.out.print("\r");
                System.out.print("--------------------"+i+"--------------------");
            }else if(i%Data.limitCount==0){
                System.out.print("\r");
                System.out.print("--------------------"+i+"--------------------");
            }else if(i==(Data.limitCount-1)){
                System.out.print("\r");
            }
    }
//**********************************************************************************************//    
    //***ユークリッド距離の計算***//
    public static double EuclideanLenght(int x1,int y1,int x2,int y2){
        return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }

//**********************************************************************************************//

}
    