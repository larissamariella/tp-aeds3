import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {
    public static void main(String[] args) {
        lerCSV CSV = new lerCSV();

         lerByte Byte = new lerByte();
      
        try{
            CSV.lerArquivoCSV();
            RandomAccessFile arqByte = new RandomAccessFile("ARQUIVO.db", "rw");
            lerByte.lerArquivoByte(arqByte);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}