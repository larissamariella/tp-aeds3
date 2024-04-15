/* Estrutura do arquivo
 * Diretório é uma lista com p^2 ponteiros, onde p é um p global e os ponteiros apontam para os buckets
 * Bucket é uma lista de par ponteiro chave, onde cada item no bucket[i] é uma chave id do registro e um ponteiro para o registro.
 */
import java.io.RandomAccessFile;
import java.util.*;



public class HashingEstendido {
    RandomAccessFile arq;
    static Scanner scan = new Scanner(System.in);
    int pGlobal;
    int ordem;
    Diretorio dir;

    public HashingEstendido(int P, int Ordem) throws Exception{
        arq = new RandomAccessFile("arquivo.db", "rw");
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

    public int power2of(int n){
        double d = Double.valueOf(n);
        d = Math.pow(2, d);
        int aux = (int) d;
        return aux;
    }

    public int getIndexAux(int pLocal){ // Retorna o index do bucket auxiliar que irá dividir os item do bucket cheio
        double d = Double.valueOf(pLocal);
        d = Math.pow(2, d);
        int aux = (int) d;
        return aux;
    }

    public int getComplementIndex(){
        double d = Double.valueOf(pGlobal-1);
        d = Math.pow(2, d);
        int aux = (int) d;
        return aux;
    }


    public class Diretorio{
        private List<Bucket> bucketList;

        public Diretorio(int p){
            bucketList = new ArrayList<Bucket>(getTamanhoDir());
            for (int i = 0; i < getTamanhoDir(); i++){
                Bucket b = new Bucket();
                bucketList.add(b);
            }
        }
    }

    public class ParChavePonteiro{
        private int chave;
        private long pos;

        private int getChave(){
            return this.chave;
        }
        private void setChave(int key){
            chave = key;
        }
        private long getPos(){
            return this.pos;
        }
        private void setPos(long position){
            pos = position;
        }

        private ParChavePonteiro(int key, long position){
            setChave(key);
            setPos(position);
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


    public void dobrarDiretorio(){
        this.aumentarPGlobal();
        Diretorio novoDir = new Diretorio(pGlobal);
        for (int i = 0; i < this.getTamanhoDir(); i++){
            novoDir.bucketList.set(i, dir.bucketList.get(i & ((getTamanhoDir()/2)-1)));
        }
        dir = novoDir;
        //System.err.println("Buckets após dobrar diretório");
        //showBucketContent();
    }

    public void tratarBucketCheio(int index, int chave, long pos){
        Bucket bucket = dir.bucketList.get(index);
        Bucket aux = new Bucket(bucket.pLocal);
        //System.out.println("index = " + index + ", Complement Index = " + getComplementIndex());
        int indexAux;
        if (index + getComplementIndex() > power2of(bucket.pLocal)){
            indexAux = index % getIndexAux(bucket.pLocal); 
            //System.out.println("indexAux = " + indexAux);
            dir.bucketList.set(indexAux , aux); // Zera o bucket auxiliar para dividir chaves com bucketCheio
        } else {
            dir.bucketList.set(index + getComplementIndex(), aux); // Zera o bucket auxiliar para dividir chaves com bucketCheio
        }

        if (bucket.pLocal < pGlobal){
            bucket.aumentarPLocal();
            aux.aumentarPLocal();
        }
        for (int i=0; i< bucket.listaChavePonteiro.size();i++){
            int antigaPosição = index;
            int novaPosição = bucket.listaChavePonteiro.get(i).getChave() % getTamanhoDir(); // Nova posição para chave
            //System.out.println("antigaposição = " + antigaPosição);
            //System.out.println("chave " + bucket.listaChavePonteiro.get(i).chave + " mod " +getTamanhoDir()+ " = " + novaPosição); 
            ParChavePonteiro p = bucket.listaChavePonteiro.get(i);
            if (novaPosição != antigaPosição){
                bucket.listaChavePonteiro.remove(p);
                i--;
                aux.listaChavePonteiro.add(p);
            }
        }


        if (bucket.full()){
            tratarBucketCheio(index, chave, pos);
        }else if (aux.full()){
            index = dir.bucketList.indexOf(aux);
            tratarBucketCheio(index, chave, pos);
        } else{
            adicionarNovoItem(chave, pos);
        }

    }

    public void adicionarNovoItem(int chave, long pos){
        //System.out.println();
        //System.out.println("-------------------adicionando novo item-----------------");
        int posição = chave % getTamanhoDir();
        ParChavePonteiro par = new ParChavePonteiro(chave, pos);
        dir.bucketList.get(posição).listaChavePonteiro.add(par);
        //System.err.println("chave = " + chave + ", posição = " + posição + ", tamanhoDir = " + getTamanhoDir() + ", pos = " + pos);
    }




    public void inserir(int chave, Long pos){
        int index = chave % getTamanhoDir();
        //System.out.println("chave = " + chave + " TamanhoDir = " + getTamanhoDir());
        Bucket bucketAInserir =dir.bucketList.get(index);
        // Checar se cheio
        if (bucketAInserir.full()){
            if (bucketAInserir.pLocal == pGlobal){
                bucketAInserir.aumentarPLocal();
                dobrarDiretorio();
                tratarBucketCheio(index, chave, pos);
            } else {
                tratarBucketCheio(index, chave, pos);
            }
            
        } else{
            adicionarNovoItem(chave, pos);
        }
        //showBucketContent();
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
    
    public Livro buscar(int id) throws Exception{
        
        int index = ((id) % getTamanhoDir());
        Bucket bucket = dir.bucketList.get(index);
        long pos = 0;
        Livro livro = null;
        for (int i=0; i < bucket.listaChavePonteiro.size(); i++){
            ParChavePonteiro par = bucket.listaChavePonteiro.get(i);
            if (par.getChave() == id){
                pos = par.getPos();
                livro = instanciarLivro(pos, arq);
                break;
            }else{
                System.out.println("Não achou");
            }
        }
        return livro;
    }

    public Livro instanciarLivro(long pos, RandomAccessFile arq) throws Exception{
        arq.seek(pos); // Move ponteiro para posição do livro na base de dados

        Livro livro = new Livro();
        char lapide = arq.readChar();
        int tamanho = arq.readInt();

        // Verifica lápide do livro a ser atualizado não está marcado e se encontrou o livro
        if (lapide != '*' && pos != 0) {
            byte[] ba = new byte[tamanho];
            arq.read(ba);
            livro = livro.fromByteArray(ba);
        }
        return livro;
    }

    
    public void deletar(int id){
        int index = id % getTamanhoDir();
        Bucket bucket = dir.bucketList.get(index);
        for (int i = 0; i < ordem; i++){
            if(id == bucket.listaChavePonteiro.get(i).getChave()){
                bucket.listaChavePonteiro.remove(i);
                break;
            }
        }
    }

    public void update(int id, long novaPosição){
        int index = id % getTamanhoDir();
        Bucket bucket = dir.bucketList.get(index);
        for (int i = 0; i < ordem; i++){
            if(id == bucket.listaChavePonteiro.get(i).getChave()){
                bucket.listaChavePonteiro.get(index).setPos(novaPosição);
                break;
            }
        }
    }

    public static void main(String[] args) throws Exception{
        HashingEstendido hashing = new HashingEstendido(1, 10);
        preencherHashing(hashing.arq, hashing);
    }
    


    static void preencherHashing (RandomAccessFile arq, HashingEstendido h) throws Exception{
        arq.seek(0);
        arq.readInt();
        Livro livro = new Livro();
        while (arq.getFilePointer() < arq.length()) {
            long oldPos = arq.getFilePointer();
            char lapide = arq.readChar();
            int tamanho = arq.readInt();

            
            if (lapide != '*') { // Checa se livro não foi removido ou atualizado por um registro maior

                byte[] ba = new byte[tamanho];
                arq.read(ba);
                livro = new Livro(ba);
                //System.err.println("----------- Inserindo id " + livro.getID() + " ---------------");
                h.inserir(livro.getID(), oldPos);
            } else { // Pula o registro do livro caso esteja marcado com a lápide
                arq.seek(arq.getFilePointer() + tamanho);
            }
        }
        toByteArray(arq, h);
    }

    static void toByteArray(RandomAccessFile arq, HashingEstendido h) throws Exception{
        // Estrutura do arquivo
        // tamanho -- i -- plocal -- chave1 -- pos1 -- chave2 -- pos2 -- ...
        RandomAccessFile hashingByte = new RandomAccessFile("hashing.db", "rw");
        int tamanho = h.dir.bucketList.size();
        hashingByte.writeInt(tamanho);

        // Estrutura do arquivo
        // tamanho -- pGlocal -- ordem -- i -- pos -- ...
        RandomAccessFile dirByte = new RandomAccessFile("dir.db", "rw");
        int tamanhoDir = h.getTamanhoDir();
        dirByte.writeInt(tamanhoDir);
        dirByte.writeInt(h.pGlobal);
        dirByte.writeInt(h.ordem);


        for (int i = 0; i < tamanho; i++){
            //System.out.println(i);
            Bucket bucketatual = h.dir.bucketList.get(i);

            dirByte.writeInt(i);
            dirByte.writeLong(hashingByte.getFilePointer()); 

            hashingByte.writeInt(i);
            hashingByte.writeInt(bucketatual.pLocal);
            
            for (int j = 0; j< bucketatual.listaChavePonteiro.size(); j++){
                //System.out.println("escrevendo par chave = " + bucketatual.listaChavePonteiro.get(j).getChave() + ", pos = " + bucketatual.listaChavePonteiro.get(j).getPos());
                hashingByte.writeInt(bucketatual.listaChavePonteiro.get(j).getChave());
                hashingByte.writeLong(bucketatual.listaChavePonteiro.get(j).getPos());
            }
        }
        
    }

    static HashingEstendido fromByteArray(RandomAccessFile dir, RandomAccessFile hash) throws Exception{
        dir.seek(0);
        hash.seek(0);
        int tamanho = dir.readInt();
        int pG = dir.readInt();
        int order = dir.readInt();
        HashingEstendido h = new HashingEstendido(pG, order);
        int pos;
        for (int i = 0; i < tamanho; i++){
            dir.seek(4); // pula o indice
            pos = dir.readInt();
            hash.seek(pos);
            int index = hash.readInt();
        }
        return h;
    }
}



