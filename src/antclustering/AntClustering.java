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
    static int kind =  1;
    static int ant = 800;
    static int iteration = 100000;
    static int limittime = 4;
    
    
    public static void main(String[] args) {
        Field field = new Field();
        local_initial_parameters(field);
        System.out.println("\n終了");
    }
    
    public static void local_initial_parameters(Field field){     
        field.set(size,object,kind,ant,iteration,limittime);
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
