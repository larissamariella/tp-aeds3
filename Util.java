import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Util {
        public static long formatarData(String dateString) {
        DateTimeFormatter mascara = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate data = LocalDate.parse(dateString, mascara);
        Instant instant = data.atStartOfDay().toInstant(ZoneOffset.UTC);
        return instant.getEpochSecond();
    }

    public static void escreverLivro(Livro livro, RandomAccessFile arq) throws IOException {
        arq.seek(0);
        arq.writeInt(livro.getID());
        arq.seek(arq.length());

        byte[] ba = livro.toByteArray();
        arq.writeChar('-');
        arq.writeInt((ba.length));
        arq.write(ba);
    }

    public static long posicaoLivro(int id, RandomAccessFile arq) throws IOException{
        int ultimoID = arq.readInt();
        //System.out.println(ultimoID);
        Livro livro = new Livro();
        boolean achou = false;
        long posicaoLivro = arq.getFilePointer();
        //System.out.println(posicaoLivro);

        while (arq.getFilePointer() < arq.length() && achou == false) {
            posicaoLivro = arq.getFilePointer();
            char lapide = arq.readChar();
            int tamanho = arq.readInt();
           
            if (lapide != '*') {
                byte[] ba = new byte[tamanho];
                arq.read(ba);
                livro = new Livro(ba);
                if (livro.getID() == id){
                    achou = true;
                    //System.out.println(posicaoLivro);
                }
            } else {
                arq.seek(arq.getFilePointer() + tamanho);
            }
        }
        if (achou == false) livro = null;
        return posicaoLivro;
    }
}
