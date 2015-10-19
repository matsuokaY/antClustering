
package antclustering;


public class Grand {
    public int[][] state;
    public int[][][] pheromone;
    public int[][] cloneState;
    
    public int ANT;
    
    
    public void setting(int ant){
        this.ANT = ant;
        
    }
    
    public void setState(int x,int y,int kind){
        this.state[y][x] = kind;
    }
    public void setAnt(int x,int y){
        this.state[y][x] += ANT;
    }
    public void removeAnt(int x,int y){
        this.state[y][x] -= ANT;
    }
    public void setCloneState(){
        this.cloneState = state.clone();
    }
    public void resetState(){
        this.state = cloneState;
    }
    public boolean checkObject(int x,int y,int object){
        return state[y][x] == object||state[y][x]-ANT == object;
    }
}
