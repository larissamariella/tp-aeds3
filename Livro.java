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
    String [] nomeCategoria;
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

    public Livro(int id, String codigo, String titulo, String autor, Float avaliacao, Float preco, boolean kindleUnlimited, long data, String [] nomeCategoria) {
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
    
    @Override
    public String toString() {
        return "ID: " + id + "\n" +
        "Código: " + codigo + "\n" +
        "Título: " + titulo + "\n" +
        "Autor: " + autor + "\n" +
        "Avaliação: " + avaliacao + "\n" +
        "Preço: " + preco + "\n" +
        "Kindle Unlimited: " + kindleUnlimited + "\n" +
        "Data: " + getData() + "\n" +
        "Nome da Categoria: " + nomeCategoria + "\n";
    }

    public String getData(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(data);
        return dateString;
    }

    public static void exibir(Livro livro) {
        System.out.print("ID: " + livro.id + "\n" +
        "Código: " + livro.codigo + "\n" +
        "Título: " + livro.titulo + "\n" +
        "Autor: " + livro.autor + "\n" +
        "Avaliação: " + livro.avaliacao + "\n" +
        "Preço: " + livro.preco + "\n" +
        "Kindle Unlimited: " + livro.kindleUnlimited + "\n" +
        "Data: " + livro.data + "\n" +
        "Nome da Categoria: ");
        for (int i = 0; i <livro.nomeCategoria.length; i++) {
            System.out.print(livro.nomeCategoria[i] + " ");
        }
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
        dos.writeLong(data);
        //dos.writeUTF(nomeCategoria);

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
        data = dis.readLong();
      //  nomeCategoria = dis.readUTF();
    }
}