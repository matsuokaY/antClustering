
package antclustering;

import java.awt.Point;

//      a[y][x]
public class Grand {
    public int[][] state;
//    public int[][] cloneState;
    public int[][] ant;
    
//    public C[] C;
    public int MAX_size;
    public void set(int size){
        MAX_size = size;
    }
    
    public void setState(int x,int y,int kind){
        this.state[y][x] = kind;
    }
    public void resetAnt(Point an,Point P,int no){
        removeAnt(P.x,P.y);
        setAnt(an.x,an.y,no);

    }
    public void setAnt(int x,int y,int no){
        this.ant[y][x] = no;
    }
    public void removeAnt(int x,int y){
        this.ant[y][x] = 00;
    }

    public boolean checkObject(int x,int y,int object){
        return state[y][x] == object;
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
        Xend = Math.min(x+Range,MAX_size);
        Y = Math.max(0,y-Range);
        Yend = Math.min(y+Range,MAX_size);
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
    //***周囲(半径R)の物体の数***//
    public int AroundR(ant ant,int R){
        int count=0,object = ant.State;
        if(object == 0){
            object = state[ant.Location.y][ant.Location.x];
        }
        for(int y=ant.Location.y-R;y<=ant.Location.y+R;y++)
            for(int x=ant.Location.x-R;x<=ant.Location.x+R;x++){
                if((y>=0&&y<this.state.length)&&(x>=0&&x<this.state.length)&&checkObject(x,y,object))
                    count++;
            }
        return count;
    }
    
    //***蟻の移動***//
    public boolean MovingANT(int x,int y,ant an){
        if((x>=0&&x<state.length)&&(y>=0&&y<state.length)){
            //以前の位置から蟻を削除                   
            //蟻の移動
            Point P  = an.Location;
            an.Move(x, y);
            //移動後の位置に蟻を追加
            resetAnt(an.Location,P,an.No);
            //ループ抜け
            return true;
            }
        return false;
    }
}
