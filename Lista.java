import java.io.*;
import java.util.*;

public class Lista {

    public static void main(String[] args) {
        LerCSV l = new LerCSV();
        Livro[] bibLivros = new Livro[3];
        try {

            bibLivros = l.lerArquivoCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<ListaInvertida> listaInvertida = new ArrayList<ListaInvertida>();

        int indice = 1;

        int indexTermo = 0;
        for (int i = 0; i < bibLivros.length; i++) {

            String str = bibLivros[i].getTitulo();
            String[] termos = str.split(" ", -1);

            for (int j = 0; j < termos.length; j++) {
                String termoFormatado = Util.formatarTermo(termos[j])
                boolean termoRepetido = false;
                if (!Util.palavrasGenericas(termoFormatado )) {
                    for (ListaInvertida item : listaInvertida) {
                        if (termos[j].equals(item.getTermo())) {
                            termoRepetido = true;
                            indexTermo = listaInvertida.indexOf(item); // Armazenar a posição da palavra que repete
                            break;
                        }
                    }
                    ListaInvertida tupla = new ListaInvertida();

                    if (termoRepetido) {
                        tupla = listaInvertida.get(indexTermo);
                        tupla.setIdRegistro(bibLivros[i].getID());
                        listaInvertida.set(indexTermo, tupla);
                    } else {
                        // Criação de novo termo
                        tupla = new ListaInvertida(termos[j], bibLivros[i].getID());

                        listaInvertida.add(tupla); // Adicionar tupla na Lista Invertida
                    }
                }
            }

        }

        try {
            RandomAccessFile li = new RandomAccessFile("arqByte.db", "rw");

            // Limpar possíveis dados anteriores do arquivo (casos de reescrita)

            for (ListaInvertida tup : listaInvertida) {

                System.out.println(tup.getTermo() + " - " + tup.getIdRegistro().size());
                li.writeUTF(tup.getTermo());
                // Optamos por adicionar a quantidade de IDs no arquivo de registros, para
                // facilitar a leitura no retorno
                li.writeInt(tup.getIdRegistro().size());

                // Escreve todos os IDs que estão associados a um termo
                for (int id : tup.getIdRegistro()) {
                    li.seek(li.length());
                    li.writeInt(id);
                }
            }

            li.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
