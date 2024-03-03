import java.io.*;

public class LerByte {
    
    // Faz a leitura completa do livro, char de lápide, int do tamanho e array de bytes
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