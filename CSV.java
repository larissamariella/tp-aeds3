import java.io.*;
import java.util.*;

public class CSV {
    void lerArquivoCSV() throws IOException {
        String nomeArquivo = "amz kindle books updated.csv";
        RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "rw");
        RandomAccessFile arqByte = new RandomAccessFile("arquivo.db", "rw");
        
        ArrayList<ListaInvertida> listaInvertidaTitulo = new ArrayList<ListaInvertida>();
        ArvoreB arvoreB = new ArvoreB(4); // Grau mínimo 3
        int ultimoID = 0;
        String str = arq.readLine();
        Livro livro;
        arqByte.seek(0);


        while ((str = arq.readLine()) != null) {
            livro = new Livro();
       //     livro = lerStringLivro(str, livro, ultimoID);
            ultimoID ++;
            
            arvoreB.inserir(livro);
            Util.escreverLivro(livro, arqByte);
            //Livro.exibir(livro);
        }
        arvoreB.escreverArquivo();

    // System.out.println("Árvore B:");
  //  arvoreB.imprimirPagina();
  arvoreB.imprimirPagina();
      System.out.println("----\n -------------------------------\n Excluindo");
       arvoreB.remover(6);
       arvoreB.imprimirPagina();
       Livro l =  arvoreB.buscar(4);
       Livro.exibir(l);
       System.out.println("\n -------------------------------\n Excluindo");

      arvoreB.imprimirPagina();
             //  arvoreB.imprimir();
  
      // arvoreB.imprimir();


        //ListaInvertidaController.escreverArquivoListaTitulos();
        //ListaInvertidaController.escreverArquivoListaCategorias();
        arq.close();
        arqByte.close();
    }

}