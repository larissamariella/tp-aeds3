import java.io.*;

class Livro {

    private int id;
    private String codigo;
    private String titulo;
    private String autor;
    private Float avaliacao;
    private Float preco;
    private boolean kindleUnlimited;
    private long data;
    private String[] nomeCategoria;
    private long prox;

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

    // Setters e Getters

    public void setprox(Long prox) {
        this.prox = prox;
    }

    public Long getprox() {
        return this.prox;
    }

    // ID
    public void setId(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    // Codigo
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return this.codigo;
    }

    // Titulo
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return this.titulo;
    }

    // Autor
    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getAutor() {
        return this.autor;
    }

    // Avaliação
    public void setAvaliacao(Float avaliacao) {
        this.avaliacao = avaliacao;
    }

    public float getAvaliacao() {
        return this.avaliacao;
    }

    // Preço
    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public float getPreco() {
        return this.preco;
    }

    // Kindle Unlimited
    public void setKindleUnlimited(Boolean kindleUnlimited) {
        this.kindleUnlimited = kindleUnlimited;
    }

    public boolean getKindleUnlimited() {
        return this.kindleUnlimited;
    }

    // Data
    public void setData(long data) {
        this.data = data;
    }

    public long getData() {
        return this.data;
    }

    // Nome Categoria
    public void setNomeCategoria(String[] nomeCategoria) {
        this.nomeCategoria = new String[nomeCategoria.length];
        for (int i = 0; i < nomeCategoria.length; i++) {
            this.nomeCategoria[i] = nomeCategoria[i];
        }
    }

    public String[] getNomeCategoria() {
        return this.nomeCategoria;
    }

    // Método para exibir livro
    public static void exibir(Livro livro) {
        System.out.println("ID: " + livro.getID());
        System.out.println("Código: " + livro.getCodigo());
        System.out.println("Título: " + livro.getTitulo());
        System.out.println("Autor: " + livro.getAutor());
        System.out.println("Avaliação: " + livro.getAvaliacao());
        System.out.println("Preço: " + livro.getPreco());
        System.out.println("Kindle Unlimited: " + livro.getKindleUnlimited());

        if (livro.getData() != -1) {
            System.out.println("Data: " + livro.getData());
        } else {
            System.out.println("Data: Não informado");
        }

        System.out.print("Nome da Categoria: ");
        for (int i = 0; i < livro.getNomeCategoria().length; i++) {
            System.out.print(livro.getNomeCategoria()[i] + " ");
        }
        System.out.println("\n");
    }

    // Retorna o array de bytes com as informações do livro
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

    // Retorna objeto livro que foi lido pelo array de bytes
    public Livro fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        Livro livro = new Livro();
        livro.setId(dis.readInt());
        livro.setCodigo(dis.readUTF());
        livro.setTitulo(dis.readUTF());
        livro.setAutor(dis.readUTF());
        livro.setAvaliacao(dis.readFloat());
        livro.setPreco(dis.readFloat());
        livro.setKindleUnlimited(dis.readBoolean());
        livro.setData(dis.readLong());
        int tamCategorias = dis.readInt();
        String[] nomeCategoria = new String[tamCategorias];
        for (int i = 0; i < tamCategorias; i++) {
            nomeCategoria[i] = dis.readUTF();
        }
        livro.setNomeCategoria(nomeCategoria);
        return livro;
    }

    // Outro tipo de inicializador do objeto Livro
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