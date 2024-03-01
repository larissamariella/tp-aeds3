import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class CRUD {

   static Scanner scan = new Scanner(System.in);
      
    public static void menu() throws IOException {
        RandomAccessFile arq = new RandomAccessFile("ARQUIVO.db", "rw");

        CRUD crud = new CRUD(); 

        int opcao;

        do {
            System.out.println("╔═════════════════╗");
            System.out.println("║ +-- M E N U --+ ║");
            System.out.println("╚═════════════════╝");
            System.out.println("1. Adicionar Livro");
            System.out.println("2. Listar Livros");
            System.out.println("3. Buscar Livro");
            System.out.println("4. Atualizar Livro");
            System.out.println("5. Remover Livro");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scan.nextInt();
            scan.nextLine();

            switch (opcao) {
                case 1:
                    // Adicionar Livro
                    crud.adicionarLivro(arq);
                    break;
                case 2:
                    // Listar Livros
                    crud.listarLivros(arq);
                    break;
                case 3:
                    System.out.print("Digite o ID do livro que deseja buscar: ");
                    int id = scan.nextInt();
                    Livro livro = crud.buscarLivro(id, arq);
                    if(livro!=null){
                        System.out.println("\n+--Informações do livro--+");
                        Livro.exibir(livro);
                    }
                    else{
                        System.out.println("\nLivro não encontrado.\n");
                    }
                    break;
                case 4:
                    // Atualizar Livro
                    //   crud.atualizarLivro();
                    break;
                case 5:
                    System.out.print("Digite o ID do livro que deseja remover: ");
                    id = scan.nextInt();
                    crud.removerLivro(arq, id);
                    break;
                case 6:
                    System.out.println("Saindo do Menu.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 6);
        

       arq.close();
    }

    void listarLivros(RandomAccessFile arq) throws IOException{
        arq.seek(0);
        lerByte.lerArquivoByte(arq);
    }

    Livro buscarLivro(int id, RandomAccessFile arq) throws IOException{
        arq.seek(0);
        int ultimoID = arq.readInt();
        Livro livro = new Livro();
        boolean achou = false;

        while (arq.getFilePointer() < arq.length() && achou == false) {
            char lapide = arq.readChar();
            int tamanho = arq.readInt();

            if(lapide!='*'){
                byte[] ba = new byte[tamanho];
                arq.read(ba);
                livro = new Livro(ba);
                if (livro.id == id) achou = true;
            }
            else{
                arq.seek(arq.getFilePointer()+tamanho);
            }
        }
        if(achou==false) livro = null;
        return livro;
    }

    void adicionarLivro(RandomAccessFile arq)throws IOException{
        arq.seek(0); 
        int ultimoID = arq.readInt();
        Livro livro = new Livro();

        livro.id = ultimoID+1;
        
        System.out.print("Informe o Código do livro: ");
        livro.setCodigo(scan.nextLine());

        System.out.print("Informe o Título do livro: ");
        livro.setTitulo(scan.nextLine());

        System.out.print("Informe o Autor do livro: ");
        livro.setAutor(scan.nextLine());

        System.out.print("Informe a Avaliação do livro: ");
        livro.setAvaliacao(scan.nextLine());

        System.out.print("Informe o Preço do livro: ");
        livro.setPreco(scan.nextLine());

        System.out.print("O livro possui Kindle Unlimited? (true/false): ");
        livro.setKindleUnlimited(scan.nextLine());

        System.out.print("Informe a Data do livro (yyyy-mm-dd): ");
        livro.setData(scan.nextLine());

        System.out.print("Informe a quantidade de Categorias: ");
        int quantidadeCategorias = scan.nextInt();
        scan.nextLine();

        String[] nomeCategoria = new String[quantidadeCategorias];
        for (int i = 0; i < quantidadeCategorias; i++) {
            System.out.print("Informe a Categoria " + (i + 1) + ": ");
            nomeCategoria[i] = scan.nextLine();
        }
        livro.setNomeCategoria(nomeCategoria);

        Util.escreverLivro(livro, arq);
        System.out.println("\nLivro adicionado com sucesso!\n");
        Livro.exibir(livro);
    }

    void removerLivro(RandomAccessFile arq, int id) throws IOException{
        arq.seek(0);

        int ultimoID = arq.readInt();
        Livro livro = new Livro();
        boolean achou = false;

        while (arq.getFilePointer() < arq.length() && achou == false) {
            long posicaoLapide = arq.getFilePointer();
            char lapide = arq.readChar();
            int tamanho = arq.readInt();

            if(lapide!='*'){
                byte[] ba = new byte[tamanho];
                arq.read(ba);
                livro = new Livro(ba);
               
                if (livro.id == id) {
                    arq.seek(posicaoLapide);
                    arq.writeChar('*');
                    System.out.println("\nLivro de id " + livro.id + " removido com sucesso!");
                    achou = true;
                }    
            }
            else{
                arq.seek(arq.getFilePointer()+tamanho);
            }
        }
        if(achou==false) {
            System.out.println("\nEsse livro não foi encontrado no arquivo.\n");
        }
    }

    void atualizarLivro(int id, RandomAccessFile arq) throws IOException {
        Livro livro = buscarLivro(id, arq);


        
    }

    
    public static void main(String[] args) {
        lerCSV l = new lerCSV();
    
        try{
           // l.lerArquivoCSV(); // a primeira vez que for compilar -> roda esse lerArquivoCSV e depois comenta ele
            menu();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        scan.close();
    }
     
}
