
package antclustering;

import java.awt.Point;

public class ant {
    //蟻のナンバー
    public int No;
    //蟻の位置
    public  Point Location;
    //状態
    public  int State;
    //移動速度

    public int time;
    public Memory Memory;
    
    
    //***蟻の配置***//
    public void set(int x,int y,int z,int state){
        this.Location = new Point(x,y);
        this.State = state;
        this.No = z;
    }
     
    public int Drop(){
        int result = this.State;
        this.State = 0;
        this.time = 0;
        return result;
    }
    
    public void Move(int x,int y){
        Point P = new Point(x,y);
        this.Location = P;
    }
}
