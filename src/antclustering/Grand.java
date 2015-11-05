
package antclustering;


public class Grand {
    public int[][] state;
    public int[][][] pheromone;
    public int[][] cloneState;
    
    public int ANT;
    public int MAX;
    
    
    public void setting(int ant,int max){
        this.ANT = ant;
        this.MAX = max;
        
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
    //***周りとの類似度***//
    public double Neight(ant ant,int Range){
        double count=0,objectCount=0,anotherCount=0;
        int X,Y,Xend,Yend,
            x = ant.Location.x,
            y = ant.Location.y;
        int object = ant.State;
        //蟻が保持しているオブジェクトが0(保持なし)ならその場に落ちているオブジェクトで判断する。
        if(object ==0)
            object = state[y][x];
        //蟻が認識する範囲(XからXendまで、YからYendまで)
        X = Math.max(0,x-Range);
        Xend = Math.min(x+Range,MAX);
        Y = Math.max(0,y-Range);
        Yend = Math.min(y+Range,MAX);
        //同一オブジェクト同士の類似
        for(int i =X ;i<Xend;i++)
            for(int j = Y;j<Yend;j++){
                //オブジェクトの一致
                if(state[j][i]>0
                        //&&grand.state[j][i]<ANT
                        ){
                    objectCount++;
                    if(!(i==x&&j==y)&&state[j][i] == object||state[j][i]-ANT == object)
                        count++;
                    
                }                    
            }
        if(count==0)return 0;        
        return count / objectCount;
    }
    //***周囲8マスの物体の数***//
    public int Around8(ant ant){
        int x = ant.Location.x,
            y = ant.Location.y,
            X,Y,Xend,Yend,count=0,
            object = ant.State;
        if(object == 0){
            object = state[y][x];
        }
        
        //蟻が認識する範囲
        X = Math.max(0,x-1);
        Xend = Math.min(x+1,MAX-1);
        Y = Math.max(0,y-1);
        Yend = Math.min(y+1,MAX-1);
        //同一オブジェクト同士の類似
        for(int i =X ;i<=Xend;i++)
            for(int j = Y;j<=Yend;j++){
                //オブジェクトの一致
                if(checkObject(i,j,object))
                    count++;
            }
        return count;
    }
}
