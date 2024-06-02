import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LerByte {

    int backup = 0;
    // Faz a leitura completa do livro, char de lápide, int do tamanho e array de
    // bytes
    public static Livro lerLivro(RandomAccessFile arq) throws IOException {
        Livro livro = new Livro();

        char lapide = arq.readChar();
        int tamanho = arq.readInt();

        if (lapide != '*') {
            byte[] ba = new byte[tamanho];
            arq.read(ba);
            livro = livro.fromByteArray(ba);
        } else {
            livro = null;
        }
        return livro;
    }

    public void comprimir(){
        String [] arquivos = {"arquivo", "tituloLivro", "categoriasLivro"};
        for (String arquivo : arquivos) {
            try {
                comprimir(arquivo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        backup++;
    }

    public void comprimir(String nomeArquivo) throws IOException {

            String nomeFormatadoArquivo = nomeArquivo+".db";
            System.out.println("nome"+nomeFormatadoArquivo);

            RandomAccessFile arq = new RandomAccessFile(nomeFormatadoArquivo, "rw");
            HashMap<Byte, Integer> map = new HashMap<Byte, Integer>();

            System.out.println("percorre o inicio");
            while (arq.getFilePointer() < arq.length()) {
                byte b = arq.readByte();

                if (map.containsKey(b)) {
                    map.put(b, map.get(b) + 1);
                } else {
                    map.put(b, 1);
                }
                
            }

            System.out.println("faz a lista");
            ArrayList<No> lista = new ArrayList<No>();
            for (byte chave : map.keySet()) {
                lista.add(new No(chave, map.get(chave)));
            }
            System.out.println("inicio arvore");

            Arvore arvore = new Arvore();
            arvore.inserirHuffman(lista);
            System.out.println("Arvore criada");

            System.out.println("Dicionario: ");
            HashMap<Byte, String> dicionario = new HashMap<Byte, String>();
            arvore.percorrer(arvore.raiz,dicionario, "");
 
            ArraydeBits bits = new ArraydeBits();
            bits.adicionar(dicionario.size());
            System.out.println("adciona no bitset");
            for (byte chave : dicionario.keySet()) {
                bits.adicionar(chave);
                bits.adicionar((byte)dicionario.get(chave).length());
                for (int i = 0; i < dicionario.get(chave).length(); i++) {
                    if (dicionario.get(chave).charAt(i) == '0') {
                        bits.adicionar(false);
                    } else {
                        bits.adicionar(true);
                    }
                }
            }
    
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

            String arquivoCompactado = "Backup/Huffman/"+nomeArquivo+"HuffmanCompressao"+backup+".db";
            RandomAccessFile arqSaida = new RandomAccessFile(arquivoCompactado, "rw");
            byte [] ba = bits.toByteArray();
            arqSaida.write(ba);
            arqSaida.close();
            arq.close();
         System.out.println("fim compactacao");
    }


    public void decodificar(int versaoBackup){
        String [] arquivos = {"arquivo", "tituloLivro", "categoriasLivro"};
        for (String arquivo : arquivos) {
            try {
                    decodificar(arquivo, versaoBackup);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void decodificar(String nomeArquivo, int versaoBackup) throws IOException{
        
            String nomeFormatadoArquivo = "Backup/Huffman/"+nomeArquivo+"HuffmanCompressao"+versaoBackup+".db";
            System.out.println("nome-decodificar: "+nomeFormatadoArquivo);
            System.out.println("inicio descompactacao");
            RandomAccessFile arq = new RandomAccessFile(nomeFormatadoArquivo, "rw");
            arq.seek(0);
            HashMap<String, Byte> dicionario = new HashMap<String, Byte>();
            ArraydeBits bits = new ArraydeBits();
            byte[] ba = new byte[(int)arq.length()];
            arq.read(ba);
            bits.fromByteArray(ba);
            int tamanhoDicionario = bits.getInt(0);
            int posicao = 32;
            System.out.println("inicio dicionario");
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

            System.out.println("inicio novo arquivo descompactado");
            String nomeArqDescomprimido = nomeArquivo+".db";
            RandomAccessFile arqDescomprimido = new RandomAccessFile(nomeArqDescomprimido, "rw");
            arqDescomprimido.seek(0);
            String codigo = "";
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
            System.out.println("fim descompactacao");
    }
}