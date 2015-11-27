
package antclustering;

import static antclustering.Field.limitMoveTime;
import java.awt.Point;
import java.util.Random;

public class M {
    //  乱数によって動かす方向を決定するための配列
    static final int[] movex = {-1, 0, 1,-1, 1,-1, 0, 1};
    static final int[] movey = {-1,-1,-1, 0, 0, 1, 1, 1};
    static final int[] moveX = {-2,-1, 0, 1, 2,-2,-1, 0, 1, 2,-2,-1, 1, 2,-2,-1, 0, 1, 2,-2,-1, 0, 1, 2};
    static final int[] moveY = {-2,-2,-2,-2,-2,-1,-1,-1,-1,-1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2};
    /********************************/
    /* -1, 0, 1            -1,-1,-1 */
    /* -1  ,  1             0  ,  0 */
    /* -1, 0, 1             1, 1, 1 */
    /********************************/
    static final int[][] move_x = {{ 1, 1,-1, 0, 1},{-1, 1,-1, 0, 1},{-1,-1,-1, 0, 1},{ 0, 1, 1, 0, 1},{-1, 0,-1,-1, 0},{-1, 0, 1, 1, 1},{-1, 0, 1,-1, 1},{-1, 0, 1,-1,-1},{-1, 0, 1,-1, 1,-1, 0, 1}};
    static final int[][] move_y = {{-1, 0, 1, 1, 1},{ 0, 0, 1, 1, 1},{-1, 0, 1, 1, 1},{-1,-1, 0, 1, 1},{-1,-1, 0, 1, 1},{-1,-1,-1, 0, 1},{-1,-1,-1, 0, 0},{-1,-1,-1, 0, 1},{-1,-1,-1, 0, 0, 1, 1, 1}};
    /**************************************/
    /*-2,-1, 0, 1, 2        -2,-2,-2,-2,-2*/
    /*-2,-1, 0, 1, 2        -1,-1,-1,-1,-1*/
    /*-2,-1, ,  1, 2         0, 0, ,  0, 0*/
    /*-2,-1, 0, 1, 2         1, 1, 1, 1, 1*/
    /*-2,-1, 0, 1, 2         2, 2, 2, 2, 2*/
    /**************************************/
    
    static final int[][] move_X = {{ 2, 1, 2, 1, 2,-1, 0, 1, 2,-2,-1, 0, 1, 2},{-2,-1, 1, 2,-2,-1, 0, 1, 2,-2,-1, 0, 1, 2},{-2,-2,-1,-2,-1,-2,-1, 0, 1,-2,-1, 0, 1, 2},{ 0, 1, 2, 0, 1, 2, 1, 2, 0, 1, 2, 0, 1, 2},{-2,-1, 0,-2,-1, 0,-2,-1,-2,-1, 0,-2,-1, 0},
                                   {-2,-1, 0, 1, 2,-1, 0, 1, 2, 1, 2, 1, 2, 2},{-2,-1, 0, 1, 2,-2,-1, 0, 1, 2,-2,-1, 1, 2},{-2,-1, 0, 1, 2,-2,-1, 0, 1,-2,-1,-2,-1,-2},{-2,-1, 0, 1, 2,-2,-1, 0, 1, 2,-2,-1, 1, 2,-2,-1, 0, 1, 2,-2,-1, 0, 1, 2}};
    static final int[][] move_Y = {{-2,-1,-1, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2},{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2},{-2,-1,-1, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2},{-2,-2,-2,-1,-1,-1, 0, 0, 1, 1, 1, 2, 2, 2},{-2,-2,-2,-1,-1,-1, 0, 0, 1, 1, 1, 2, 2, 2},{-2,-2,-2,-2,-2,-1,-1,-1,-1, 0, 0, 1, 1, 2},
                                   {-2,-2,-2,-2,-2,-1,-1,-1,-1,-1, 0, 0, 0, 0},{-2,-2,-2,-2,-2,-1,-1,-1,-1, 0, 0, 1, 1, 2},{-2,-2,-2,-2,-2,-1,-1,-1,-1,-1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2},};
        //***蟻の移動　ランダム***//
    public static void wander(int i,ant[] ant,Grand grand){   
        //蟻すべてがランダムに移動
        int an=0;
        for(;an<Field.ant_E;an++){
            //移動が完了するまで
            if(ant[an].time < Field.limitMoveTime){
                Moves(ant[an],grand);
            }else{
                Moves2(ant[an],grand);
            }
        }
    //    grand.C=antOperation.PAround(grand.pheromone);
        for(;an<Field.MAX_ant;an++){
            if(ant[an].State!=0&&i>Field.HalfIteration){
                Carry(ant[an],grand);
            }else if(ant[an].time < Field.limitMoveTime){
                Move(ant[an],grand);
            }else{
                Move2(ant[an],grand);
            }
        }
    }
    //***ランダム移動***//
    public static void Move(ant an,Grand grand){
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
    public static void Move2(ant an,Grand grand){
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
    public static void Moves(ant an,Grand grand){
        int k,x,y,count=0;
        int[] Q = antOperation.RandomQ(an, grand.ant,1);
        if(Q[0]!=-1){
            Random rnd = new Random();
            k = Q[rnd.nextInt(Q.length)];
        }
        else{
            Move(an,grand);
            return;
        }
        x = an.old.x + M.move_x[8][k];
        y = an.old.y + M.move_y[8][k];
        if(grand.MovingANT(x, y, an))
            return;
    }
    public static void Moves2(ant an,Grand grand){
        int k,x,y,count=0;
        int[] Q = antOperation.RandomQ(an, grand.ant,2);
        if(Q[0]!=-1){
            Random rnd = new Random();
            k = Q[rnd.nextInt(Q.length)];
        }
        else{
            Moves(an,grand);
            return;
        }
        x = an.old.x + M.move_X[8][k];
        y = an.old.y + M.move_Y[8][k];
        //移動先が存在する
        if(grand.MovingANT(x, y, an))
            return;
    }
    //***物を持っている時***//
    public static void Carry(ant an,Grand grand){
        int k,x,y,count=0;
        Random rnd = new Random();
//        int Q = antOperation.CarryAround(an, grand.C);
        int Q = antOperation.CarryAround2(an, grand,2);
        while(count<30){
            count++;
            k = rnd.nextInt(M.move_x[Q].length);
            x = an.old.x + M.move_x[Q][k];
            y = an.old.y + M.move_y[Q][k];
            //移動先が存在する
            if((x>0&&x<Field.MAX_size)&&(y>0&&y<Field.MAX_size)){
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
}
