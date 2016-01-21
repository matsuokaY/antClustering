
package antclustering;

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
    
    //***ランダム移動***//
    public static void Move(ant an,Grand grand){
        int k,x,y,count=0;
        Random rnd = new Random();
        while(count<30){
            count++;
            k = rnd.nextInt(M.move_x[8].length);
            x = an.Location.x + M.move_x[8][k];
            y = an.Location.y + M.move_y[8][k];
            //移動先が存在する
            if(grand.MovingANT(x, y, an))
                break;
        }        
    }
    //***ランダム移動　距離2***//
    public static void Move2(ant an,Grand grand){
        int k,x,y,count=0;
        Random rnd = new Random();
        while(count<40){
                count++;
                k = rnd.nextInt(moveX.length);
                x = an.Location.x + moveX[k];
                y = an.Location.y + moveY[k];
                if(grand.MovingANT(x, y, an))
                    break;
            }
    }
    //***他の蟻を避ける移動***//
    public static void Moves(ant an,Grand grand){
        int k,x,y;
        int[] Q = antOperation.RandomQ(an, grand.ant,1);
        Point P =new Point();
        if(Q[0]!=-1){
            Random rnd = new Random();
            k = Q[rnd.nextInt(Q.length)];
        }
        else{
//            Move(an,grand);
            return;
        }
        x = an.Location.x + M.move_x[8][k];
        y = an.Location.y + M.move_y[8][k];
        if(!grand.MovingANT(x,y, an))
            System.out.println("困った");
    }
    //***他の蟻を避ける移動　距離2***//
    public static void Moves2(ant an,Grand grand){
        int k,x,y;
        int[] Q = antOperation.RandomQ(an, grand.ant,2);
        if(Q[0]!=-1){
            Random rnd = new Random();
            k = Q[rnd.nextInt(Q.length)];
        }
        else{
//            Move2(an,grand);
            return;
        }
        x = an.Location.x + M.move_X[8][k];
        y = an.Location.y + M.move_Y[8][k];
        
        //移動先が存在する
        if(!grand.MovingANT(x, y, an))
            System.out.println("困った");
    }

}
