import java.util.*;
import java.io.*;

class Livro {

    int id;
    String codigo;
    String titulo;
    String autor;
    float avaliacao;
    float preco;
    boolean kindleUnlimited;
    String data;
    String nomeCategoria;

    Livro(int id, String codigo, String titulo, String autor, float avaliacao, float preco, boolean kindleUnlimited,
            String data, String nomeCategoria) {
        this.id = id;
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.avaliacao = avaliacao;
        this.preco = preco;
        this.kindleUnlimited = kindleUnlimited;
        this.data = data;
        this.nomeCategoria = nomeCategoria;
    }

    public Livro() {
        this.id = 0;
        this.codigo = "";
        this.titulo = "";
        this.autor = "";
        this.avaliacao = 0.0f;
        this.preco = 0.0f;
        this.kindleUnlimited = false;
        this.data = "21112005";
        this.nomeCategoria = "";
    }

    static void exibir(Livro l) {
        System.out.println("ID: " + l.id +
                "\nCodigo: " + l.codigo +
                "\nTitulo: " + l.titulo +
                "\nAutor: " + l.autor +
                "\nAvaliacao: " + l.avaliacao +
                "\nPreco: " + l.preco +
                "\nKindleUnlimited: " + l.kindleUnlimited +
                "\nData: " + l.data +
                "\nNome Categoria: " + l.nomeCategoria + "\n");
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeUTF(codigo);
        dos.writeUTF(titulo);
        dos.writeUTF(autor);
        dos.writeFloat(avaliacao);
        dos.writeFloat(preco);
        dos.writeBoolean(kindleUnlimited);
        dos.writeUTF(data);
        dos.writeUTF(nomeCategoria);

        return baos.toByteArray();
    }

    public void fromByteArray(byte [] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readInt();
        codigo = dis.readUTF();
        titulo = dis.readUTF();
        autor = dis.readUTF();
        avaliacao = dis.readFloat();
        preco = dis.readFloat();
        kindleUnlimited = dis.readBoolean();
        data = dis.readUTF();
        nomeCategoria = dis.readUTF();
    }

    static void iniciarArquivo() throws IOException {
        RandomAccessFile arqEntrada = new RandomAccessFile("ex.csv", "rw");
        RandomAccessFile arqSaida = new RandomAccessFile("10booksExample.db", "rw");

        String str = "";
        int index = 0;
        Livro livro;

        while ((str = arqEntrada.readLine()) != null) {
            
            index++;
            livro = new Livro();
            livro.id = index;

            String[] divisao = str.split(",", -1);

            livro.codigo = divisao[0];

            if (str.contains(String.valueOf('"'))) {

                int posicaoInicial = str.indexOf('"');
                int posicaoFinal = str.indexOf('"', posicaoInicial + 1);
                divisao = str.substring(posicaoFinal + 2, str.length()).split(",", -1);

                String titulo = str.substring(posicaoInicial + 1, posicaoFinal);

                livro.titulo = titulo;
                livro.autor = divisao[0];
                livro.avaliacao = Float.parseFloat(divisao[1]);
                livro.preco = Float.parseFloat(divisao[2]);
                livro.kindleUnlimited = (divisao[3].equals("TRUE")) ? true : false;
                livro.data = divisao[4];
                livro.nomeCategoria = divisao[5];
            } else {
                livro.titulo = divisao[1];
                livro.autor = divisao[2];
                livro.avaliacao = Float.parseFloat(divisao[3]);
                livro.preco = Float.parseFloat(divisao[4]);
                livro.kindleUnlimited = (divisao[5].equals("TRUE")) ? true : false;
                livro.data = divisao[6];
                livro.nomeCategoria = divisao[7];
            }

            byte[] ba = livro.toByteArray();

            arqSaida.writeInt(ba.length);
            arqSaida.write(ba);
        }

        arqEntrada.close();
        arqSaida.close();
    }

    static void exibirArquivo() throws IOException {
        RandomAccessFile arqEntrada = new RandomAccessFile("10booksExample.db", "rw");

        int ultimoId = arqEntrada.readInt();
        System.out.println(ultimoId);
        int tamanho;
        char lapide;
        byte[] ba;
        Livro livro;

        int i = 10;

        while (i-- > 0) {

            lapide = arqEntrada.readChar();
            tamanho = arqEntrada.readInt();
            ba = new byte[tamanho];
            livro = new Livro();

            if (lapide != '*') {
                arqEntrada.read(ba);
                livro.fromByteArray(ba);
                exibir(livro);
            } else {
                arqEntrada.seek(arqEntrada.getFilePointer() + tamanho);
            }
        }
        arqEntrada.close();
    }

    public static void main(String[] args) {

        try {
            iniciarArquivo();
        } catch (IOException e) {
            System.err.println("Erro no arquivo");
        }
    }
}