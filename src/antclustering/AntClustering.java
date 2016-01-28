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
    static int iteration = 5000000;
//    static int iteration = 1000;
    static int limittime = 4;
    
    
    public static void main(String[] args) {
        Data data= new Data();
        Data data2 = new Data();
        Data data3 = new Data();
        data.set(size, object, kind, ant, iteration, limittime);
        data.Fieldset();
        data2.clone(data);
        data3.clone(data);
        System.out.println("/****************************************************************************/");
    //    normal.local_initial_parameters(data2);        
        System.out.println("/****************************************************************************/");        
        Field.local_initial(data3);
        System.out.println("\n終了");
    }
    public static void print(Data data){
        for (int[] loop : data.grand.state) {
            for (int j = 0; j<data.grand.state.length; j++) {
                System.out.print(loop[j] + " ");   
            }
            System.out.println("");
        }
/*        int[][] A = new int[data.grand.state.length][data.grand.state.length];
        for(int k =0 ;k<data.ant.length;k++){
            A[data.ant[k].Location.y][data.ant[k].Location.x]++;
        }
        for(int y=0;y<data.grand.state.length;y++){
            for(int x=0;x<data.grand.state.length;x++)
                System.out.print(data.grand.ant[y][x]+" ");
            System.out.print("        ");
            for(int x=0;x<data.grand.state.length;x++)
                System.out.print(A[y][x]+" ");
            System.out.println("");
        }*/
        
    }

}
