
package antclustering;

import java.util.ArrayList;


class value{
    ant[] ant;
    Grand grand;
}

public class antOperation {
    static int pickCount = 3;

    static final int R = 4;
    static final int r = 1;
    //  乱数によって動かす方向を決定するための配列
    static final int[] movex = {-1, 0, 1,-1, 1,-1, 0, 1};
    static final int[] movey = {-1,-1,-1, 0, 0, 1, 1, 1};
    static final int[] moveX = {-2,-1, 0, 1, 2,-2,-1, 0, 1, 2,-2,-1, 1, 2,-2,-1, 0, 1, 2,-2,-1, 0, 1, 2};
    static final int[] moveY = {-2,-2,-2,-2,-2,-1,-1,-1,-1,-1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2};
    
    //***周りがpickCount以下なら持ち上げる***//
    public static boolean pickObject(int an,int iteration){
        //三分の一
        if(iteration<Field.ThirdIteration)
            return an<=pickCount-1;
        //三分の二
        else if(iteration<Field.ThirdIteration*2)
            return an<=pickCount;
        //残り
        else
            return an<=pickCount;
    }
    //***周りがpickCount以上なら降ろす***//
    public static boolean dropObject(int an,int iteration){
        if(iteration<Field.ThirdIteration)
            return an>=pickCount-1;
        else if(iteration<Field.ThirdIteration*2)
            return an>=pickCount;
        else
            return an>=pickCount;
    }

    //***オブジェクトの持ち上げれるか***//
    public static boolean pickCheck(int an,int grand){
        // 蟻がオブジェクトを所持していない　かつ　場に蟻とオブジェクトが存在
        return an==0&&grand>0;
    }
    //***オブジェクトを降ろせるか***//
    public static boolean dropCheck(int an,int grand){
        //　蟻がオブジェクトを所持している　かつ　場にオブジェクトが存在しない
        return an!=0&&grand==0;
    }
    
    public static boolean pick(ant an,ant clone,Grand grand,int i,int x,int y){
        if(!pickCheck(an.State,grand.state[y][x]))
            return false;
        an.around=grand.AroundR(an,1);
        if(pickObject(an.around,i)){
            //蟻がオブジェクトを持ち上げる
            clone.Pick(x,y,grand);
        }else
            clone.time++;
        return true;
    }      
    public static boolean drop(ant an,ant clone,Grand grand,int i,int x,int y){
        if(!dropCheck(an.State,grand.state[y][x]))
            return false;
        an.around=grand.AroundR(an,1);
        if(dropObject(an.around,i)){
            //蟻がオブジェクトを下す
            grand.cloneState[y][x] = clone.Drop();
        }else
            clone.time ++;
        if(Field.limitKeepTime<clone.time&&an.around>0){
           //蟻がオブジェクトを下す
            grand.cloneState[y][x] = clone.Drop();
        }
        return true;
    }
    
    //**********************************************************************************************//
    //周りにある要素の中から最も多い要素を返す。
    public static int AroundState(ant ant, int[][] A){
        int x = ant.Location.x,y = ant.Location.y,X,Y,Xend,Yend,result=0;
        //蟻が認識する範囲
        X = Math.max(0,x-r);
        Xend = Math.min(x+r,Field.MAX_size-1);
        Y = Math.max(0,y-r);
        Yend = Math.min(y+r,Field.MAX_size-1);
        int[] state = new int[Field.MAX_kind+1];
        for(int i=X;i<Xend;i++){
            for(int j=Y;j<Yend;j++){
                state[A[j][i]]++;
            }
        }
        for(int i=1;i<state.length;i++)
            if(state[i]>state[result])
                result = i;
        return result;
    }
    //周辺n*nから蟻が多い方向(8方向)を見つける  n=R*2+1 R-rの範囲
    public static int Around(ant ant,int[][] A){
        int x = ant.Location.x,
            y = ant.Location.y,X,Y,Xend,Yend,result=0;
        int[] k = new int[8];
        //蟻が認識する範囲
        X = Math.max(0,x-R);
        Xend = Math.min(x+R,Field.MAX_size-1);
        Y = Math.max(0,y-R);
        Yend = Math.min(y+R,Field.MAX_size-1);
        //同一オブジェクト同士の類似
        for(int i=X;i<Xend;i++){
            for(int j=Y;j<Yend;j++){
                if(i<x-r&&j<y-r&&0<A[j][i])
                        k[0] += A[j][i];
                else if(i<x+r&&j<y-r&&0<A[j][i])
                    k[1] += A[j][i];
                else if(i<Xend&&j<y-r&&0<A[j][i])
                    k[2] += A[j][i];
                else if(i<x-r&&j<y+r&&0<A[j][i])
                    k[3] += A[j][i];
                else if(i<Xend&&j<y+r&&0<A[j][i])
                    k[4] += A[j][i];
                else if(i<x-r&&j<Yend&&0<A[j][i])
                    k[5] += A[j][i];
                else if(i<x+r&&j<Yend&&0<A[j][i])
                    k[6] += A[j][i];
                else if(i<Xend&&j<Yend)
                    k[7] += A[j][i];
            }
        }
        for(int i=1;i<k.length-1;i++)
            if(k[result]<k[i])
                result = i;
        return result;
        
    }
    //各フェロモンの最も高い濃度のところをの値と位置を返す
    public static C[] PAround(double[][][] A){
        C result[] = new C[Field.MAX_kind];
        for(int k=0;k<result.length;k++){
            result[k] = new C();
            for(int i=0;i<A[k].length;i++){
                for(int j=0;j<A[k][i].length;j++){
                    if(result[k].value<A[k+1][i][j]){
                        result[k].value=A[k+1][i][j];
                        result[k].x = i;
                        result[k].y = j;
                    }
                }
            }
        }
        return result;
    }
    //運ぶ時に向かうべきでない方向を見つける
    public static int CarryAround(ant ant,C[] C){
        /***********/
        /* 0  1  2 */
        /* 3  a  4 */
        /* 5  6  7 */
        /***********/
        int result=0;
        if(ant.Location.x<C[ant.State].x&&ant.Location.y<C[ant.State].y)
            result = 7;
        else if(ant.Location.x<C[ant.State].x&&ant.Location.y>C[ant.State].y)
            result = 2;
        else if(ant.Location.x>C[ant.State].x&&ant.Location.y<C[ant.State].y)
            result = 5;
        else if(ant.Location.x>C[ant.State].x&&ant.Location.y>C[ant.State].y)
            result = 0;
        return result;
    }
    //進むべき方向の選択肢決め
    public static int[] RandomQ(ant ant,int[][] A,int Re){
        int[] value = new int[4*Re*Re+4*Re];
        int x=ant.Location.x,y=ant.Location.y,count=0;
        ArrayList<Integer> List=new ArrayList<>();
        for(int j=y-Re;j<y+Re;j++){
            if(j!=y)
                for(int i=x-Re;i<x+Re;i++){
                    if(i!=x){
                        if(i>0&&i<Field.MAX_size&&j>0&&j<Field.MAX_size)
                            value[count] = A[j][i];
                        else
                            value[count] = -1;
                        count++;
                    }
                }
        }
        count=0;
        for(int k=0;k<value.length;k++)
            if(value[k]==0){
                List.add(k);
                count++;
            }
        int[] result =new int[count+1];
        if(count == 0)
            return result;
        for(int l=0;l<count;l++){
            result[l] = List.get(l);
        }
        return result;
    }
}
