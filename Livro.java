import java.io.*;
import java.util.*;

class Livro {

    int id;
    String codigo;
    String titulo;
    String autor;
    Float avaliacao;
    Float preco;
    boolean kindleUnlimited;
    String nomeCategoria;

    public Livro() {
        this.id = 0;
        this.codigo = "";
        this.titulo = "";
        this.autor = "";
        this.avaliacao = 0.0f;
        this.preco = 0.0f;
        this.kindleUnlimited = false;
        this.nomeCategoria = "";
    }

    public Livro(int id, String codigo, String titulo, String autor, Float avaliacao, Float preco,
            boolean kindleUnlimited,
            String nomeCategoria) {
        this.id = id;
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.avaliacao = avaliacao;
        this.preco = preco;
        this.kindleUnlimited = kindleUnlimited;
        this.nomeCategoria = nomeCategoria;
    }
    
    @Override
    public String toString() {
        return "ID: " + id + "\n" +
        "Código: " + codigo + "\n" +
        "Título: " + titulo + "\n" +
        "Autor: " + autor + "\n" +
        "Avaliação: " + avaliacao + "\n" +
        "Preço: " + preco + "\n" +
        "Kindle Unlimited: " + kindleUnlimited + "\n" +
        "Nome da Categoria: " + nomeCategoria + "\n";
    }

    public void exibir() {
        System.out.println("ID: " + id + "\n" +
        "Código: " + codigo + "\n" +
        "Título: " + titulo + "\n" +
        "Autor: " + autor + "\n" +
        "Avaliação: " + avaliacao + "\n" +
        "Preço: " + preco + "\n" +
        "Kindle Unlimited: " + kindleUnlimited + "\n" +
        "Nome da Categoria: " + nomeCategoria + "\n");
        System.out.println();
    }

    public byte [] toByteArray() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeUTF(codigo);
        dos.writeUTF(titulo);
        dos.writeUTF(autor);
        dos.writeFloat(avaliacao);
        dos.writeFloat(preco);
        dos.writeBoolean(kindleUnlimited);
        dos.writeUTF(nomeCategoria);

        return baos.toByteArray();
    }

    public void fromByteArray(byte [] ba) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readInt();
        codigo = dis.readUTF();
        titulo = dis.readUTF();
        autor = dis.readUTF();
        avaliacao = dis.readFloat();
        preco = dis.readFloat();
        kindleUnlimited = dis.readBoolean();
        nomeCategoria = dis.readUTF();
    }

    static void inicializarArquivo() throws IOException {
        String nomeArquivo = "10booksExample.csv";
        RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "rw");

        String str = arq.readLine();
        int index = 1;
        Livro livro;

        RandomAccessFile arqByte = new RandomAccessFile("arquivoByte.db", "rw");

        while ((str = arq.readLine()) != null) {

            livro = new Livro();
            livro.id = index;

            String[] divisao = str.split(",", -1);
            livro.codigo = divisao[0];
            if (str.contains(String.valueOf('"'))) {
                int posicaoInicial = str.indexOf('"');
                int posicaoFinal = str.indexOf('"', posicaoInicial + 1);

                String titulo = str.substring(posicaoInicial + 1, posicaoFinal);

                divisao = str.substring(posicaoFinal + 2, str.length()).split(",", -1);

                livro.titulo = titulo;
                livro.autor = divisao[0];
                livro.avaliacao = Float.parseFloat(divisao[1]);
                livro.preco = Float.parseFloat(divisao[2]);
                livro.kindleUnlimited = (divisao[3].equals("TRUE")) ? true : false;
                livro.nomeCategoria = divisao[5];
            } else {
                livro.titulo = divisao[1];
                livro.autor = divisao[2];
                livro.avaliacao = Float.parseFloat(divisao[3]);
                livro.preco = Float.parseFloat(divisao[4]);
                livro.kindleUnlimited = (divisao[5].equals("TRUE")) ? true : false;
                livro.nomeCategoria = divisao[7];
            }
            index++;
           
            escreverObjeto(livro);
        }
    }

    static void escreverObjeto(Livro livro){
    
       try{
            RandomAccessFile arq = new RandomAccessFile("arquivoByte.db", "rw");
            arq.seek(arq.length());

            byte [] baOutput = livro.toByteArray();

            arq.writeInt((baOutput.length));
            //arq.writeChar(' ');
            arq.write(baOutput);

            arq.close();
       }
       catch (IOException e){
            System.out.println("Erro no arquivo 1");
       }
    }


   static Livro lerObjeto(){
        Livro livro = new Livro();

        try{
            RandomAccessFile arq = new RandomAccessFile("arquivoByte.db", "rw");
            int tamanho = arq.readInt();

            //System.out.println("tam: " + tamanho);
            
            byte [] baInput = new byte[tamanho];
            //char lapide = arq.readChar();
            arq.read(baInput);
    
            livro.fromByteArray(baInput);

            arq.close();
       }
       catch (IOException e){
            System.out.println("Erro no arquivo 2");
       }

       return livro;
    }

    public static void main(String[] args) {
        Livro livro = new Livro();
        try{
            inicializarArquivo();
            livro = lerObjeto();
            System.out.println(livro);
        }
        catch(IOException e){
            System.out.println("Erro no arquivo");
        }
    }

}