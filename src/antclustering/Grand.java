
package antclustering;

import java.awt.Point;


public class Grand {
    //蟻のフェロモン濃度
    public final double p = 1;
    //フェロモンの蒸発率
    public final double lostP = 0.9;
    public static final double p_max = 10;
    
    public int[][] state;
    public double[][][] pheromone;
    public int[][] cloneState;
    public int[][] ant;
   
    public void setState(int x,int y,int kind){
        this.state[y][x] = kind;
    }
    public void resetAnt(ant an,Point P){
        setAnt(an.Location.x,an.Location.y);
        removeAnt(P.x,P.y);
    }
    public void setAnt(int x,int y){
        this.ant[y][x] += 1;
    }
    public void removeAnt(int x,int y){
        this.ant[y][x] -= 1;
    }
    public void setCloneState(){
        this.cloneState = state.clone();
    }
    public void resetState(){
        this.state = cloneState;
    }
    public boolean checkObject(int x,int y,int object){
        return state[y][x] == object;
    }
    //**********************************************************************************************//
    //***フェロモンの散布***//
    public void setPheromone(ant[] ant){
        int state=0;
        for(int i=0;i<ant.length;i++){
            state = ant[i].State;
            if(state==0)
                state = this.state[ant[i].Location.y][ant[i].Location.x];
            if(state!=0&&this.pheromone[state][ant[i].Location.y][ant[i].Location.x]<p_max){
                this.pheromone[state][ant[i].Location.y][ant[i].Location.x] += p;
            }
        }
    }
    //***フェロモンの蒸発***//
    public void lostP(){
        for(int i=0;i<pheromone[0].length;i++){
            for(int j=0;j<pheromone[0][i].length;j++){
                for(int k=0;k<pheromone.length;k++){
                    if(this.pheromone[k][i][j]!=0)
                        this.pheromone[k][i][j] *= lostP;
                }
            }
        }
    }
    //**********************************************************************************************//
    //***周りとの類似度***//
    public double Neight(ant ant,int Range){
        double count=0,objectCount=0;
        int X,Y,Xend,Yend,
            x = ant.Location.x,
            y = ant.Location.y;
        int object = ant.State;
        //蟻が保持しているオブジェクトが0(保持なし)ならその場に落ちているオブジェクトで判断する。
        if(object ==0)
            object = state[y][x];
        //蟻が認識する範囲(XからXendまで、YからYendまで)
        X = Math.max(0,x-Range);
        Xend = Math.min(x+Range,Field.MAX_size);
        Y = Math.max(0,y-Range);
        Yend = Math.min(y+Range,Field.MAX_size);
        //同一オブジェクト同士の類似
        for(int i =X ;i<Xend;i++)
            for(int j = Y;j<Yend;j++){
                //オブジェクトの一致
                if(state[j][i]>0
                        //&&grand.state[j][i]<ANT
                        ){
                    objectCount++;
                    if(!(i==x&&j==y)&&state[j][i] == object)
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
        Xend = Math.min(x+1,Field.MAX_size-1);
        Y = Math.max(0,y-1);
        Yend = Math.min(y+1,Field.MAX_size-1);
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
