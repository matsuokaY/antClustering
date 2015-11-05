
package antclustering;

import static antclustering.Field.ANT;
import java.util.Random;


class value{
    ant[] ant;
    Grand grand;
}

public class antOperation {
    static int pickCount = 3;

    //  乱数によって動かす方向を決定するための配列
    static final int[] movex = {-1, 0, 1,-1, 1,-1, 0, 1};
    static final int[] movey = {-1,-1,-1, 0, 0, 1, 1, 1};
    static final int[] moveX = {-2,-1, 0, 1, 2,-2,-1, 0, 1, 2,-2,-1, 1, 2,-2,-1, 0, 1, 2,-2,-1, 0, 1, 2};
    static final int[] moveY = {-2,-2,-2,-2,-2,-1,-1,-1,-1,-1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2};
    
    //***周りがpickCount以下なら持ち上げる***//
    public static boolean pickObject(int an,int iteration){
        //三分の一
        if(iteration<Field.ThirdIteration)
            return an<=pickCount-1;
        //三分の二
        else if(iteration<Field.ThirdIteration*2)
            return an<=pickCount;
        //残り
        else
            return an<=pickCount-1;
    }
    //***周りがpickCount以上なら降ろす***//
    public static boolean dropObject(int an,int iteration){
        if(iteration<Field.ThirdIteration)
            return an>=pickCount-1;
        else if(iteration<Field.ThirdIteration*2)
            return an>=pickCount;
        else
            return an>=pickCount-1;
    }

    //***オブジェクトの持ち上げれるか***//
    public static boolean pickCheck(int an,int grand){
        // 蟻がオブジェクトを所持していない　かつ　場に蟻とオブジェクトが存在
        return an==0&&grand>Field.ANT;
    }
    //***オブジェクトを降ろせるか***//
    public static boolean dropCheck(int an,int grand){
        //　蟻がオブジェクトを所持している　かつ　場にオブジェクトが存在しない
        return an!=0&&grand==Field.ANT;
    }
    
    public static boolean pick(ant an,ant clone,Grand grand,int i,int x,int y){
        if(!pickCheck(an.State,grand.state[y][x]))
            return false;
        an.around=grand.Around8(an);
        if(pickObject(an.around,i)){
            //蟻がオブジェクトを持ち上げる
            clone.Pick(x,y,grand);
        }else
            clone.time++;
        return true;
    }      
    public static boolean drop(ant an,ant clone,Grand grand,int i,int x,int y){
        if(!dropCheck(an.State,grand.state[y][x]))
            return false;
        an.around=grand.Around8(an);
        if(dropObject(an.around,i)){
            //蟻がオブジェクトを下す
            grand.cloneState[y][x] = clone.Drop();
        }else
            clone.time ++;
        if(Field.limitKeepTime<clone.time&&clone.around>0){
           //蟻がオブジェクトを下す
            grand.cloneState[y][x] = clone.Drop();
        }
        return true;
    }
    
    //**********************************************************************************************//    

}
