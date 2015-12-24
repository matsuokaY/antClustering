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
        Field field = new Field();
        normal field2 = new normal();
//        local_initial_parameters(field);
        local_initial(field2);
        System.out.println("\n終了");
    }
    //置いた場所を記憶させそこに進むようにする。
    //置いた場所からある程度距離が離れている場合は置かないようにする。
    public static void local_initial_parameters(Field field){     
        field.set(size,object,kind,100,iteration,limittime);
        field.Fieldset();
        System.out.println("配置完了");
//        grand.Check();
        field.NotAntCheck();
        System.out.println("\nクラスタリング開始");
        field.Clustering();
        System.out.println("クラスタリング終了");
        System.out.println("\n/****************************************************************************/");
        field.Check();
    }
    //クリティカルパスを見つける。
    public static void local_initial(normal field){     
        field.set(size,object,kind,100,iteration,limittime);
        field.Fieldset();
        System.out.println("配置完了");
//        grand.Check();
        field.NotAntCheck();
        System.out.println("\nクラスタリング開始");
        field.Clustering();
        System.out.println("クラスタリング終了");
        System.out.println("\n/****************************************************************************/");
        field.Check();
    }
}
