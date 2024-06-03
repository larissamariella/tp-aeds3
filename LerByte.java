import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LerByte {
    // versao do backup
    int backup;
   static double taxaCompressao;

    public void comprimir() {
        // seleciona a pasta de todos os arquivos que precisam ser comprimidos
        File file = new File("Backup/Huffman/");
        int quantidadeArquivos = file.list().length;
        System.out.println("quantidade de arquivos: " + quantidadeArquivos);


        long tempoinicial = System.currentTimeMillis();
        String[] arquivos = { "arquivo", "tituloLivro", "categoriasLivro", "hashing", "dir", "arqArvoreB"};
        backup = quantidadeArquivos / arquivos.length;
        for (String arquivo : arquivos) {
            try {
                comprimir(arquivo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long tempofinal = System.currentTimeMillis();
        System.out.println("Tempo de execução - Huffman: " + (tempofinal - tempoinicial)/ 1000 + "s");
    }

    // metodo que le o arquivo, cria o dicionario e a arvore de huffman
    public void comprimir(String nomeArquivo) throws IOException {

        String nomeFormatadoArquivo = nomeArquivo + ".db";
        System.out.println("nome" + nomeFormatadoArquivo);

        RandomAccessFile arq = new RandomAccessFile(nomeFormatadoArquivo, "rw");
        HashMap<Byte, Integer> map = new HashMap<Byte, Integer>();

        while (arq.getFilePointer() < arq.length()) {
            byte b = arq.readByte();

            if (map.containsKey(b)) {
                map.put(b, map.get(b) + 1);
            } else {
                map.put(b, 1);
            }
        }
        //lista dos nos da arvore
        ArrayList<NoHuffman> lista = new ArrayList<NoHuffman>();
        for (byte chave : map.keySet()) {
            lista.add(new NoHuffman(chave, map.get(chave)));
        }

        //cria a arvore com o unico no que restou (Raiz)
        ArvoreHuffman arvore = new ArvoreHuffman();
        arvore.inserirHuffman(lista);

        // monta o dicionario
        HashMap<Byte, String> dicionario = new HashMap<Byte, String>();
        arvore.percorrer(arvore.raiz, dicionario, "");

        ArraydeBits bits = new ArraydeBits();
        bits.adicionar(dicionario.size());

        // busca o codigo de cada caracter no dicionario
        for (byte chave : dicionario.keySet()) {
            bits.adicionar(chave);
            bits.adicionar((byte) dicionario.get(chave).length());
            for (int i = 0; i < dicionario.get(chave).length(); i++) {
                if (dicionario.get(chave).charAt(i) == '0') {
                    bits.adicionar(false);
                } else {
                    bits.adicionar(true);
                }
            }
        }

        // escreve o novo arquivo no byte array
        arq.seek(0);
        for (int i = 0; i < arq.length(); i++) {
            String codigo = dicionario.get(arq.readByte());
            for (int j = 0; j < codigo.length(); j++) {
                if (codigo.charAt(j) == '0') {
                    bits.adicionar(false);
                } else {
                    bits.adicionar(true);
                }
            }
        }

        // escreve no arquivo e calcula a porcentagem de perda (compressao)
        String arquivoCompactado = "Backup/Huffman/" + nomeArquivo + "HuffmanCompressao" + backup + ".db";
        RandomAccessFile arqSaida = new RandomAccessFile(arquivoCompactado, "rw");
        byte[] ba = bits.toByteArray();
        arqSaida.write(ba);

        double porcentagemDePerda = (double) (arqSaida.length() * 100) / arq.length();
        taxaCompressao = (100- porcentagemDePerda);
        System.out.println("Taxa de compressão - Huffman: " + (taxaCompressao) + "%");
     
        arqSaida.close();
        arq.close();
    }

    // seleciona os arquivos que precisam ser descomprimidos
    public void decodificar(int versaoBackup) {
        String[] arquivos = { "arquivo", "tituloLivro", "categoriasLivro", "hashing", "dir", "arqArvoreB"};
        for (String arquivo : arquivos) {
            try {
                long tempoinicial = System.currentTimeMillis();
                decodificar(arquivo, versaoBackup);
                long tempofinal = System.currentTimeMillis();
                System.out.println("Tempo de descompactação " + arquivo + " :" + (tempofinal - tempoinicial)/ 1000 + "s");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }

    public void decodificar(String nomeArquivo, int versaoBackup) throws IOException {

        String nomeFormatadoArquivo = "Backup/Huffman/" + nomeArquivo + "HuffmanCompressao" + versaoBackup + ".db";

        RandomAccessFile arq = new RandomAccessFile(nomeFormatadoArquivo, "rw");
        arq.seek(0);

        // le o dicionario que esta no inicio do arquivo
        HashMap<String, Byte> dicionario = new HashMap<String, Byte>();
        ArraydeBits bits = new ArraydeBits();
        byte[] ba = new byte[(int) arq.length()];
        arq.read(ba);
        bits.fromByteArray(ba);
        int tamanhoDicionario = bits.getInt(0);
        int posicao = 32;

        for (int i = 0; i < tamanhoDicionario; i++) {
            byte chave = bits.getByte(posicao);
            posicao += 8;
            int tamanho = bits.getByte(posicao);
            posicao += 8;
            String codigo = "";
            for (int j = 0; j < tamanho; j++) {
                if (bits.get(posicao)) {
                    codigo += "1";
                } else {
                    codigo += "0";
                }
                posicao++;
            }
            dicionario.put(codigo, chave);
        }

        String nomeArqDescomprimido = nomeArquivo + ".db";
        RandomAccessFile arqDescomprimido = new RandomAccessFile(nomeArqDescomprimido, "rw");
        arqDescomprimido.seek(0);
        String codigo = "";
        // le o arquivo e pesquisa no dicionario o seu codigo, montando novamente o arquivo descomprimido
        for (int i = posicao; i < bits.tamanho; i++) {
            if (bits.get(i)) {
                codigo += "1";
            } else {
                codigo += "0";
            }
            if (dicionario.containsKey(codigo)) {
                arqDescomprimido.writeByte(dicionario.get(codigo));
                codigo = "";
            }
        }
        arqDescomprimido.close();
    }
}