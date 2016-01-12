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
    static int ant = 100;
    static int iteration = 1000000;
    static int limittime = 4;
    
    
    public static void main(String[] args) {
        Data data= new Data();
        Data data2=new Data();
        data.set(size, object, kind, ant, iteration, limittime);
        data.Fieldset();
        data2 = data.clone();
        normal.local_initial_parameters(Data.grand,Data.ant);
        System.out.println("/****************************************************************************/");
        Field.local_initial(Data.grand,Data.ant);
        System.out.println("\n終了");
    }

}
