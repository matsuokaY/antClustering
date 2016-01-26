
package antclustering;

import java.awt.Point;
import java.util.ArrayList;


class value{
    ant[] ant;
    Grand grand;
}

class result{
    int value;
    int state;
}

public class antOperation {
    static int pickCount = 5;
    static int dropCount = 4;

    static final int R = 4;
    static final int r = 1;
    
    //***周りがpickCount以下なら持ち上げる***//
    public static boolean pickObject(int an,int iteration){
        //三分の一
        if(iteration<Field.Iteration/2)
            return an<=pickCount-1;
        //三分の二
        else if(iteration<Field.Iteration/3*2)
            return an<=pickCount;
        //残り
        else
            return an<=pickCount;
    }
    //***周りがpickCount以上なら降ろす***//
    public static boolean dropObject(int an,int iteration){
        if(iteration<Field.Iteration/2)
            return an>=dropCount-1;
        else if(iteration<Field.Iteration/3*2)
            return an>=dropCount;
        else
            return an>=dropCount;
    }

            //蟻がいるかどうか
    static boolean is_stayingAnt(int[][] A,Point P){
        if(A[P.y][P.x]==0)
            return true;
        else
            return false;
    }
    //何か持っているか
    static boolean is_carryingObject(ant ant) {
        return ant.State!=0;
    }    
    //何も持っていないか
    static boolean is_unloading(ant ant) {
        return ant.State==0;
    }
    //置ける状態か
    static boolean is_cellEmpty(Point P,int[][] A) {
        return A[P.y][P.x]==0;
    }
    //持ち上げれる状態か
    static boolean has_object(Point P,int[][] A) {
        return A[P.y][P.x]!=0;
    }
    //**********************************************************************************************//    
    public static int[] RandomQ(ant ant,int[][] A,int Re){
        int[] value = new int[4*Re*Re+4*Re];
        int x=ant.Location.x,y=ant.Location.y,count=0;
        ArrayList<Integer> List=new ArrayList<>();
        for(int j=y-Re;j<=y+Re;j++){
                for(int i=x-Re;i<=x+Re;i++){
                    if(i!=x||j!=y){
                        if(i>=0&&i<A.length&&j>=0&&j<A.length&&A[j][i]==0)
                                List.add(count);
                        count++;
                    }
                }
        }
        if(List.size() == 0){
            int[] result ={-1};
            return result;
        }
        int[] result =new int[List.size()];
        for(int k=0;k<List.size();k++){
            result[k] = List.get(k);
        }
        return result;
    }

    public static void c (ant[] ant){
        for(int t=0;t<ant.length;t++)
            for(int s=0;s<ant.length;s++)
                if(t!=s&&ant[t].Location.x==ant[s].Location.x&&ant[t].Location.y==ant[s].Location.y)
                    System.out.println(t+" "+s);
        
    }
    
    static final int[] movex = {-1, 0, 1,-1, 1,-1, 0, 1};
    static final int[] movey = {-1,-1,-1, 0, 0, 1, 1, 1};
    static Point Memory(int[][] grand,ant an,ant[] ant) {
        Point P =new Point();
        P.x=-1;
        P.y=-1;
        int state=0;
        for(int k=0;k<movey.length;k++){
            //付近のアリのメモリーに情報が入っているか
            if(an.Location.y+movey[k]>=0&&an.Location.y+movey[k]<grand.length&&an.Location.x+movex[k]>=0&&an.Location.x+movex[k]<grand.length && ant[grand[an.Location.y+movey[k]][an.Location.x+movex[k]]].Memory.number!=0)
                for(int l=0;l<Data.Memory_size;l++)
                    //付近のアリが持っているStateと同じ種類の位置情報を保持しているか
                    if(ant[grand[an.Location.y+movey[k]][an.Location.x+movex[k]]].Memory.state[l]==an.State&&state==0){
                        P = ant[grand[an.Location.y+movey[k]][an.Location.x+movex[k]]].Memory.P[l];
                        state = 1;
                    }else if(ant[grand[an.Location.y+movey[k]][an.Location.x+movex[k]]].Memory.state[l]==an.State&&state==1){
                        //確率でメモリを更新
                        if(Math.random()<0.5)
                            P = ant[grand[an.Location.y+movey[k]][an.Location.x+movex[k]]].Memory.P[l];
                    }
        }
        return P;
    }
}
