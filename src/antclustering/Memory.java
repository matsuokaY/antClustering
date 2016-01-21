package antclustering;


import java.awt.Point;

public class Memory {
    Point[] P;
    int[] state;
    int number;
    void set_memory(Point P,int State) {
        for(int i=0;i<=this.number;i++)
            if(this.state[i]==State){
                this.P[i]=P;
                return ;
            }
        if(this.number == this.state.length)
            this.number = 0;
        this.P[this.number] = P;
        this.state[this.number]=State;
        this.number++;
    }
    int serch_memory(int State) {
        int g=0;
        for(int i=0;i<state.length;i++){
            g=this.number+i;
            if(g<state.length&&state[i]==State)
                return i;
            else if(g>=state.length&&state[g-state.length]==State)
                return g-state.length;
        }
        return -1;
    }
}
