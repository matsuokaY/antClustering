
package antclustering;

import java.awt.Point;
import java.util.Random;

public class Data {
    //**********************************************************************************************//
    public Grand grand ;
    //蟻
    public int ant_E;
    public ant[] ant;
    //閾値
    static final double A = 1;
    static final double Kp = 1;
    static final double Kd = 0.3;
    static int Range = 2;
    static double V = 2;
    static final int Memory_size = 6;   
    //*****************************************************************************//
    public int MAX_size;
    public int MAX_object;
    public int MAX_kind;
    public int MAX_state;
    public int MAX_ant;
    public int ANT;
    //繰り返し回数
    public int Iteration;
    public int limitMoveTime;
    public int limitKeepTime;
    public int limitCount;
    //繰り返し回数の半分
    public int HalfIteration;
    //繰り返し回数の三分の一
    public int ThirdIteration;
    //*****************************************************************************//
    //***数値の初期化***//
    public void set(int size,int object,int kind,int ant,int iteration,int limitmovetime){
        
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
        
    }
    //***初期配置を行う***//
    public void Fieldset(){
        Random rnd = new Random();
        grand = new Grand();
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
                grand.setAnt(object_x,object_y,ant_size);
                ant_size++;
            }
        }
    }
    
    public void clone(Data data){
        MAX_size = data.MAX_size;
        MAX_object = data.MAX_object;
        MAX_kind = data.MAX_kind;
        MAX_state = data.MAX_state;
        MAX_ant = data.MAX_ant;
        ANT = data.ANT;
        //繰り返し回数
        Iteration = data.Iteration;
        limitMoveTime = data.limitMoveTime;
        limitKeepTime = data.limitKeepTime;
        limitCount = data.limitCount;
        //繰り返し回数の半分
        HalfIteration = data.HalfIteration;
        //繰り返し回数の三分の一
        ThirdIteration = data.ThirdIteration;
        //*****************************************************************************//
        this.grand = new Grand();
        this.grand.state = new int[data.MAX_size][data.MAX_size];
        this.grand.ant = new int[data.MAX_size][data.MAX_size];
        this.ant_E = (int) (data.MAX_ant*0.2);
        this.ant = new ant[data.MAX_ant];
        
        for(int i=0;i<grand.ant.length;i++)
            for(int j=0;j<grand.ant.length;j++){
                this.grand.ant[i][j]= (int)data.grand.ant[i][j];
                this.grand.state[i][j]=(int)data.grand.state[i][j];
            }
        for(int k=0;k<ant.length;k++){
            this.ant[k] = new ant();
            this.ant[k].Location = new Point(data.ant[k].Location.x,data.ant[k].Location.y);
            this.ant[k].Memory = new Memory();
            this.ant[k].Memory.state = new int[data.ant[k].Memory.state.length];
            this.ant[k].Memory.P = new Point[data.ant[k].Memory.P.length];
            for(int l=0;l<data.ant[k].Memory.P.length;l++){
                this.ant[k].Memory.P[l] = new Point();
            }
            this.ant[k].No=data.ant[k].No;
            this.ant[k].State=data.ant[k].State;
            this.ant[k].time=data.ant[k].time;
        }
        
    }

}
