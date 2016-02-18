
package antclustering;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Data {
    //**********************************************************************************************//
    public Grand grand ;
    //蟻
    public ant[] ant;
    
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
    public int limitCount;
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
        limitCount = iteration / 100;
        
    }
    //***初期配置を行う***//
    public void Fieldset(){
        Random rnd = new Random();
        grand = new Grand();
        grand.state = new int[MAX_size][MAX_size];
        grand.ant = new int[MAX_size][MAX_size];
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
                ant[ant_size].Memory.state = new int[MAX_kind+1];
                ant[ant_size].Memory.P = new Point[MAX_kind+1]; 
                for(int i=0;i<=MAX_kind;i++){
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
        limitCount = data.limitCount;
        //*****************************************************************************//
        this.grand = new Grand();
        this.grand.state = new int[data.MAX_size][data.MAX_size];
        this.grand.ant = new int[data.MAX_size][data.MAX_size];
        this.ant = new ant[data.MAX_ant-1];
        
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
    //faileに書き込み
    public void write(){
        try{//ファイルの書き込みにはエラー処理がいる。
            int[][] B = new int[grand.state.length][grand.state.length];
            for(int k=0;k<ant.length;k++){
                if(ant[k].State!=0)
                    B[ant[k].Location.y][ant[k].Location.x]=ant[k].State+1;
            }
            FileWriter wr=new FileWriter("file.txt");//Fileとアプリを書き込みでつなぐ
            wr.write(String.valueOf(this.MAX_size)+"\r\n");
            for(int i=0;i<this.MAX_size;i++){
                for(int j=0;j<this.MAX_size;j++){
                    wr.write(String.valueOf(this.grand.ant[i][j]+1)+" ");
                }
                wr.write("\r\n");
                for(int j=0;j<this.MAX_size;j++){
                    wr.write(String.valueOf(this.grand.state[i][j])+" ");
                }
                wr.write("\r\n");
            }
            wr.flush();//flush
            wr.close();//閉じる
        }catch(Exception e){
            //例外処理は、エラーを表示
            System.out.println("Err e="+e);
        }
    }
    //faileから呼び出し
    public void read(){
        String line,line2;
        String[] fruit,fruit2;
        int j=0;
        this.grand = new Grand();
        try{//ファイルをか使うには、例外処理が必要
            FileReader rd=new FileReader("file.txt");//読み取り用として、ファイルとアプリを繋ぐ
            BufferedReader br = new BufferedReader(rd);
            int size = Integer.parseInt(br.readLine());
            this.grand.state = new int[size][size];
            this.grand.ant = new int[size][size];
            while((line = br.readLine()) != null){
                if((line2 = br.readLine()) != null){
                    fruit = line.split(" ", 0);
                    fruit2 = line2.split(" ", 0);
                    for(int i=0;i<fruit.length;i++){

                        this.grand.ant[j][i] = Integer.parseInt(fruit[i]);
                        this.grand.state[j][i] = Integer.parseInt(fruit2[i]);
                    }j++;
                }
            }
            rd.close();//閉じる
        }catch(IOException e){
            //エラーが発生したら　エラーを表示
            System.out.println("Err="+e);
        }
    }
    //呼出し後アリをセット
    void setting() {
        ant = new ant[MAX_ant];
        int ant_size=0;
    //蟻の配置
        for(int j=0;j<this.MAX_size;j++){
            for(int i=0;i<this.MAX_size;i++){
                if(grand.ant[j][i]!=0){
                    ant_size = grand.ant[j][i]-1;
                    ant[ant_size] = new ant();
                    ant[ant_size].set(i,j,ant_size,0);
                    ant[ant_size].Memory = new Memory();
                    ant[ant_size].Memory.state = new int[MAX_kind+1];
                    ant[ant_size].Memory.P = new Point[MAX_kind+1]; 
                    for(int k=0;k<=MAX_kind;k++){
                        ant[ant_size].Memory.P[k] = new Point();
                    }
                    grand.setAnt(i,j,ant_size);
                }
            }
        }
    }
        

}
