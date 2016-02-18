package antclustering;


import java.awt.Point;

public class Memory {
    Point[] P;
    int[] state;
    int number;
    boolean all;
    
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
    void all_serch(int kind){
        int count=0;
        for(int i=1;i<=kind;i++){
            if(this.state[i]==1)
                count++;
        }
        if(count==kind)
            this.all=true;
        else
            this.all=false;
    }
}
