package antclustering;

import static antclustering.Field.MAX_kind;

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
//    static int iteration = 100000;
    static int limittime = 4;
    
    
    public static void main(String[] args) {
        Data data= new Data();
        Data data2 = new Data();
        Data data3 = new Data();
        Data data4 = new Data();
        data.set(size, object, kind, ant, iteration, limittime);
        data.Fieldset();
        data2.clone(data);
        data3.clone(data);
        data4.clone(data);
        data2.write(data);
        String[] a= new String[24];
        data2.read(a);
   //     print(data);
        System.out.println("/****************************************************************************/");
        long start = System.currentTimeMillis();
//        normal_beta.local_initial_parameters(data2);        
        long end = System.currentTimeMillis();
        System.out.println("nomal ="+(end - start)  + "ms");
        
        System.out.println("/****************************************************************************/");        
        long start2 = System.currentTimeMillis();
 //       Field.local_initial(data3);
        long end2 = System.currentTimeMillis();
        System.out.println("field ="+(end2 - start2)  + "ms");
        System.out.println("/****************************************************************************/");
        long start3 = System.currentTimeMillis();
 //       normal.local_initial_parameters(data4);        
        long end3 = System.currentTimeMillis();
        System.out.println("nomal_beta ="+(end3 - start3)  + "ms");
        
        System.out.println("\n終了");
    }
    public static void print(Data data){ 
        for (int i=0;i<data.grand.state.length;i++) {
            //要素の様子
            for(int j=0;j<data.grand.state.length;j++){
                if(data.grand.state[i][j]!=0)
                    System.out.print(data.grand.state[i][j] + " ");
                else
                    System.out.print(0 + " ");
            }
            System.out.print("        ");
            //蟻の様子
            for(int j=0;j<data.grand.state.length;j++){
                if(data.grand.ant[i][j]!=0&&data.grand.ant[i][j]<10)
                    System.out.print(0+""+data.grand.ant[i][j] + " ");
                else if(data.grand.ant[i][j]!=0)
                    System.out.print(data.grand.ant[i][j] + " ");
                else
                    System.out.print(0+""+0 + " ");
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
