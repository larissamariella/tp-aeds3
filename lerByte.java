import java.io.*;
import java.util.*;

public class lerByte {
    public static void lerArquivoByte(RandomAccessFile arq) throws IOException{
        int ultimoId = arq.readInt();
       
        while (arq.getFilePointer() < arq.length()) {
            Livro livro = lerLivro(arq);
           if(livro!=null){
            Livro.exibir(livro);
           }
        }
    }

    public static Livro lerLivro(RandomAccessFile arq) throws IOException {
        Livro livro = new Livro();
       
        char lapide = arq.readChar();
        int tamanho = arq.readInt();

        if(lapide != '*'){
            byte[] ba = new byte[tamanho];
            arq.read(ba);
            livro = livro.fromByteArray(ba);
        }else{
            livro = null;
        }
        return livro;
    }

}
