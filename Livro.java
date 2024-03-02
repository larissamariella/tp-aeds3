import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class Livro {

    private int id;
    private String codigo;
    private String titulo;
    private String autor;
    private Float avaliacao;
    private Float preco;
    private boolean kindleUnlimited;
    private long data;
    private String [] nomeCategoria;
    private long dataPublicada;

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

    //Setters e Getters

        // ID
        public void setId(int id) {
            this.id = id;
        }

        public int getID(){
            return this.id;
        }

        // Codigo
        public void setCodigo(String codigo){
            this.codigo = codigo;
        }

        public String getCodigo(){
            return this.codigo;
        }

        // Titulo
        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getTitulo(){
            return this.titulo;
        }

        // Autor
        public void setAutor(String autor) {
            this.autor = autor;
        }

        public String getAutor(){
            return this.autor;
        }

        // Avaliação
        public void setAvaliacao(String avaliacao) {
            this.avaliacao = Float.parseFloat(avaliacao);
        }

        public float getAvaliacao(){
            return this.avaliacao;
        }

        // Preço
        public void setPreco(Float preco) {
            this.preco = preco;
        }

        public float getPreco(){
            return this.preco;
        }

        // Kindle Unlimited
        public void setKindleUnlimited(String kindleUnlimited) {
            this.kindleUnlimited = Boolean.parseBoolean(kindleUnlimited);
        }

        public boolean getKindleUnlimited(){
            return this.kindleUnlimited;
        }

        // Data
        public void setData(long data) {
            this.data = data;
        }

        public long getData(){
            return this.data;
        }

        // Nome Categoria
        public void setNomeCategoria(String[] nomeCategoria) {
            this.nomeCategoria = new String[nomeCategoria.length];
            for (int i = 0; i < nomeCategoria.length; i++) {
            this.nomeCategoria[i] = nomeCategoria[i];
            }
        }

        public String [] getNomeCategoria() {
            return this.nomeCategoria;
        }

        // Data publicada
        public void setDataPublicada(String dataPublicada) {
            this.dataPublicada = Long.parseLong(dataPublicada);
        }

        public long getDataPublicada(){
            return this.dataPublicada;
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

/*     public String getData(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(data);
        return dateString;
    } */

    public static void exibir(Livro livro) {
        System.out.print("ID: " + livro.getID() + "\n" +
        "Código: " + livro.getCodigo() + "\n" +
        "Título: " + livro.getTitulo() + "\n" +
        "Autor: " + livro.getAutor() + "\n" +
        "Avaliação: " + livro.getAvaliacao() + "\n" +
        "Preço: " + livro.getPreco() + "\n" +
        "Kindle Unlimited: " + livro.getKindleUnlimited() + "\n" +
        "Data: " + livro.getData() + "\n" +
        "Nome da Categoria: ");
        for (int i = 0; i <livro.getNomeCategoria().length; i++) {
            System.out.print(livro.getNomeCategoria()[i] + " ");
        }
        System.out.println("\n");
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
        dos.writeInt(nomeCategoria.length);
        for (int i = 0; i < nomeCategoria.length; i++) {
            dos.writeUTF(nomeCategoria[i]);
        }
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
        int tamCategorias = dis.readInt();
        nomeCategoria = new String[tamCategorias];
        for (int i = 0; i < tamCategorias; i++) {
            nomeCategoria[i] = dis.readUTF();
        }
    }

    public Livro(byte [] ba) throws IOException{
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