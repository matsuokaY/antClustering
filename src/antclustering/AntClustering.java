package antclustering;

/*******
 * 0：配置なし
 * 1~kind：オブジェクト
 * kind+1：蟻
 * kind+2~kind*2+1：オブジェクトと蟻
 ******/

public class AntClustering {
    static int size = 20;
    static int object = 200;
    static int kind =  3;
    static int ant = 300;
    static int iteration = 1000000;
    static int limittime = 4;
    
    public static void main(String[] args) {
        Field grand = new Field();
        local_initial_parameters(grand);
        System.out.println("\n終了");
    }
    
    public static void local_initial_parameters(Field grand){     
        grand.set(size,object,kind,ant,iteration,limittime);
        grand.Grandset();
        System.out.println("配置完了");
//        grand.Check();
        grand.NotAntCheck();
        System.out.println("\nクラスタリング開始");
        grand.Clustering();
        System.out.println("クラスタリング終了");
        System.out.println("\n/*********************************************/");
        grand.NotAntCheck();
//        System.out.println("\n/*********************************************/");
//        grand.Check();
    }
}
