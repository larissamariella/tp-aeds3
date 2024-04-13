import java.io.RandomAccessFile;
import java.util.*;

public class BookBTree {
    private int ordem;
    int minimo; // Define quantidade minima de chaves na página
    static Scanner scan = new Scanner(System.in);

    public class parChaveLivro{ 
        int chave;
        Livro livro;

        public parChaveLivro(int chave, Livro livro){
            this.chave = chave;
            this.livro = livro;
        }
        public int getChave(){
            return this.chave;
        }
        public void setChave(int chave){
            this.chave = chave;
        }

        public Livro getLivro(){
            return this.livro;
        }
        public void setLivro(){
            this.livro = livro;
        }
    }

    public class Node {
        int n;
        parChaveLivro par[] = new parChaveLivro[ordem-1];
        Node filhos[] = new Node[ordem];
        boolean folha = true;

        
        public int index(int chave){
            for (int i = 0; i < n; i++){
                if (par[i].getChave() == chave){
                    return i;
                }
            }
            return -1;
        }

        public boolean full(){
            if (n == ordem-1) {return true;} else {return false;}
        }

        public int nFilhos(){
            int i = 0;
            for (; i < filhos.length; i++){
                if (this.filhos[i] == null){
                    return i;
                }
            }
            return i;
        }
        public void printChaves(){
            for (int i = 0; i < this.n; i++){
                System.out.print(par[i].chave);
            }
            System.out.println();
        }
        public void printFilhos(){
            for (int i = 0; i < filhos.length; i++){
                System.out.print(filhos[i]);
            }
            System.out.println();
        }
    }

    public BookBTree(int O) {
        ordem = O;
        minimo = O/2;
        raiz = new Node();
        raiz.n = 0;
        raiz.folha = true;
    }

    public Node raiz;
    
/*     public void escreverArvore(RandomAccessFile arq, Node r){
        arq.writeInt(r.n);
        for (int i = 0; i< ordem-1; i++){
            arq.writeLong(r.filhos[i]);
            arq.writeInt(r.par[i].chave);
            arq.writeLong(r.par[i].livro);
        }
        arq.writeLong(r.filhos[ordem]);
    } */

    public Node buscar(Node x, int chave){
        int i = 0;
        if (x == null) {
            return x;
        }  
        // Percorre chaves até achar a procurada ou para quando acha uma maior
        for (i = 0; i < x.n; i++){ 
            if (chave < x.par[i].chave){
                break;
            }
            if (chave == x.par[i].chave){
                return x;
            }
        }

        if (x.folha){
            return null;
        } else {
            return buscar(x.filhos[i], chave);
        }
    }

    private Livro buscarLivro(Node x, int chave){
        int i = 0;
        if (x == null) {
            return null;
        }  
        // Percorre chaves até achar a procurada ou para quando acha uma maior
        for (i = 0; i < x.n; i++){ 
            if (chave < x.par[i].chave){
                break;
            }
            if (chave == x.par[i].chave){
                return x.par[i].livro;
            }
        }

        if (x.folha){
            return null;
        } else {
            return buscarLivro(x.filhos[i], chave);
        }
    }

    private void dividirFilho(Node pai, int pos, Node filho1){
        Node filho2 = new Node();
        filho2.folha = filho1.folha; // Se o filho cheio é folha, o novo também será
        filho2.n = minimo-1; // Define quantidade de chaves no novo filho como o mínimo

        for (int m = 0; m < minimo - 1; m++){ // Novo filho recebe chaves superiores e respectivos livros
            filho2.par[m] = filho1.par[minimo+m];
        }
        if (!filho1.folha) {
            for (int m = 0; m < minimo; m++){
                filho2.filhos[m] = filho1.filhos[minimo + m];
            }
        }
        filho1.n = minimo-1;

        System.out.print("chaves do filho1 = ");
        filho1.printChaves();
        System.out.print("chaves do filho2 = ");
        filho2.printChaves();
        System.out.print("chaves pai = ");
        pai.printChaves();
        adicionarFilho(pai, pos, filho2);

        adicionarChave(pai, pos, filho1, minimo);
    }

    // Adiciona novo filho ao pai, logo após ao filho cheio  
    private void adicionarFilho(Node pai, int posFilhoCheio, Node novoFilho) {
        for (int m = pai.n; m >= posFilhoCheio+1; m--){
            pai.filhos[m+1] = pai.filhos[m];
        }
        pai.filhos[posFilhoCheio+1] = novoFilho;
    }

    // Adiciona chave do meio do filho cheio na devida posição no nodo pai
    private void adicionarChave(Node pai, int posFilhoCheio, Node filhoCheio, int minimo) {
        System.out.print("Chaves do filhocheio antes ");
        filhoCheio.printChaves();
        System.out.println("adicionando chave do filho cheio " + posFilhoCheio + " com minimo " + minimo);
        for (int m = pai.n - 1; m >= posFilhoCheio; m--){
            pai.par[m+1].chave = pai.par[m].chave; 
        }
        System.out.println("Chaves do pai antes ");
        pai.printChaves();
        System.out.print("Adicionando chave ");
        System.out.println(filhoCheio.par[minimo-1].chave);
        pai.par[posFilhoCheio].chave = filhoCheio.par[minimo-1].chave;
        pai.n = pai.n+1;
        System.out.println("Chaves do pai depois ");
        pai.printChaves();
    }

    private void adicionarChaveEmFolha(Node atual, int chave) {
        int i = 0;
        System.out.println("inserindo " + chave + " em nodo folha");
        atual.printChaves();
        for (i = atual.n -1; i >= 0 && chave < atual.par[i].chave; i--){
            atual.par[i+1].chave = atual.par[i].chave;
        }
        parChaveLivro novo = new parChaveLivro(chave, null);
        atual.par[i+1] = novo;
        atual.n = atual.n +1;;
    }
    public void inserir(int chave){
        Node root = raiz;
        System.out.print("filhos raiz antes ");
        raiz.printFilhos();
        if (root.full()) { // Raiz já está cheia
            Node novaRaiz = new Node();
            raiz = novaRaiz;
            novaRaiz.n = 0;
            novaRaiz.folha = false;
            novaRaiz.filhos[0] = root;
            
            dividirFilho(novaRaiz, 0, root);
            inserirEm(novaRaiz, chave);
            System.out.print("filhos raiz depois ");
            raiz.printFilhos();
        } else{
            inserirEm(root, chave);
        }

        
    }


    private void inserirEm(Node atual, int chave) {
        if (atual.folha) {
            adicionarChaveEmFolha(atual, chave);
        } else {
            int i = 0;
            for (i = atual.n - 1; i >= 0 && chave < atual.par[i].chave; i--){}
            i++;
            Node aux = atual.filhos[i];
            if (aux.full()){
                dividirFilho(atual, i, aux);
                if (chave > atual.par[i].chave){
                    i++;
                }
            }
            inserirEm(atual.filhos[i], chave);
        }
    }

    public void remover(int chave){
        Node x = buscar(raiz, chave);
        if (x == null){
            return;
        }
        removerDe(x, chave);
    }

    private void removerEmFolha(Node x, int pos, int chave){
        int i = pos;
        for (;i < x.n; i++){
            if (i < ordem -2){
                x.par[i].chave = x.par[i+1].chave;
            }
        }
        x.n--;
    }

    private void removerPredecessor(Node pred, Node x, int pos, int chave){
        int chavePred = 0;
        for(;;){
            if (pred.folha){
                System.out.println(pred.n);
                chavePred = pred.par[pred.n -1].chave;
                break;
            } else {
                pred = pred.filhos[pred.n];
            }
        }
        removerDe(pred, chavePred);
        x.par[pos].chave = chavePred;
    }

    private void removerPosterior(Node post, Node x, int pos, int chave){
        int chavePost = post.par[0].chave;
        if (!post.folha) {
        post = post.filhos[0];
        for (;;) {
            if (post.folha) {
            chavePost = post.par[post.n - 1].chave;
            break;
            } else {
            post = post.filhos[post.n];
            }
        }
        }
        removerDe(post, chavePost);
        x.par[pos].chave = chavePost;
    }

    private void removerDe(Node x, int chave) {
        int pos = x.index(chave); // Busca posição da chave
        if (pos != -1) { // Se achou a chave
            // Caso 1: Chave na folha e pode retirar//
            if (x.folha) {
                removerEmFolha(x, pos, chave);
                return;
            }
            if (!x.folha){
                Node pred = x.filhos[pos];
                if (pred.n >= minimo+1) { // Se puder tirar 1
                    removerPredecessor(pred, x, pos, chave);
                    return;
                }
                
                Node post = x.filhos[pos + 1];
                if (post.n >= minimo+1) {
                    removerPosterior(post, x, pos, chave);
                    return;
                }
                // -------------------------------------------------------------------------------- //

                int temp = pred.n + 1;
                pred.par[pred.n++] = x.par[pos];
                for (int i = 0, j = pred.n; i < post.n; i++) {
                    pred.par[j++] = post.par[i];
                    pred.n++;
                }
                for (int i = 0; i < post.n + 1; i++) {
                    pred.filhos[temp++] = post.filhos[i];
                }
        
                x.filhos[pos] = pred;
                for (int i = pos; i < x.n; i++) {
                    if (i != ordem-1) {
                    x.par[i] = x.par[i + 1];
                    }
                }
                for (int i = pos + 1; i < x.n + 1; i++) {
                    if (i != ordem) {
                    x.filhos[i] = x.filhos[i + 1];
                    }
                }
                x.n--;
                if (x.n == 0) {
                    if (x == raiz) {
                    raiz = x.filhos[0];
                    }
                    x = x.filhos[0];
                }
                removerDe(pred, chave);
                return;
                }
            } else {
                Node tmp = x.filhos[pos];
                if (tmp.n >= ordem-1) {
                removerDe(tmp, chave);
                return;
                }
                if (true) {
                Node nb = null;
                int devider = -1;
        
                if (pos != x.n && x.filhos[pos + 1].n >= ordem-1) {
                    devider = x.par[pos].chave;
                    nb = x.filhos[pos + 1];
                    x.par[pos].chave = nb.par[0].chave;
                    tmp.par[tmp.n++].chave = devider;
                    tmp.filhos[tmp.n] = nb.filhos[0];
                    for (int i = 1; i < nb.n; i++) {
                    nb.par[i - 1].chave = nb.par[i].chave;
                    }
                    for (int i = 1; i <= nb.n; i++) {
                    nb.filhos[i - 1] = nb.filhos[i];
                    }
                    nb.n--;
                    removerDe(tmp, chave);
                    return;
                } else if (pos != 0 && x.filhos[pos - 1].n >= ordem-1) {
        
                    devider = x.par[pos - 1].chave;
                    nb = x.filhos[pos - 1];
                    x.par[pos - 1].chave = nb.par[nb.n - 1].chave;
                    Node filhos = nb.filhos[nb.n];
                    nb.n--;
        
                    for (int i = tmp.n; i > 0; i--) {
                    tmp.par[i].chave = tmp.par[i - 1].chave;
                    }
                    tmp.par[0].chave = devider;
                    for (int i = tmp.n + 1; i > 0; i--) {
                    tmp.filhos[i] = tmp.filhos[i - 1];
                    }
                    tmp.filhos[0] = filhos;
                    tmp.n++;
                    removerDe(tmp, chave);
                    return;
                } else {
                    Node lt = null;
                    Node rt = null;
                    boolean last = false;
                    if (pos != x.n) {
                    devider = x.par[pos].chave;
                    lt = x.filhos[pos];
                    rt = x.filhos[pos + 1];
                    } else {
                    devider = x.par[pos - 1].chave;
                    rt = x.filhos[pos];
                    lt = x.filhos[pos - 1];
                    last = true;
                    pos--;
                    }
                    for (int i = pos; i < x.n - 1; i++) {
                    x.par[i].chave = x.par[i + 1].chave;
                    }
                    for (int i = pos + 1; i < x.n; i++) {
                    x.filhos[i] = x.filhos[i + 1];
                    }
                    x.n--;
                    lt.par[lt.n++].chave = devider;
        
                    for (int i = 0, j = lt.n; i < rt.n + 1; i++, j++) {
                    if (i < rt.n) {
                        lt.par[j].chave = rt.par[i].chave;
                    }
                    lt.filhos[j] = rt.filhos[i];
                    }
                    lt.n += rt.n;
                    if (x.n == 0) {
                    if (x == raiz) {
                        raiz = x.filhos[0];
                    }
                    x = x.filhos[0];
                    }
                    removerDe(lt, chave);
                    return;
                }
            }
        }
    }

    public void printTree(Node x, int nivel){
        if (x == null){
            return;
        }
        System.out.println("Level" + nivel + ": ");
        for (int i = 0 ; i < x.n; i ++){
            System.out.print(x.par[i].chave + " ");
        }
        System.out.println();
        /* System.out.print("N = ");
        System.out.print(x.n);
        System.out.print("filhos ");
        x.printFilhos(); */
        nivel++;
        if (!x.folha){
            for (int i = 0; i < x.nFilhos(); i++){
                printTree(x.filhos[i], nivel);
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        BTree b = new BTree(4);
        b.inserir(8);
        b.printTree(b.raiz, 0);
        b.inserir(9);
        b.printTree(b.raiz, 0);
        b.inserir(10);
        b.printTree(b.raiz, 0);
        b.inserir(11);
        b.printTree(b.raiz, 0);
        b.inserir(15);
        b.printTree(b.raiz, 0);
        b.inserir(20);
        b.printTree(b.raiz, 0);
        b.inserir(17);
        b.printTree(b.raiz, 0);
        System.out.println();
        //b.printNode(b.root, 0);
        //b.Show();
        System.out.println();

        b.remover(10);
        //b.printNode(b.root, 0);
        System.out.println();

        int option;
        do {
            System.out.println(" Inicio ");
            System.out.println("Selecione uma opção:\n1. Adicionar um número.\n2. Buscar um número. \n3. Deletar um número.\n4. Sair\n");
            option = scan.nextInt();
            switch (option) {
                case 1:
                    System.out.println("Qual o número que deseja inserir?\n");
                    int valor = scan.nextInt();
                    b.inserir(valor);
                    break;
                case 2:
                    System.out.println("Qual o número que deseja buscar?\n");
                    valor = scan.nextInt();
                    b.buscar(b.raiz, valor);
                case 3:
                    System.out.println("Qual o número que deseja deletar?\n");
                    valor = scan.nextInt();
                    b.remover(valor);
                case 4:
                    break;
                default:
                    System.out.println("Favor inserir um número valido:");
                    System.out.println("\n--- Original B-Tree ---\n");
            }
            b.printTree(b.raiz, 0);
        } while (option != 4);
    }
}
