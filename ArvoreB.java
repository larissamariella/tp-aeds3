import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.*;

public class ArvoreB {
    private Pagina raiz;
    private int t;
    private RandomAccessFile file;
    private long posicaoAtual;

    public ArvoreB(int t) {
        this.raiz = null;
        this.t = t;
        try {
            this.file = new RandomAccessFile("arqArvoreB.db", "rw");
            RandomAccessFile arq = new RandomAccessFile("arquivo.db", "rw");
            byte [] arqBytes = new byte[(int) arq.length()/40];
            arq.read(arqBytes);
            file.write(arqBytes);

            this.posicaoAtual = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Pagina {
        List<Livro> chaves;
        List<Pagina> filhos;
        boolean folha;

        public Pagina(boolean folha) {
            this.chaves = new ArrayList<>();
            this.filhos = new ArrayList<>();
            this.folha = folha;
        }
    }

    public void inserir(Livro chave) {
        if (raiz == null) {
            raiz = new Pagina(true);
            raiz.chaves.add(chave);
        } else {
            if (raiz.chaves.size() == (t - 1)) {
                Pagina novaRaiz = new Pagina(false);
                novaRaiz.filhos.add(raiz);
                dividirFilho(novaRaiz, 0);
                raiz = novaRaiz;
            }
            inserirNaoCheio(raiz, chave);
        }
    }

    private void inserirNaoCheio(Pagina no, Livro chave) {
        int i = no.chaves.size() - 1;
        if (no.folha) {
            while (i >= 0 && chave.getID() < no.chaves.get(i).getID()) {
                i--;
            }
            no.chaves.add(i + 1, chave);
        } else {
            while (i >= 0 && chave.getID() < no.chaves.get(i).getID()) {
                i--;
            }
            i++;
            if (no.filhos.get(i).chaves.size() == (t - 1)) {
                dividirFilho(no, i);
                if (chave.getID() > no.chaves.get(i).getID()) {
                    i++;
                }
            }
            inserirNaoCheio(no.filhos.get(i), chave);
        }
    }

    private void dividirFilho(Pagina pai, int indiceFilho) {
        Pagina filho = pai.filhos.get(indiceFilho);
        Pagina novoFilho = new Pagina(filho.folha);

        Livro mediana = filho.chaves.get(t / 2 - 1);

        pai.chaves.add(indiceFilho, mediana);

        pai.filhos.add(indiceFilho + 1, novoFilho);

        novoFilho.chaves.addAll(filho.chaves.subList(t / 2, filho.chaves.size()));
        filho.chaves.subList(t / 2 - 1, filho.chaves.size()).clear();

        if (!filho.folha) {
            novoFilho.filhos.addAll(filho.filhos.subList(t / 2, filho.filhos.size()));
            filho.filhos.subList(t / 2, filho.filhos.size()).clear();
        }
    }

    private void printarPagina(Pagina raiz) {
        for (Livro livro : raiz.chaves) {
            System.out.print(livro.getID() + " ");
        }
        System.out.println();
    }

    // para imprimir os níveis
    public void imprimir() {
        imprimir(raiz, 0);
    }

    private void imprimir(Pagina no, int nivel) {
        if (no != null) {
            System.out.print("Nível " + nivel + ": ");
            for (int i = 0; i < no.chaves.size(); i++) {
                Livro livro = no.chaves.get(i);
                Livro.exibir(livro);
                System.out.println();
            }
            System.out.println();
            nivel++;
            for (int i = 0; i < no.filhos.size(); i++) {
                imprimir(no.filhos.get(i), nivel);
            }
        }
    }

    //para imprimir os elementos da pagina
    public void imprimirPagina() {
        imprimirPagina(raiz, 0);
    }

    private void imprimirPagina(Pagina no, int nivel) {
        if (no != null) {
            System.out.print("Nível " + nivel + ": ");
            printarPagina(no);
            System.out.println();
            nivel++;
            for (int i = 0; i < no.filhos.size(); i++) {
                imprimirPagina(no.filhos.get(i), nivel);
            }
        }
    }

    public void remover(int chave) {
        if (raiz == null) {
            System.out.println("Árvore vazia. Nenhuma remoção possível.");
            return;
        }
        remover(raiz, chave);
        if (raiz.chaves.isEmpty() && !raiz.filhos.isEmpty()) {
            raiz = raiz.filhos.get(0);
        }
    }

    private void remover(Pagina no, int chave) {
        int index = encontraIndiceChave(no, chave);

        if (index < no.chaves.size() && no.chaves.get(index).getID() == chave) {
            if (no.folha) {
                no.chaves.remove(index);
            } else { 
                Livro predecessor = getPredecessor(no, index);

                if (no.filhos.size() - 1 == no.chaves.size()) {

                    Pagina paginaDireita = no.filhos.get(index + 1);

                    if (predecessor.getID() < paginaDireita.chaves.get(0).getID()) {
                        paginaDireita.chaves.add(0, predecessor);
                    } else {
                        paginaDireita.chaves.add(0, predecessor);
                    }
                    no.filhos.set(index, paginaDireita);
                    no.filhos.indexOf(paginaDireita);
                    no.filhos.remove(paginaDireita);
                } else {
                    no.chaves.set(index, predecessor);
                }

                no.chaves.remove(predecessor);

            }
        } else {
            Pagina filho = null;
            if (no.filhos.size() > 0)
                filho = no.filhos.get(index);

            if (filho != null && filho.chaves.size() >= t / 2 - 1) { 
                remover(filho, chave);
            } else {
                Pagina irmao = null;
                int irmaoIndex = (index == no.chaves.size()) ? index - 1 : index;

                if (irmaoIndex >= 0 && no != null && no.filhos.get(irmaoIndex).chaves.size() >= t) {
                    irmao = no.filhos.get(irmaoIndex);
                } else if (irmaoIndex + 1 < no.filhos.size() && no.filhos.get(irmaoIndex + 1).chaves.size() >= t) {
                    irmao = no.filhos.get(irmaoIndex + 1); 
                }

                if (irmao != null) { 
                    if (irmaoIndex == index) {
                        Livro chaveIrmao = irmao.chaves.remove(0);
                        Livro chavePai = no.chaves.get(irmaoIndex);
                        no.chaves.set(irmaoIndex, chaveIrmao);
                        remover(filho, chave);
                    } else { 
                        Livro chaveIrmao = irmao.chaves.remove(irmao.chaves.size() - 1);
                        Livro chavePai = no.chaves.get(irmaoIndex);
                        no.chaves.set(irmaoIndex, chaveIrmao);
                        remover(filho, chave);
                    }
                } else if (no != null) {
                    if (irmaoIndex == index) {
                        irmao = no.filhos.get(irmaoIndex);
                        irmao.chaves.add(no.chaves.remove(irmaoIndex));
                        irmao.chaves.addAll(filho.chaves);
                        irmao.filhos.addAll(filho.filhos);
                        no.filhos.remove(filho);
                    } else {
                        irmao = no.filhos.get(irmaoIndex + 1);
                        irmao.chaves.add(0, no.chaves.remove(irmaoIndex));
                        irmao.chaves.addAll(0, filho.chaves);
                        irmao.filhos.addAll(0, filho.filhos);
                        no.filhos.remove(filho);
                    }
                    remover(irmao, chave);
                }
            }
        }
    }

    private int encontraIndiceChave(Pagina no, int chave) {
        int index = 0;
        while (index < no.chaves.size() && chave > no.chaves.get(index).getID()) {
            index++;
        }
        return index;
    }

    private Livro getPredecessor(Pagina no, int index) {
        Livro livro = null;
        if (index == 0) {
            Pagina atual = no.filhos.get(index);
            while (!atual.folha) {

                atual = atual.filhos.get(atual.chaves.size() - 1);
            }
            System.out.println(atual.chaves.get(atual.chaves.size() - 1).getID() + "<- id");
            livro = atual.chaves.get(atual.chaves.size() - 1);
            atual.chaves.remove(livro);

        } else if (no.chaves.get(index - 1) != null) {
            livro = no.chaves.get(index - 1);
        }
        return livro;
    }

    public void escreverArquivo() {
        escreverArvore(raiz);
    }

    public void escreverLivro(Livro livro) throws IOException {
     

        byte[] ba = livro.toByteArray();

        this.file.writeChar('-');
        this.file.writeInt((ba.length));
        this.file.write(ba);
    }

    private void escreverArvore(Pagina no) {
        if (no != null) {
            for (Livro livro : no.chaves) {
                try {
                    this.file.seek(posicaoAtual);
                    this.file.writeInt(no.filhos.size());
                    this.file.writeLong(posicaoAtual + 4 + no.filhos.size() * 8);
                    escreverLivro(livro);
                    posicaoAtual = this.file.getFilePointer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (Pagina pagina : no.filhos) {
                escreverArvore(pagina);
            }
        }
    }
    public Livro buscar(int chave) {
        return buscar(raiz, chave);
    }
    
    private Livro buscar(Pagina no, int chave) {
        int index = encontraIndiceChave(no, chave);
    
        if (index < no.chaves.size() && no.chaves.get(index).getID() == chave) {
            return no.chaves.get(index);
        }
    
        if (no.folha) {
            return null;
        }
    
        return buscar(no.filhos.get(index), chave);
    }
    
    public void fecharArquivo() {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void preencherArvore(RandomAccessFile arq, ArvoreB arvoreB) throws Exception{
        arq.seek(0);
        arq.readInt();
        Livro livro = new Livro();
        while (arq.getFilePointer() < arq.length()) {
            char lapide = arq.readChar();
            int tamanho = arq.readInt();
            
            if (lapide != '*') { // Checa se livro não foi removido ou atualizado por um registro maior
                byte[] ba = new byte[tamanho];
                arq.read(ba);
                livro = new Livro(ba);
                //System.err.println("----------- Inserindo id " + livro.getID() + " ---------------");
                arvoreB.inserir(livro);
            } else { // Pula o registro do livro caso esteja marcado com a lápide
                arq.seek(arq.getFilePointer() + tamanho);
            }
        }
    }
}
