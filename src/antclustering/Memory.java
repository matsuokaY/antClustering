package antclustering;


import java.awt.Point;

public class Memory {
    Point[] P;
    int[] state;
    int number;
    
    Point F = new Point(-1,-1);
    void set_memory(Point P,int State) {
        this.P[State] = P;
        this.state[State] = 1;
    }
    boolean serch_memory(int State) {
        if(this.state[State]==1)
            return true;
        else
            return false;
    }
}
