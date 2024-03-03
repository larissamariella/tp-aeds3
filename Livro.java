import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class Livro {

    int id;
    String codigo;
    String titulo;
    String autor;
    Float avaliacao;
    Float preco;
    boolean kindleUnlimited;
    long data;
    String[] nomeCategoria;
    long dataPublicada;

    public Livro() {
        this.id = 0;
        this.codigo = "";
        this.titulo = "";
        this.autor = "";
        this.avaliacao = 0.0f;
        this.preco = 0.0f;
        this.kindleUnlimited = false;
        this.data = -1;
        this.nomeCategoria = new String[0];
    }

    public Livro(int id, String codigo, String titulo, String autor, Float avaliacao, Float preco,
            boolean kindleUnlimited, long data, String[] nomeCategoria) {
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

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = Float.parseFloat(avaliacao);
    }

    public void setPreco(String preco) {
        this.preco = Float.parseFloat(preco);
    }

    public void setKindleUnlimited(String kindleUnlimited) {
        this.kindleUnlimited = Boolean.parseBoolean(kindleUnlimited);
    }

    public void setData(String data) {
        this.data = Util.formatarData(data);
    }

    public void setNomeCategoria(String[] nomeCategoria) {
        this.nomeCategoria = new String[nomeCategoria.length];
        for (int i = 0; i < nomeCategoria.length; i++) {
            this.nomeCategoria[i] = nomeCategoria[i];
        }
    }

    public void setDataPublicada(String dataPublicada) {
        this.dataPublicada = Long.parseLong(dataPublicada);
    }
    
    public static void exibir(Livro livro) { // mudar o livro.id para livro.getID()
        System.out.print("ID: " + livro.id + "\n" +
                "Código: " + livro.codigo + "\n" +
                "Título: " + livro.titulo + "\n" +
                "Autor: " + livro.autor + "\n" +
                "Avaliação: " + livro.avaliacao + "\n" +
                "Preço: " + livro.preco + "\n" +
                "Kindle Unlimited: " + livro.kindleUnlimited + "\n" +
                "Data: " + livro.data + "\n" +
                "Nome da Categoria: ");
        for (int i = 0; i < livro.nomeCategoria.length; i++) {
            System.out.print(livro.nomeCategoria[i] + " ");
        }
        System.out.println("\n");
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
        dos.writeLong(data);
        dos.writeInt(nomeCategoria.length);
        for (int i = 0; i < nomeCategoria.length; i++) {
            dos.writeUTF(nomeCategoria[i]);
        }
        
        return baos.toByteArray();
    }

    public Livro fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        Livro livro = new Livro();
        livro.id = dis.readInt();
        livro.codigo = dis.readUTF();
        livro.titulo = dis.readUTF();
        livro.autor = dis.readUTF();
        livro.avaliacao = dis.readFloat();
        livro.preco = dis.readFloat();
        livro.kindleUnlimited = dis.readBoolean();
        livro.data = dis.readLong();
        int tamCategorias = dis.readInt();
        livro.nomeCategoria = new String[tamCategorias];
        for (int i = 0; i < tamCategorias; i++) {
            livro.nomeCategoria[i] = dis.readUTF();
        }
        return livro;
    }

    public Livro(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readInt();
        codigo = dis.readUTF();
        titulo = dis.readUTF();
        autor = dis.readUTF();
        avaliacao = dis.readFloat();
        preco = dis.readFloat();
        kindleUnlimited = dis.readBoolean();
        data = dis.readLong();
        int tamCategorias = dis.readInt();
        nomeCategoria = new String[tamCategorias];
        for (int i = 0; i < tamCategorias; i++) {
            nomeCategoria[i] = dis.readUTF();
        }
    }
}