import java.io.File;
import java.io.RandomAccessFile;
public class Main {
      public static void main(String[] args) {
        try{
            RandomAccessFile arquivo = new RandomAccessFile("arquivo.db", "rw");
            LerCSV csv = new LerCSV();
            csv.lerArquivoCSV();   
            CRUD.menu();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
}

