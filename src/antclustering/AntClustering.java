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
        Data data2 = new Data();
        data.set(size, object, kind, ant, iteration, limittime);
        data.Fieldset();
        data2.clone(data);
        System.out.println(data==data2);
        System.out.println("/****************************************************************************/");
    //    normal.local_initial_parameters(data);
        System.out.println("/****************************************************************************/");
        Field.local_initial(data2);
        System.out.println("\n終了");
    }
    public static void print(Data data){
        for (int[] loop : data.grand.state) {
            for (int j = 0; j<data.grand.state.length; j++) {
                System.out.print(loop[j] + " ");   
            }
            System.out.println("");
        }
        
    }

}
