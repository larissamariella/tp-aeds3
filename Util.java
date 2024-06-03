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
  
    public static void escreverLivro(Livro livro, RandomAccessFile arq) throws IOException{
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

        Set<String> palavrasGenericas = new HashSet<>();
        palavrasGenericas.add("and");
        palavrasGenericas.add("is");
        palavrasGenericas.add("or");
        palavrasGenericas.add("the");
        palavrasGenericas.add("it");
        palavrasGenericas.add("that");
        palavrasGenericas.add("to");
        palavrasGenericas.add("of");
        palavrasGenericas.add("by");
        palavrasGenericas.add("for");
        palavrasGenericas.add("in");
        palavrasGenericas.add("a");
        palavrasGenericas.add("at");
        palavrasGenericas.add("with");
        palavrasGenericas.add("on");
        palavrasGenericas.add("from");
        palavrasGenericas.add("as");
        palavrasGenericas.add("since");
        palavrasGenericas.add("than");

        return palavrasGenericas.contains(termo);
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
