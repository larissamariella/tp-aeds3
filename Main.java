import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        lerCSV CSV = new lerCSV();

         lerByte Byte = new lerByte();
      
        try{
            CSV.lerArquivoCSV();
            Byte.lerArquivoByte();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}