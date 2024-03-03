import java.io.*;
import java.util.*;

public class lerCSV {
    void lerArquivoCSV() throws IOException {
        String nomeArquivo = "amz kindle books updated.csv";
        RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "rw");
        RandomAccessFile arqByte = new RandomAccessFile("ARQUIVO.db", "rw");
        
        int ultimoID = 0;
        String str = arq.readLine();
        Livro livro;
        arqByte.seek(0);
        while ((str = arq.readLine()) != null) {
            livro = new Livro();
            livro = lerStringLivro(str, livro, ultimoID);
            ultimoID ++;
            Util.escreverLivro(livro, arqByte);
            //Livro.exibir(livro);
        }
        arq.close();
        arqByte.close();
    }

    Livro lerStringLivro(String str, Livro livro, int ultimoID){
        int indice = 0;
        livro.id = ultimoID + 1;
        indice = lerCodigo(str, livro);
        indice = lerTitulo(str, livro, indice);
        indice = lerAutor(str, livro, indice);
        indice = lerAvaliacao(str, livro, indice);
        indice = lerPreco(str, livro, indice);
        indice = lerKindleUnlimited(str, livro, indice);
        indice = lerData(str, livro, indice);
        lerCategoria(str, livro, indice);
        //Livro.exibir(livro);
        //System.out.println();
        return livro;
    }

    int lerCodigo(String str, Livro livro) {
        int posicao = str.indexOf(',');
        String codigo = str.substring(0, posicao);
        livro.codigo = codigo;
        return posicao;
     }

     int lerTitulo(String str, Livro livro, int i) {
        String titulo = "";
        int posicao = 0;;

        String aux = str.substring(i+1);

        if(aux.charAt(0)=='"'){
            for(int j=1;j<aux.length();j++){
                if(aux.charAt(j)=='"'){
                    posicao = j+i+2;
                    j=aux.length();
                }
                else{
                    titulo += aux.charAt(j);
                }
            }
        }
        else{
            posicao = str.indexOf(',', i + 1);
            titulo = str.substring(i + 1, posicao);
        }
        livro.titulo = titulo;
        return posicao;
    }

    int lerAutor(String str, Livro livro, int i) {
        String autor = "";
        int posicao = 0;
        String aux = str.substring(i + 1, str.length());

        if (aux.contains(String.valueOf('"'))) {
            int posicaoInicial = aux.indexOf('"');
            posicao = aux.indexOf('"', posicaoInicial + 1);
            autor = aux.substring(posicaoInicial + 1, posicao);
            posicao = i + autor.length() + 1;
           // System.out.println("aux = " + autor.length() );
        } 
        else {
            posicao = str.indexOf(',', i + 1);
            autor = str.substring(i + 1, posicao);
        }
        livro.autor = autor;
        return posicao;
    }

    int lerAvaliacao(String str, Livro livro, int i) {
        String aux = str.substring(i);
        String avaliacao = "";
        int posicao = 0;

        if (aux.contains(String.valueOf('"'))) {
            posicao = str.indexOf(',', i + 3);
            avaliacao = str.substring(i + 3, posicao);
        }
        else{
            posicao = str.indexOf(',', i + 1);
            avaliacao = str.substring(i + 1, posicao);
        }

        livro.avaliacao = Float.parseFloat(avaliacao);
        return posicao;
    }

    int lerPreco(String str, Livro livro, int i) {
        int posicao = str.indexOf(',', i + 1);
        String preco = str.substring(i + 1, posicao);
        livro.preco = Float.parseFloat(preco);
        return posicao;
    }

    int lerKindleUnlimited(String str, Livro livro, int i) {
        int posicao = str.indexOf(',', i + 1);
        String kindleUnlimited = str.substring(i + 1, posicao);
        livro.kindleUnlimited = (kindleUnlimited.equals("True")) ? true : false;
        return posicao;
    }

    int lerData(String str, Livro livro, int i) {
        int posicao = str.indexOf(',', i + 1);
        String data = str.substring(i + 1, posicao);

        if (!data.equals("")) livro.data = Util.formatarData(data);
        return posicao;
    }

    void lerCategoria(String str, Livro livro, int i){
        String categorias = str.substring(i + 1, str.length());
        String [] categoria = categorias.split(" & ",-1);

        livro.nomeCategoria = new String[categoria.length];
        for (int j = 0; j < categoria.length; j++) {
            livro.nomeCategoria[j] = categoria[j];
        }
    }
    

  /*
   *    public static void main(String[] args) throws Exception {
        lerCSV l = new lerCSV();
        l.lerArquivoCSV();
    } 
   */

}