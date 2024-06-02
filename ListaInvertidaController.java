import java.io.*;
import java.util.*;

public class ListaInvertidaController {
    public static ArrayList<ListaInvertida> listaInvertidaCategorias = new ArrayList<ListaInvertida>();
    public static ArrayList<ListaInvertida> listaInvertidaTitulos = new ArrayList<ListaInvertida>();

    public static void inserirTermoNaListaTitulos(Livro livro, long pos) {

        String str = livro.getTitulo();
        String[] termos = str.split(" ", -1);
        int indexTermo = 0;

        for (int j = 0; j < termos.length; j++) {
            String termoFormatado = Util.formatarTermo(termos[j]);
            boolean termoRepetido = false;
            if (!Util.palavrasGenericas(termoFormatado)) {
                for (ListaInvertida item : listaInvertidaTitulos) {
                    if (termoFormatado.equals(item.getTermo().toLowerCase())) {
                        termoRepetido = true;
                        indexTermo = listaInvertidaTitulos.indexOf(item);
                        break;
                    }
                }
                ListaInvertida tupla = new ListaInvertida();

                if (termoRepetido) {
                    tupla = listaInvertidaTitulos.get(indexTermo);
                    tupla.setPosicao(pos);
                    listaInvertidaTitulos.set(indexTermo, tupla);
                } else {
                    tupla = new ListaInvertida(termoFormatado, pos);

                    listaInvertidaTitulos.add(tupla);
                }
            }
        }
    }

    public static void inserirTermoNaListaCategoria(Livro livro, long pos) {
        String[] termos = livro.getNomeCategoria();
        int indexTermo = 0;

        for (int j = 0; j < termos.length; j++) {
            String termoFormatado = Util.formatarTermo(termos[j]);
            boolean termoRepetido = false;
            if (!Util.palavrasGenericas(termoFormatado)) {
                for (ListaInvertida item : listaInvertidaCategorias) {
                    if (termoFormatado.equals(item.getTermo().toLowerCase())) {
                        termoRepetido = true;
                        indexTermo = listaInvertidaCategorias.indexOf(item);
                        break;
                    }
                }
                ListaInvertida tupla = new ListaInvertida();

                if (termoRepetido) {
                    tupla = listaInvertidaCategorias.get(indexTermo);
                    tupla.setPosicao(pos);
                    listaInvertidaCategorias.set(indexTermo, tupla);
                } else {
                    tupla = new ListaInvertida(termoFormatado, pos);

                    listaInvertidaCategorias.add(tupla);
                }
            }
        }
    }

    public static void escreverArquivoListaTitulos() throws IOException {
        RandomAccessFile arq = new RandomAccessFile("tituloLivro.db", "rw");

        arq.seek(0);
        for (ListaInvertida tup : listaInvertidaTitulos) {
            arq.writeUTF(tup.getTermo());
            arq.writeInt(tup.getPosicao().size());

            for (long posicao : tup.getPosicao()) {
                arq.writeLong(posicao);
            }
        }
        arq.close();
    }

    public static void escreverArquivoListaCategorias() throws IOException {
        RandomAccessFile arq = new RandomAccessFile("categoriasLivro.db", "rw");
        arq.seek(0);
        for (ListaInvertida tup : listaInvertidaCategorias) {
            arq.writeUTF(tup.getTermo());
            arq.writeInt(tup.getPosicao().size());

            for (long posicao : tup.getPosicao()) {
                arq.writeLong(posicao);
            }
        }
        arq.close();
    }

    public static void buscarTermo(String termo, String type) throws IOException {
        RandomAccessFile arq;
        if (type.equals("titulo")) {
            arq = new RandomAccessFile("tituloLivro.db", "rw");
        } else {
            arq = new RandomAccessFile("categoriasLivro.db", "rw");
        }
        buscarTermo(termo, arq);
        arq.close();
    }

    public static void buscarTermo(String termo, RandomAccessFile arq) throws IOException {
        boolean encontrado = false;
        arq.seek(0);
        termo = termo.toLowerCase();

        while (arq.getFilePointer() < arq.length() && !encontrado) {

            String str = arq.readUTF();
            int qntTermos = arq.readInt();

            if (str.toLowerCase().equals(termo)) {
                encontrado = true;

                for (int i = 0; i < qntTermos; i++) {
                    long posicao = arq.readLong();
                    Livro livro = buscaListaInvertida(posicao);
                    Livro.exibir(livro);
                }
            } else {
                for (int i = 0; i < qntTermos; i++) {
                    arq.readLong();
                }
            }
        }
    }

    public static Livro buscaListaInvertida(long posicao) throws IOException {
        RandomAccessFile arq = new RandomAccessFile("arquivo.db", "rw");
        arq.seek(posicao);
        char lapide = arq.readChar();
        int tamanho = arq.readInt();

        Livro livro = new Livro();
        if (lapide != '*') {
            byte[] ba = new byte[tamanho];
            arq.read(ba);
            livro = livro.fromByteArray(ba);
        } else {
            livro = null;
        }
        arq.close();
        return livro;
    }



    public static void atualizar(long pos1, long pos2) throws IOException {
        RandomAccessFile arqTitulo = new RandomAccessFile("tituloLivro.db", "rw");
        atualizar(pos1, pos2, arqTitulo);
        RandomAccessFile arqCat = new RandomAccessFile("categoriasLivro.db", "rw");
        atualizar(pos1, pos2, arqCat);
    }

    private static void atualizar(long pos1, long pos2, RandomAccessFile arq) throws IOException {
        arq.seek(0);
        while (arq.getFilePointer() < arq.length()) {

            String str = arq.readUTF();
            int qntTermos = arq.readInt();

            for (int i = 0; i < qntTermos; i++) {
                long posAtual = arq.getFilePointer();
                long posicao = arq.readLong();
                if(pos1==posicao){
                    arq.seek(posAtual);
                    arq.writeLong(pos2);
                }
            }
        }
        arq.close();
    }

      public static void excluirTermo(String termo, RandomAccessFile arq) throws IOException {
        boolean encontrado = false;
        arq.seek(0);
        termo = termo.toLowerCase();

        while (arq.getFilePointer() < arq.length() && !encontrado) {
            
            long pos = arq.getFilePointer();
            String str = arq.readUTF();
            int qntTermos = arq.readInt();

            if (str.toLowerCase().equals(termo)) {
                encontrado = true;

                for (int i = 0; i < qntTermos; i++) {
                    arq.writeLong(-1);
                }
               arq.seek(pos);
               arq.writeUTF("*");
            } else {
                for (int i = 0; i < qntTermos; i++) {
                    arq.readLong();
                }
            }
        }
    }

}
