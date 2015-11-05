
package antclustering;

import static antclustering.Field.ANT;
import java.awt.Point;

public class ant {
    //蟻のナンバー
    public int No;
    //蟻の位置
    public  Point Location;
    //状態
    public  int State;
    //移動速度
    public double v;

    public Point old;
    public int around;
    public int time;
    
    
    //***蟻の配置***//
    public void set(int x,int y,int z,int state){
        this.Location = new Point(x,y);
        this.old = new Point(x,y);
        this.State = state;
        this.No = z;
    }
     
    public void Pick(int x,int y,Grand grand){
        this.State = grand.cloneState[y][x]-Field.ANT;
        grand.cloneState[y][x] = Field.ANT;
    }
    public int Drop(){
        int result = this.State;
        this.State = 0;
        this.time = 0;
        return result+Field.ANT;
    }

}
