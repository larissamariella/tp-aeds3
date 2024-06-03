import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;

public class ArvoreHuffman {
    NoHuffman raiz;
    public ArvoreHuffman(){
        raiz = null;
    }

    // metodo que busca qual nó de menor frequencia
    public NoHuffman getFrequenciaMenor(ArrayList<NoHuffman> lista) {
        if (lista == null || lista.isEmpty()) {
            return null;
        }

        NoHuffman menorFrequenciaNoHuffman = lista.get(0);

        for (NoHuffman no : lista) {
            if (no.frequencia < menorFrequenciaNoHuffman.frequencia) {
                menorFrequenciaNoHuffman = no;
            }
        }

        lista.remove(menorFrequenciaNoHuffman);
        return menorFrequenciaNoHuffman;
    }

    // metodo que insere os nós na arvore de huffman, pela lista de nós
    public void inserirHuffman(ArrayList<NoHuffman> lista) {

        while (lista.size() > 1) {
            NoHuffman menor1 = getFrequenciaMenor(lista);
            NoHuffman menor2 = getFrequenciaMenor(lista);
            NoHuffman aux = new NoHuffman();
            aux.dir = menor1;
            aux.esq = menor2;
            aux.frequencia = menor1.frequencia + menor2.frequencia;
            lista.add(aux);
        }
        raiz = lista.get(0);
    }

    public void mostrarHuffman(NoHuffman r) {
        if (r != null) {
           System.out.println(r.caracter + " - " + r.frequencia);
            mostrarHuffman(r.esq);
            mostrarHuffman(r.dir);
        }
    }

    // metodo que percorre a arvore de huffman e monta o dicionario
   public void percorrer(NoHuffman raiz, HashMap<Byte, String> dicionario, String codigo){
       if(raiz != null){
           if(raiz.esq == null && raiz.dir == null){
               dicionario.put(raiz.caracter, codigo);
           }else{
               percorrer(raiz.esq, dicionario, codigo + "0");
               percorrer(raiz.dir, dicionario, codigo + "1");
           }
       }
   }
}