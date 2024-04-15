import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class CRUD {

    static Scanner scan = new Scanner(System.in);

    public static void menu() throws Exception {
        System.out.println("╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                   Bem-vindo à Base de Dados da                       ║");
        System.out.println("║                  Amazon Kindle Unlimited Books!                      ║");
        System.out.println("║        Explore uma vasta coleção de livros no Kindle Unlimited       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");

        RandomAccessFile arq = new RandomAccessFile("arquivo.db", "rw");
        CRUD crud = new CRUD();
        HashingEstendido h = new HashingEstendido(1, 9);
        HashingEstendido.preencherHashing(arq, h);
        ArvoreB arvoreB = new ArvoreB(10);
        ArvoreB.preencherArvore(arq, arvoreB);

        ListaInvertidaController.escreverArquivoListaTitulos();
        ListaInvertidaController.escreverArquivoListaCategorias();
        int opcao;

        do {
            System.out.println("\n╔═════════════════╗");
            System.out.println("║ +-- M E N U --+ ║");
            System.out.println("╚═════════════════╝");
            System.out.println("1. Adicionar Livro");
            System.out.println("2. Listar Livros");
            System.out.println("3. Buscar Livro");
            System.out.println("4. Atualizar Livro");
            System.out.println("5. Remover Livro");
            System.out.println("6. Buscar Termos no Título do Livro");
            System.out.println("7. Buscar Termos nas Categorias do Livro");
            System.out.println("8. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scan.nextInt();
            scan.nextLine();
            Livro livro;

            switch (opcao) {
                case 1:
                    // Adicionar Livro
                    long pos = arq.length();
                    crud.adicionarLivro(arq);
                    arq.seek(0);
                    int bookID = arq.readInt();

                    // Adicionar novo Livro à estrutura da arvoreB
                    livro = crud.buscarLivro(bookID, arq);
                    System.out.println(livro);
                    arvoreB.inserir(livro);

                    // Adicionar novo Livro à estrutura do hashing
                    h.inserir(bookID, pos);

                    /* Adicionar na lista invertida */
                    break;
                case 2:
                    // Listar Livros
                    crud.listarLivros(arq);
                    break;
                case 3:
                    int escolha = 0;
                    int id = 0;
                    do { // Buscar Livro

                        System.out.print("Digite o ID do livro que deseja buscar: ");
                        id = scan.nextInt();
                        System.out.println();
                        System.out.println("Selecione como deseja fazer a busca:");
                        System.out.println(
                                "1. Busca sequencial.\n2. Arvore B.\n3. Hashing.\n4. Lista Invertida - Título\n5. Lista Invetida - Categorias\n6. Sair");
                        escolha = scan.nextInt();
                        switch (escolha) {
                            case 1:
                                livro = crud.buscarLivro(id, arq);
                                if (livro != null) {
                                    System.out.println("\n+--Informações do livro--+");
                                    Livro.exibir(livro);
                                } else {
                                    System.out.println("\nLivro não encontrado. :(\r\n");
                                }
                                break;
                            case 2:
                                // Buscar via Arvore B.
                                livro = arvoreB.buscar(id);
                                Livro.exibir(livro);
                                break;
                            case 3:
                                // Buscar via hashing
                                livro = h.buscar(id);
                                Livro.exibir(livro);
                                break;
                            case 4:
                                // Buscar via Lista invertida -- titulo
                                System.out.print("Digite o termo que deseja buscar (para títulos): ");
                                String termo = scan.nextLine();
                                System.out.println();
                                ListaInvertidaController.buscarTermo(termo, "titulo");
                                break;
                            case 5:
                                // Buscar via Lista invertida -- categoria
                                System.out.print("Digite a categoria que deseja buscar (para categorias): ");
                                String categoria = scan.nextLine();
                                System.out.println();
                                ListaInvertidaController.buscarTermo(categoria, "categoria");
                                break;
                            case 6:
                                // Sair
                                System.out.println("Saindo do Menu Busca.");
                                break;
                            default:
                                System.out.println("Opção inválida. Tente novamente.");
                        }
                    } while (escolha != 6);

                    break;
                case 4:
                    // Atualizar livro
                    System.out.print("Digite o ID do livro que deseja atualizar: ");
                    id = scan.nextInt();
                    long posicaoInicial = Util.posicaoLivro(id, arq);
                    crud.atualizarLivro(id, arq);
                    long posicaoFinal = Util.posicaoLivro(id, arq);
                    if (posicaoFinal != posicaoInicial) {
                        // Atualizar nas estruturas de dados
                        h.update(id, posicaoFinal);
                        ListaInvertidaController.atualizar(posicaoInicial, posicaoFinal);
                    }
                    break;
                case 5:
                    // Remover livro
                    System.out.print("Digite o ID do livro que deseja remover: ");
                    id = scan.nextInt();

                    crud.removerLivro(id, arq);
                    arvoreB.remover(id);
                    h.deletar(id);
                    break;
                case 6:
                    // Buscar via Lista invertida -- titulo
                    System.out.print("Digite o termo que deseja buscar (para títulos): ");
                    String termo = scan.nextLine();
                    System.out.println();
                    ListaInvertidaController.buscarTermo(termo, "titulo");
                    break;
                case 7:
                    // Buscar via Lista invertida -- categoria
                    System.out.print("Digite a categoria que deseja buscar (para categorias): ");
                    String categoria = scan.nextLine();
                    System.out.println();
                    ListaInvertidaController.buscarTermo(categoria, "categoria");
                    break;
                    case 8:
                    // Sair
                    System.out.println("Saindo do Menu.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 8);

        arq.close();
    }

    void listarLivros(RandomAccessFile arq) throws IOException {
        arq.seek(0); // Move ponteiro para inicio do arquivo
        arq.readInt(); // Lê ultimo id (Quantidade de livros na base de dados)
        Livro livro = new Livro();

        // Percorre todo o arquivo exibindo livro por livro
        while (arq.getFilePointer() < arq.length()) {
            char lapide = arq.readChar();
            int tamanho = arq.readInt();

            if (lapide != '*') { // Checa se livro não foi removido ou atualizado por um registro maior
                byte[] ba = new byte[tamanho];
                arq.read(ba);
                livro = new Livro(ba);
                Livro.exibir(livro);
            } else { // Pula o registro do livro caso esteja marcado com a lápide
                arq.seek(arq.getFilePointer() + tamanho);
            }
        }
    }

    Livro buscarLivro(int id, RandomAccessFile arq) throws IOException {
        arq.seek(0);// Move ponteiro para inicio do arquivo
        arq.readInt();// Lê ultimo id (Quantidade de livros na base de dados)
        Livro livro = new Livro();
        boolean achou = false;

        // Percorre todo o arquivo até achar o livro de id informado
        while (arq.getFilePointer() < arq.length() && achou == false) {
            char lapide = arq.readChar();
            int tamanho = arq.readInt();

            if (lapide != '*') { // Checa se livro não foi removido ou atualizado por um registro maior
                byte[] ba = new byte[tamanho];
                arq.read(ba);
                livro = new Livro(ba);
                if (livro.getID() == id) // Achou o livro
                    achou = true;
            } else {// Pula o registro do livro caso esteja marcado com a lápide
                arq.seek(arq.getFilePointer() + tamanho);
            }
        }
        if (achou == false) // Retorna null caso não encontre o livro
            livro = null;
        return livro;
    }

    void adicionarLivro(RandomAccessFile arq) throws IOException {
        arq.seek(0);// Move ponteiro para inicio do arquivo
        int ultimoID = arq.readInt(); // Lê ultimo id (Quantidade de livros na base de dados)
        Livro livro = new Livro();

        livro.setId(ultimoID + 1); // Define ID para o novo livro como ultimoID + 1

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

        Util.escreverLivro(livro, arq); // Chama função para inserir novo livro
        System.out.println("\nLivro adicionado com sucesso!\n");
        Livro.exibir(livro);

    }

    void removerLivro(int id, RandomAccessFile arq) throws IOException {
        arq.seek(0);// Move ponteiro para inicio do arquivo
        arq.readInt();// Lê ultimo id (Quantidade de livros na base de dados)

        Livro livro = new Livro();
        boolean achou = false;
        // Percorre todo o arquivo até achar o livro de id informado
        while (arq.getFilePointer() < arq.length() && achou == false) {
            long posicaoLapide = arq.getFilePointer(); // Salva ponteiro para posição da lápide
            char lapide = arq.readChar();
            int tamanho = arq.readInt();

            if (lapide != '*') { // Checa se livro não foi removido ou atualizado por um registro maior
                byte[] ba = new byte[tamanho];
                arq.read(ba);
                livro = new Livro(ba);

                if (livro.getID() == id) {
                    arq.seek(posicaoLapide);
                    arq.writeChar('*'); // Adiciona marcador da lápide caso encontre o livro
                    System.out.println("\nLivro de id " + livro.getID() + " removido com sucesso!");
                    achou = true;
                }
            } else {// Pula o registro do livro caso esteja marcado com a lápide
                arq.seek(arq.getFilePointer() + tamanho);
            }
        }
        if (achou == false) {
            System.out.println("\nEsse livro não foi encontrado no arquivo.\n");
        }
    }

    void atualizarLivro(int id, RandomAccessFile arq) throws IOException {
        arq.seek(0);// Move ponteiro para inicio do arquivo
        long posicaoLivro = Util.posicaoLivro(id, arq); // Busca posição do livro de id informado
        arq.seek(posicaoLivro); // Move ponteiro para posição do livro na base de dados

        Livro livro = new Livro();
        char lapide = arq.readChar();
        int tamanho = arq.readInt();

        // Verifica lápide do livro a ser atualizado não está marcado e se encontrou o
        // livro
        if (lapide != '*' && posicaoLivro != 0) {
            byte[] ba = new byte[tamanho];
            arq.read(ba);
            livro = livro.fromByteArray(ba);
            System.out.println("\n");
            Livro.exibir(livro);

            System.out.println(
                    "\nQual campo do livro você deseja editar?\n1. Código\t2. Título\t3. Autor\t4. Avaliação\t5. Preço\t6. Kindle Unlimited\t7. Data \t8. Nome da Categoria");
            System.out.print("Escolha uma opção: ");
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

            arq.seek(posicaoLivro); // Se achou o livro para atualizar ele move novamente para a posição inicial do
                                    // registro deste livro
            ba = livro.toByteArray();
            if (ba.length > tamanho) {
                /*
                 * Se o novo registro for maior que o tamanho do registro atual do livro, o
                 * caracter de lápide
                 * é marcado no registro, move o ponteiro para o final do arquivo, escreve o
                 * lápide como não marcada,
                 * o tamanho do registro e o byteArray que é o próprio registro do livro
                 * atualizado
                 */
                arq.writeChar('*');
                arq.seek(arq.length());
                arq.writeChar(' ');
                arq.writeInt(ba.length);
                arq.write(ba);

                System.out.println("Livro movido para o final do arquivo\n");
            } else { // Caso contrário, não marca a lápide, insere o tamanhono registro e escreve o
                     // novo registro atualizado
                arq.writeChar(' ');
                arq.writeInt(tamanho);
                arq.write(ba);
                System.out.println("\nLivro atualizado com sucesso!");
            }
        } else {
            System.out.println("\nLivro não encontrado. :(\r\n");
        }

    }

    // Funções de validação

    // Valida a avaliação dada ao livro. Deve ser entre 1.00 e 5.00
    public static void validarAvalicao(Livro livro) {
        while (true) {
            try {
                float avaliacao = Float.parseFloat(scan.nextLine());
                if ((avaliacao >= 1 && avaliacao <= 5)) {
                    livro.setAvaliacao(avaliacao);
                    break;
                } else {
                    System.out.println("Avaliação inválida. Por favor, informe uma nota entre 1.00 e 5.00: ");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, informe um número válido entre 1.00 e 5.00: ");
            }
        }
    }

    // Valida se o preço informado é valido.
    public static void validarPreco(Livro livro) {
        while (true) {
            try {
                float preco = Float.parseFloat(scan.nextLine());
                livro.setPreco(preco);
                break;
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Entrada inválida. Por favor, informe um número válido");
            }
        }
    }

    // Valida se a entrada informada é true, false, sim ou não. Todas maíusculas ou
    // minúsculas
    public static void validarKindleUnlimited(Livro livro) {
        while (true) {
            String kindleUnlimited = scan.nextLine();
            kindleUnlimited = kindleUnlimited.toUpperCase();
            if (kindleUnlimited.equals("TRUE") ||
                    kindleUnlimited.equals("FALSE") ||
                    kindleUnlimited.equals("NAO") ||
                    kindleUnlimited.equals("F") ||
                    kindleUnlimited.equals("N")) {
                livro.setKindleUnlimited(Boolean.parseBoolean(kindleUnlimited));
                break;
            } else if (kindleUnlimited.equals("SIM") ||
                    kindleUnlimited.equals("S") ||
                    kindleUnlimited.equals("T")) {
                livro.setKindleUnlimited(true);
                break;
            } else {
                System.out.println("Informe uma entrada válida, True ou False");
            }
        }
    }

    // Valida se a data informada é valida.
    public static void validarData(Livro livro) {
        while (true) {
            try {
                String data = scan.nextLine();
                long aux;
                aux = Util.formatarData(data);
                livro.setData(aux);
                break;
            } catch (Exception e) {
                System.out.println(
                        "Formato de data inválido. Por favor, informe uma data para o livro no formato yyyy-mm-dd: ");
            }
        }
    }

}
