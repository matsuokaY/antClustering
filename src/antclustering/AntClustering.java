package antclustering;

public class AntClustering {
    static int size = 20;
    static int object = 200;
    static int kind =  1;
    static int ant = 200;
  static int iteration = 6000;
    static int limittime = 4;
    
    
    public static void main(String[] args) {
        //書き込み
        write();
        //読み込み
        Data data = read();
        Data data2 = read();
        Data data3 = read();
        print(data);
        System.out.println("/****************************************************************************/");
        normal_beta.local_initial_parameters(data);        
        System.out.println("/****************************************************************************/");        
        Field.local_initial(data2);
        System.out.println("/****************************************************************************/");
        normal.local_initial_parameters(data3);        
        normal_ganma.local_initial_parameters(data2);        

        
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
                if(data.grand.ant[i][j]!=0){
                    System.out.print(1+" ");
                }else
                    System.out.print(0+" ");
            }
            System.out.print("        ");
            /*
            for(int j=0;j<data.grand.state.length;j++){
                if(data.grand.ant[i][j]!=0&&data.grand.ant[i][j]<10)
                    System.out.print(0+""+(data.grand.ant[i][j]) + " ");
                else if(data.grand.ant[i][j]!=0)
                    System.out.print(data.grand.ant[i][j] + " ");
                else
                    System.out.print(0+""+0 + " ");
            }*/
            System.out.println("");
        }
    }

    private static void write() {
        Data data= new Data();
        data.set(size, object, kind, ant, iteration, limittime);
        data.Fieldset();
    //    print(data);
        //書き込み
        data.write();
    }
    private static Data read() {
        Data data= new Data();
        data.read();
        data.set(data.grand.ant.length, object, kind, ant, iteration, limittime);
        data.setting();
        return data;
    }
    

}
