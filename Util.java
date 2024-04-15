import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Util {
        public static long formatarData(String dateString) {
        DateTimeFormatter mascara = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate data = LocalDate.parse(dateString, mascara);
        Instant instant = data.atStartOfDay().toInstant(ZoneOffset.UTC);
        return instant.getEpochSecond();
    }

    // Escreve registro do livro
  
    public static void escreverLivro(Livro livro, RandomAccessFile arq) throws IOException {
        arq.seek(0);
        arq.writeInt(livro.getID());
        long fim = arq.length();
        arq.seek(fim);

        byte[] ba = livro.toByteArray();

        ListaInvertidaController.inserirTermoNaListaTitulos(livro, fim);
        ListaInvertidaController.inserirTermoNaListaCategoria(livro, fim);

        arq.writeChar('-');
        arq.writeInt((ba.length));
        arq.write(ba);
    }


    public static long posicaoLivro(int id, RandomAccessFile arq) throws IOException{
        arq.readInt();
        Livro livro = new Livro();
        boolean achou = false;
        long posicaoLivro = arq.getFilePointer();

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
                }
            } else {
                arq.seek(arq.getFilePointer() + tamanho);
            }
        }
        if (achou == false) livro = null;
        return posicaoLivro;
    }

    // tp2

    public static boolean palavrasGenericas(String termo){
        String termoLowerCaseString = termo.toLowerCase();
        String[] palavrasGenericas = {"and", "is", "or", "the", "it", "that", "to", "of", "the", "by", "for", "in","a", "at", "with","on", "from","as","for","since","than"};
        boolean resp = false;

        for (int i = 0; i < palavrasGenericas.length; i++) {
            if(palavrasGenericas[i].equals(termoLowerCaseString)){
                resp = true;
                i = palavrasGenericas.length;
            }
        }
        return resp;
    }

    public static String formatarTermo(String termo) {
        String[] format = {"(", ")", ":", "'", ",", "."};

        // Aplica todas as substituições no mesmo String original
        for (int i = 0; i < format.length; i++) {
            termo = termo.replace(format[i], "");
        }

        return termo.toLowerCase();
    }
}
