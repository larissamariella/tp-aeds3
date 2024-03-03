import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class CRUD {

    static Scanner scan = new Scanner(System.in);

    public static void menu() throws IOException {
        RandomAccessFile arq = new RandomAccessFile("arquivo.db", "rw");

        CRUD crud = new CRUD();
        int opcao; 
        LerCSV csv = new LerCSV();

        System.out.println("Como deseja iniciar a base de dados?");
        System.out.println("1. Continuar em arquivo existente\t2. Iniciar um novo arquivo");
        int inicio = scan.nextInt();
        switch (inicio) {
            case 2:
                LoadingAnimation loadingAnimation = new LoadingAnimation();
                Thread loadingThread = new Thread(loadingAnimation);
                loadingThread.start(); 
                try{
                    csv.lerArquivoCSV();
                } finally {
                    loadingAnimation.stop();
                    try {
                        loadingThread.join(); // Espera a thread de animação terminar
                    } catch (InterruptedException e) {
                        e.printStackTrace(); // Trate a exceção ou propague para cima
                    }
                }
                break;
            default:
                break;
        }

        do {
            System.out.println("\n╔═════════════════╗");
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
                    // Buscar Livro
                    System.out.print("Digite o ID do livro que deseja buscar: ");
                    int id = scan.nextInt();
                    Livro livro = crud.buscarLivro(id, arq);
                    if (livro != null) {
                        System.out.println("\n+--Informações do livro--+");
                        Livro.exibir(livro);
                    } else {
                        System.out.println("\nLivro não encontrado. :(\r\n");
                    }
                    break;
                case 4:
                    // Atualizar livro
                    System.out.print("Digite o ID do livro que deseja atualizar: ");
                    id = scan.nextInt();
                    crud.atualizarLivro(id, arq);
                    break;
                case 5:
                    // Remover livro
                    System.out.print("Digite o ID do livro que deseja remover: ");
                    id = scan.nextInt();
                    crud.removerLivro(id, arq);
                    break;
                case 6:
                    // Sair
                    System.out.println("Saindo do Menu.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 6);

        arq.close();
    }

    void listarLivros(RandomAccessFile arq) throws IOException {
        arq.seek(0);
        arq.readInt();
        Livro livro = new Livro();

        while (arq.getFilePointer() < arq.length()) {
            char lapide = arq.readChar();
            int tamanho = arq.readInt();

            if (lapide != '*') {
                byte[] ba = new byte[tamanho];
                arq.read(ba);
                livro = new Livro(ba);
                Livro.exibir(livro);
            } else {
                arq.seek(arq.getFilePointer() + tamanho);
            }
        }
    }

    Livro buscarLivro(int id, RandomAccessFile arq) throws IOException {
        arq.seek(0);
        arq.readInt();
        Livro livro = new Livro();
        boolean achou = false;

        while (arq.getFilePointer() < arq.length() && achou == false) {
            char lapide = arq.readChar();
            int tamanho = arq.readInt();

            if (lapide != '*') {
                byte[] ba = new byte[tamanho];
                arq.read(ba);
                livro = new Livro(ba);
                if (livro.getID() == id)
                    achou = true;
            } else {
                arq.seek(arq.getFilePointer() + tamanho);
            }
        }
        if (achou == false)
            livro = null;
        return livro;
    }

    void adicionarLivro(RandomAccessFile arq) throws IOException {
        arq.seek(0);
        int ultimoID = arq.readInt();
        Livro livro = new Livro();
        
        livro.setId(ultimoID+1);

        System.out.print("Informe o Código do livro: ");
        livro.setCodigo(scan.nextLine());
        
        System.out.print("Informe o Título do livro: ");
        livro.setTitulo(scan.nextLine());

        System.out.print("Informe o Autor do livro: ");
        livro.setAutor(scan.nextLine());

        System.out.print("Informe a Avaliação do livro: ");
        validarAvalicao(livro);


        System.out.print("Informe o Preço do livro: ");
        validarPreco(livro);

        System.out.print("Informe se possui Kindle Unlimited [s/n];[sim/nao];[t/f];[true/false]:");
        validarKindleUnlimited(livro);

        System.out.print("Informe a Data do livro (yyyy-mm-dd): ");
        validarData(livro);

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

    void removerLivro(int id, RandomAccessFile arq) throws IOException {
        arq.seek(0);
        arq.readInt();

        Livro livro = new Livro();
        boolean achou = false;

        while (arq.getFilePointer() < arq.length() && achou == false) {
            long posicaoLapide = arq.getFilePointer();
            char lapide = arq.readChar();
            int tamanho = arq.readInt();

            if (lapide != '*') {
                byte[] ba = new byte[tamanho];
                arq.read(ba);
                livro = new Livro(ba);

                if (livro.getID() == id) {
                    arq.seek(posicaoLapide);
                    arq.writeChar('*');
                    System.out.println("\nLivro de id " + livro.getID() + " removido com sucesso!");
                    achou = true;
                }
            } else {
                arq.seek(arq.getFilePointer() + tamanho);
            }
        }
        if (achou == false) {
            System.out.println("\nEsse livro não foi encontrado no arquivo.\n");
        }
    }

    void atualizarLivro(int id, RandomAccessFile arq) throws IOException {
        arq.seek(0);
        long posicaoLivro = Util.posicaoLivro(id, arq);
        arq.seek(posicaoLivro);

        Livro livro = new Livro();
        char lapide = arq.readChar();
        int tamanho = arq.readInt();

        if (lapide != '*') {
            byte[] ba = new byte[tamanho];
            arq.read(ba);
            livro = livro.fromByteArray(ba);
            Livro.exibir(livro);

            System.out.println(
                    "\nQual campo do livro você deseja editar?\n1. Código\t2. Título\t3. Autor\t4. Avaliação\t5. Preço\t6. Kindle Unlimited\t7. Data \t8. Nome da Categoria");
            int escolha = scan.nextInt();
            scan.nextLine();
            switch (escolha) {
                case 1 -> {
                    System.out.print("Informe o novo Código:");
                    livro.setCodigo(scan.nextLine());
                }
                case 2 -> {
                    System.out.print("Informe o novo Título:");
                    livro.setTitulo(scan.nextLine());
                }
                case 3 -> {
                    System.out.print("Informe o novo Autor:");
                    livro.setAutor(scan.nextLine());
                }
                case 4 -> {
                    System.out.print("Informe a nova Avaliação:");
                    validarAvalicao(livro);
                }
                case 5 -> {
                    System.out.print("Informe o novo Preço:");
                    validarPreco(livro);
                }
                case 6 -> {
                    System.out.print("Informe se possui Kindle Unlimited [s/n];[sim/nao];[t/f];[true/false]:");
                    validarKindleUnlimited(livro);
                }
                case 7 -> {
                    System.out.print("Informe a nova Data:");
                    validarData(livro);
                }
                case 8 -> {
                    System.out.print("Informe a quantidade de Categorias: ");
                    int quantidadeCategorias = scan.nextInt();
                    scan.nextLine();

                    String[] nomeCategoria = new String[quantidadeCategorias];
                    for (int i = 0; i < quantidadeCategorias; i++) {
                        System.out.print("Informe a Categoria " + (i + 1) + ": ");
                        nomeCategoria[i] = scan.nextLine();
                    }
                    livro.setNomeCategoria(nomeCategoria);
                }
                default -> System.out.println("Escolha inválida.");
            }

            arq.seek(posicaoLivro);
            ba = livro.toByteArray();
            if (ba.length > tamanho) {
                arq.writeChar('*');
                arq.seek(arq.length());
                arq.writeChar('-');
                arq.writeInt(ba.length);
                arq.write(ba);
                System.out.println("Livro movido para o final do arquivo");
            } else {
                arq.writeChar('-');
                arq.writeInt(tamanho);
                arq.write(ba);
                System.out.println("\nLivro atualizado com sucesso!");
            }
        } else {
            System.out.println("\nLivro não encontrado. :(\r\n");
        }

    }

        //Funções de validação

    // Valida a avaliação dada ao livro. Deve ser entre 1.00 e 5.00
    public static void validarAvalicao(Livro livro){
        while (true) {
            try {
                float avaliacao = Float.parseFloat(scan.nextLine());
                if ((avaliacao >= 1 && avaliacao <= 5)) {
                    String nota = Float.toString(avaliacao);
                    livro.setAvaliacao(nota);
                    break;
                } else{
                    System.out.println("Avaliação inválida. Por favor, informe uma nota entre 1.00 e 5.00: ");
                }
            }catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, informe um número válido entre 1.00 e 5.00: ");
            }
        }
    }

    // Valida se o preço informado é valido.
    public static void validarPreco(Livro livro){
        while (true) {
            try {
                float preco = Float.parseFloat(scan.nextLine());
                livro.setPreco(preco);
                break;
            }catch (NumberFormatException | NullPointerException e) {
                System.out.println("Entrada inválida. Por favor, informe um número válido");
            }
        }
    }

    // Valida se a entrada informada é true, false, sim ou não. Todas maíusculas ou minúsculas
    public static void validarKindleUnlimited(Livro livro){
        while (true) {
            String kindleUnlimited = scan.nextLine();
            kindleUnlimited = kindleUnlimited.toUpperCase();
            if (kindleUnlimited.equals("TRUE") || 
                kindleUnlimited.equals("FALSE") ||
                kindleUnlimited.equals("NAO") ||
                kindleUnlimited.equals("F") ||
                kindleUnlimited.equals("N")) {
                livro.setKindleUnlimited(kindleUnlimited);
                break;
            } else if (kindleUnlimited.equals("SIM") ||
                       kindleUnlimited.equals("S") ||
                       kindleUnlimited.equals("T")){
                livro.setKindleUnlimited("TRUE");
                break;
            } else {
                System.out.println("Informe uma entrada válida, True ou False");
            }
        }
    }

    // Valida se a data informada é valida.
    public static void validarData(Livro livro){
        while (true) {
            try {
            String data = scan.nextLine();
            long aux;
            aux = Util.formatarData(data);
            System.out.println(aux);
            livro.setData(aux);
            break;
            } catch (Exception e){
                System.out.println("Formato de data inválido. Por favor, informe uma data para o livro no formato yyyy-mm-dd: ");
            }
        }
    }

}
