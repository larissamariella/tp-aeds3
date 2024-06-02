import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;

public class Arvore {

    No raiz;
    public Arvore(){
        raiz = null;
    }

    public No getFrequenciaMenor(ArrayList<No> lista) {
        if (lista == null || lista.isEmpty()) {
            return null;
        }

        No menorFrequenciaNo = lista.get(0);

        for (No no : lista) {
            if (no.frequencia < menorFrequenciaNo.frequencia) {
                menorFrequenciaNo = no;
            }
        }

        lista.remove(menorFrequenciaNo);
        return menorFrequenciaNo;
    }

    public void inserirHuffman(ArrayList<No> lista) {

        while (lista.size() > 1) {
            No menor1 = getFrequenciaMenor(lista);
            No menor2 = getFrequenciaMenor(lista);
            No aux = new No();
            aux.dir = menor1;
            aux.esq = menor2;
            aux.frequencia = menor1.frequencia + menor2.frequencia;
            lista.add(aux);
        }
        raiz = lista.get(0);
    }

    public void mostrarHuffman(No r) {

        if (r != null) {
           // System.out.println(r.caracter + " - " + r.frequencia);
            mostrarHuffman(r.esq);
            mostrarHuffman(r.dir);
        }
    }

   public void percorrer(No raiz, HashMap<Byte, String> dicionario, String codigo){
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