import java.io.*;
import java.util.*;

public class lerByte {
    void lerArquivoByte() throws IOException{
        RandomAccessFile arq = new RandomAccessFile("arquivoByte.db", "rw");

        int ultimoId = arq.readInt();
       
        while (arq.getFilePointer() < arq.length()) {
            Livro livro = lerLivro(arq);
            Livro.exibir(livro);
            System.out.println();
        }
        arq.close();
    }

    Livro lerLivro(RandomAccessFile arq) throws IOException {
        Livro livro = new Livro();
       
        char lapide = arq.readChar();
        int tamanho = arq.readInt();
        byte[] ba = new byte[tamanho];
        arq.read(ba);

        livro.fromByteArray(ba);
        return livro;
    }
   
   /*
    *  public static void main(String[] args) {
        lerByte l = new lerByte();
      
        try{
            l.lerArquivoByte();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    */
}
