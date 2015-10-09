
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
    public double v;

    public int oldX;
    public int oldY;
    public int around;
    public int time;
    
    //***蟻の配置***//
    public void set(int x,int y,int z,int state){
        this.Location = new Point(x,y);
        this.State = state;
        this.No = z;
    }
     
    public Point Location(){
        return Location;
    }
}
