
package antclustering;

import java.awt.Point;
import java.util.Random;

public class Data {
    //**********************************************************************************************//
    public static Grand grand ;
    //蟻
    public static int ant_E;
    public static ant[] ant;
    //閾値
    static final double A = 1;
    static final double Kp = 1;
    static final double Kd = 0.3;
    static int Range = 2;
    static double V = 2;
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
    }
    //***初期配置を行う***//
    public void Fieldset(){
        Random rnd = new Random();
        grand.state = new int[MAX_size][MAX_size];
        grand.ant = new int[MAX_size][MAX_size];
//        grand.pheromone = new double[MAX_kind+1][MAX_size][MAX_size];
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
                ant[ant_size].Memory = new Memory();
                ant[ant_size].Memory.state = new int[Memory_size];
                ant[ant_size].Memory.P = new Point[Memory_size];
                for(int i=0;i<Memory_size;i++){
                    ant[ant_size].Memory.P[i] = new Point();
                }
                grand.setAnt(object_x,object_y);
                ant_size++;
            }
        }
    }
}
