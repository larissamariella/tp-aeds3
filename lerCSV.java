import java.io.*;
import java.util.*;

public class lerCSV {

    void lerArquivoCSV() throws Exception {
        String nomeArquivo = "10booksExample.csv";
        RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "rw");

        int ultimoID = 0;
        String str = arq.readLine();
        Livro livro;

        str = arq.readLine();
        //while ((str = arq.readLine()) != null) {
            livro = new Livro();
           // livro = lerStringLivro(str, livro, ultimoID);
            livro = lerStringLivro(str, livro, ultimoID);
            ultimoID ++;

           escreverLivro(livro);
        //}
        arq.close();
    }
    
    void escreverLivro(Livro livro) throws IOException {
        RandomAccessFile arq = new RandomAccessFile("arquivoByte.db", "rw");
        arq.writeInt(livro.id);
        arq.seek(arq.length());
        
        byte[] ba = livro.toByteArray();
        arq.writeChar('*');
        arq.writeInt((ba.length));
        arq.write(ba);
        arq.close();
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
        Livro.exibir(livro);
        System.out.println();
        return livro;
    }

        Livro lerStringLivro2(String str, Livro livro){
        int indice = 0;
    
        indice = lerCodigo(str, livro);
        System.out.println(livro.codigo);
        indice = lerTitulo(str, livro, indice);
        System.out.println("t2- "+ livro.titulo);
        indice = lerAutor(str, livro, indice);
        System.out.println(livro.autor);
        indice = lerAvaliacao(str, livro, indice);
        System.out.println(livro.avaliacao);
        indice = lerPreco(str, livro, indice);
        System.out.println(livro.preco);
        indice = lerKindleUnlimited(str, livro, indice);
        System.out.println(livro.kindleUnlimited);
        indice = lerData(str, livro, indice);
        lerCategoria(str, livro, indice);
        Livro.exibir(livro);
        System.out.println();

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
        int posicao;

        if (str.contains(String.valueOf('"'))) {
            int posicaoInicial = str.indexOf('"');
            posicao = str.indexOf('"', posicaoInicial + 1);
            titulo = str.substring(posicaoInicial + 1, posicao);
        } 
        else {
            posicao = str.indexOf(',', i + 1);
            titulo = str.substring(i, posicao);
        }
        livro.titulo = titulo;
        return posicao;
    }

    int lerAutor(String str, Livro livro, int i) {
        String autor = "";
        int posicao = 0;
        if(str.contains(String.valueOf('"'))){
            posicao = str.indexOf(',', i + 2);
            autor = str.substring(i + 2, posicao);
        }
        else{
            posicao = str.indexOf(',', i + 1);
            autor = str.substring(i + 1, posicao);
        }

        //System.out.println(autor + " teste\n");
        livro.autor = autor;
        return posicao;
    }

    int lerAvaliacao(String str, Livro livro, int i) {
        int posicao = str.indexOf(',', i + 1);
        String avaliacao = str.substring(i + 1, posicao);
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
        livro.kindleUnlimited = (kindleUnlimited.equals("TRUE")) ? true : false;
        return posicao;
    }

    int lerData(String str, Livro livro, int i) {
        int posicao = str.indexOf(',', i + 1);
        String data = str.substring(i + 1, posicao);

        if (!data.equals("")) livro.data = Util.formatarData(data);
        return posicao;
    }

    void lerCategoria(String str, Livro livro, int i){
        String categorias = str.substring(i + 1, str.length()-1);
        //System.out.println("cat " + categorias);
        String [] categoria = categorias.split(" & ",-1);

        livro.nomeCategoria = new String[categoria.length];
        for (int j = 0; j < categoria.length; j++) {
            livro.nomeCategoria[j] = categoria[j];
        }
    }
    
/*
    public static void main(String[] args) throws Exception {
        lerCSV l = new lerCSV();
        l.lerArquivoCSV();
    } 
*/

}
