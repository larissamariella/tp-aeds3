/* Estrutura do arquivo
 * Diretório é uma lista com p^2 ponteiros, onde p é um p global e os ponteiros apontam para os buckets
 * Bucket é uma lista de par ponteiro chave, onde cada item no bucket[i] é uma chave id do registro e um ponteiro para o registro.
 */
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;



public class HashingEstendido {
    static Scanner scan = new Scanner(System.in);
    int pGlobal;
    int ordem;
    Diretorio dir;

    public HashingEstendido(int P, int Ordem){
        pGlobal = P;
        ordem = Ordem;
        dir = new Diretorio(pGlobal);
    }

    public void aumentarPGlobal(){
        pGlobal++;
    }

    public int getTamanhoDir(){
        double d = Double.valueOf(pGlobal);
        d = Math.pow(2, d);
        int aux = (int) d;
        return aux;
    }

    public int getIndexAux(){ // Retorna o index do bucket auxiliar que irá dividir os item do bucket cheio
        double d = Double.valueOf(pGlobal-1);
        d = Math.pow(2, d);
        int aux = (int) d;
        return aux;
    }

    public class Diretorio{
        List<Bucket> bucketList;

        public Diretorio(int p){
            bucketList = new ArrayList<Bucket>(getTamanhoDir());
            for (int i = 0; i < getTamanhoDir(); i++){
                Bucket b = new Bucket();
                bucketList.add(b);
            }
        }
    }

    public class ParChavePonteiro{
        int chave;
        Livro livro;

        public int getChave(){
            return this.chave;
        }
        public void setChave(int key){
            chave = key;
        }
        public Livro getLivro(){
            return this.livro;
        }
        public void setLivro(Livro book){
            livro = book;
        }

        public ParChavePonteiro(int key, Livro book){
            setChave(key);
            setLivro(book);
        }
    }


    public class Bucket{
        List<ParChavePonteiro> listaChavePonteiro;
        int pLocal;
        
        public Bucket(){
            listaChavePonteiro = new ArrayList<ParChavePonteiro>(ordem);
            pLocal = pGlobal;
        }

        public Bucket(int p){
            listaChavePonteiro = new ArrayList<ParChavePonteiro>(ordem);
            pLocal = p;
        }

        public boolean full(){
            // Percorre a lista de buckets. Se achar algum vazio retorna falso, caso contrário retorna true
            /* System.out.print("full() --> ");
            System.out.println(listaChavePonteiro); */
            if (listaChavePonteiro.size() == ordem){
                return true;
            }
            return false;
        }

        public void aumentarPLocal(){
            pLocal++;
        }
    }

    public int getLSB(int number, int nLSB){
        double d = Double.valueOf(nLSB-1);
        d = Math.pow(2, d);
        int aux = (int) d;
        return number & aux;
    }

    public void dobrarDiretorio(){
        this.aumentarPGlobal();
        Diretorio novoDir = new Diretorio(pGlobal);
        for (int i = 0; i < this.getTamanhoDir(); i++){
            novoDir.bucketList.set(i, dir.bucketList.get(i & ((getTamanhoDir()/2)-1)));
        }
        dir = novoDir;
        System.err.println("Buckets após dobrar diretório");
        showBucketContent();
    }

    public void tratarBucketCheio(int index, int chave, Livro livro){
        Bucket bucket = dir.bucketList.get(index);
        Bucket aux = new Bucket(bucket.pLocal);
        dir.bucketList.set(index + getIndexAux(), aux); // Zera o bucket auxiliar para dividir chaves com bucketCheio

        for (int i=0; i< bucket.listaChavePonteiro.size();i++){
            int antigaPosição = index;
            int novaPosição = bucket.listaChavePonteiro.get(i).chave % getTamanhoDir(); // Nova posição para chave
            /* System.out.println("antigaposição = " + antigaPosição);
            System.out.println("chave " + bucket.listaChavePonteiro.get(i).chave + " mod " +getTamanhoDir()+ " = " + novaPosição); */
            ParChavePonteiro p = bucket.listaChavePonteiro.get(i);
            if (novaPosição != antigaPosição){
                bucket.listaChavePonteiro.remove(p);
                i--;
                aux.listaChavePonteiro.add(p);
            }
        }


        if (bucket.full()){
            tratarBucketCheio(index, chave, livro);
        }else if (aux.full()){
            index = dir.bucketList.indexOf(aux);
            tratarBucketCheio(index, chave, livro);
        } else{
            adicionarNovoItem(chave, livro);
        }

    }

    public void adicionarNovoItem(int chave, Livro livro){
        System.out.println();
        System.out.println("-------------------adicionando novo item-----------------");
        int posição = chave % getTamanhoDir();
        ParChavePonteiro par = new ParChavePonteiro(chave, livro);
        dir.bucketList.get(posição).listaChavePonteiro.add(par);
        System.err.println("chave = " + chave + ", posição = " + posição + ", tamanhoDir = " + getTamanhoDir() + ", livro = " + livro + ", id = " + livro.getID());
    }

    public void verificarPLocal(Bucket bucket){
        if (bucket.pLocal == pGlobal){
            bucket.aumentarPLocal();
        }
    }


    public void inserir(int chave, Livro livro){
        int index = chave % getTamanhoDir();
        Bucket bucketAInserir =dir.bucketList.get(index);
        // Checar se cheio
        if (bucketAInserir.full()){
            if (bucketAInserir.pLocal == pGlobal){
                verificarPLocal(bucketAInserir);
                dobrarDiretorio();
                tratarBucketCheio(index, chave, livro);
            } else {
                tratarBucketCheio(index, chave, livro);
            }
            
        } else{
            adicionarNovoItem(chave, livro);
        }
        showBucketContent();
    }


    public void showBucketContent(){
        System.out.println("dir = "+ dir + " pGlobal = " + pGlobal);
        System.out.println("bucketlist = ");
        for (int i = 0; i< dir.bucketList.size(); i++){
            System.out.print("bucket " + i + " = " );
            Bucket bucketatual = dir.bucketList.get(i);
            for (int j = 0; j< bucketatual.listaChavePonteiro.size(); j++){
                System.out.print(bucketatual.listaChavePonteiro.get(j).getChave() + " ");
            }
            System.out.println(" pLocal = " + bucketatual.pLocal);
        }
    }
    
    public void buscar(int id){
        int index = id % getTamanhoDir();
        Bucket bucket = dir.bucketList.get(index);
        Livro livro = null;
        for (int i=0; i < bucket.listaChavePonteiro.size(); i++){
            ParChavePonteiro par = bucket.listaChavePonteiro.get(i);
            if (par.chave == id){
                livro = par.livro;
                break;
            }else{
                System.out.println("Não achou");
            }
        }
        Livro.exibir(livro);
    }

/*     public int numberToAdd(int index){
            
    } */
    
    public void deletar(int id){
        int index = id % getTamanhoDir();
        Bucket bucket = dir.bucketList.get(index);
        Bucket aux = dir.bucketList.get(getLSB(index, pGlobal));

    }

    public static void main(String[] args) throws IOException{
        RandomAccessFile arq = new RandomAccessFile("arquivo.db", "rw");
        CRUD crud = new CRUD();
        HashingEstendido hashing = new HashingEstendido(1, 3);
        int option;
        do {
            System.out.println(" Inicio ");
            System.out.println("Selecione uma opção:\n1. Adicionar um número.\n2. Buscar um número. \n3. Deletar um número.\n4. Sair\n");
            option = scan.nextInt();
            switch (option) {
                case 1:
                    System.out.println("Qual o número que deseja inserir?\n");
                    int valor = scan.nextInt();
                    Livro livro = crud.buscarLivro(valor, arq);
                    hashing.inserir(valor, livro);
                    break;
                case 2:
                    System.out.println("Qual o número que deseja buscar?\n");
                    valor = scan.nextInt();
                    hashing.buscar(valor);
                case 3:
                    /* System.out.println("Qual o número que deseja deletar?\n");
                    valor = scan.nextInt();
                    b.remover(valor); */
                case 4:
                    break;
                default:
                    System.out.println("Favor inserir um número valido:");
                    System.out.println("\n--- Original B-Tree ---\n");
            }
            /* b.printTree(b.raiz, 0); */
        } while (option != 4);



    }
}
