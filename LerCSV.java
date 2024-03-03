import java.io.*;

public class LerCSV {

    // Inicia a leitura do csv e tambémm, a escrita do arquivo de bytes 
    void lerArquivoCSV() throws IOException {
        String nomeArquivo = "amz kindle books updated.csv";
        RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "rw");
        RandomAccessFile arqByte = new RandomAccessFile("arquivo.db", "rw");
        
        int ultimoID = 0;
        String str = arq.readLine();
        Livro livro;
        arqByte.seek(0);
        while ((str = arq.readLine()) != null) {
            livro = new Livro();
            livro = lerStringLivro(str, livro, ultimoID);
            ultimoID ++;
            Util.escreverLivro(livro, arqByte);
        }
        arq.close();
        arqByte.close();
    }

    // Faz a manipulação da linha da bd que foi lida
    Livro lerStringLivro(String str, Livro livro, int ultimoID){
        // A variável indice controla a posição da linha lida que sera utilizada para cada trecho dos atributos  
        int indice = 0;
        livro.setId(ultimoID+1);
        indice = lerCodigo(str, livro);
        indice = lerTitulo(str, livro, indice);
        indice = lerAutor(str, livro, indice);
        indice = lerAvaliacao(str, livro, indice);
        indice = lerPreco(str, livro, indice);
        indice = lerKindleUnlimited(str, livro, indice);
        indice = lerData(str, livro, indice);
        lerCategoria(str, livro, indice);
        return livro;
    }


    // Separa a string referente ao código do livro, e retorna a posição
    int lerCodigo(String str, Livro livro) {
        int posicao = str.indexOf(',');
        String codigo = str.substring(0, posicao);
        livro.setCodigo(codigo);
        return posicao;
     }

     // Separa a string do titulo e retorna a posição da linha que parou
     int lerTitulo(String str, Livro livro, int i) {
        String titulo = "";
        int posicao = 0;;

        String aux = str.substring(i+1);

        if(aux.charAt(0)=='"'){
            for(int j=1;j<aux.length();j++){
                if(aux.charAt(j)=='"'){
                    posicao = j+i+2;
                    j=aux.length();
                }
                else{
                    titulo += aux.charAt(j);
                }
            }
        }
        else{
            posicao = str.indexOf(',', i + 1);
            titulo = str.substring(i + 1, posicao);
        }
        livro.setTitulo(titulo);
        return posicao;
    }

    //Separa a string do autor, retornando a posição da string que acaba o atributo autor
    int lerAutor(String str, Livro livro, int i) {
        String autor = "";
        int posicao = 0;
        String aux = str.substring(i + 1, str.length());

        if (aux.contains(String.valueOf('"'))) {
            int posicaoInicial = aux.indexOf('"');
            posicao = aux.indexOf('"', posicaoInicial + 1);
            autor = aux.substring(posicaoInicial + 1, posicao);
            posicao = i + autor.length() + 1;
        } 
        else {
            posicao = str.indexOf(',', i + 1);
            autor = str.substring(i + 1, posicao);
        }
        livro.setAutor(autor);
        return posicao;
    }

    // Sepaa a string de avaliação
    int lerAvaliacao(String str, Livro livro, int i) {
        String aux = str.substring(i);
        String avaliacao = "";
        int posicao = 0;

        if (aux.contains(String.valueOf('"'))) {
            posicao = str.indexOf(',', i + 3);
            avaliacao = str.substring(i + 3, posicao);
        }
        else{
            posicao = str.indexOf(',', i + 1);
            avaliacao = str.substring(i + 1, posicao);
        }
        livro.setAvaliacao(Float.parseFloat(avaliacao));
        return posicao;
    }

    // Lê a string referente a um float
    int lerPreco(String str, Livro livro, int i) {
        int posicao = str.indexOf(',', i + 1);
        String preco = str.substring(i + 1, posicao);
        livro.setPreco(Float.parseFloat(preco));
        return posicao;
    }

    // L}e o trecho da string referente ao boolean
    int lerKindleUnlimited(String str, Livro livro, int i) {
        int posicao = str.indexOf(',', i + 1);
        String kindleUnlimited = str.substring(i + 1, posicao);
        livro.setKindleUnlimited(Boolean.parseBoolean(kindleUnlimited));
        return posicao;
    }

    // Separa a string data e converte para long
    int lerData(String str, Livro livro, int i) {
        int posicao = str.indexOf(',', i + 1);
        String data = str.substring(i + 1, posicao);

        if (!data.equals("")) livro.setData(Util.formatarData(data));
        return posicao;
    }

    // Separa cada categoria de acordo com o separador &
    void lerCategoria(String str, Livro livro, int i){
        String categorias = str.substring(i + 1, str.length());
        String [] categoria = categorias.split(" & ",-1);

        String [] nomeCategoria = new String[categoria.length];
        for (int j = 0; j < categoria.length; j++) {
            nomeCategoria[j] = categoria[j];
        }
        livro.setNomeCategoria(nomeCategoria);
    }
}