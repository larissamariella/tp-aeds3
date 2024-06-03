import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.charset.StandardCharsets;



public class LZW {
    RandomAccessFile arq;
    private Map<String, Integer> dictionary = new HashMap<String, Integer>();
    private Map<String, Integer> baseDictionary = new HashMap<String, Integer>();
    int backup;

    public LZW() throws Exception {
        comprimir();        
    }

    public void comprimir(){
        File file = new File("Backup/LZW/");
        int quantidadeArquivos = file.list().length;

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
        System.out.println("Tempo de execução - LZW: " + (tempofinal - tempoinicial)/ 1000 + "s");
    }

    public void comprimir(String nomeArquivo) throws IOException{

        String nomeFormatadoArquivo = nomeArquivo+".db";
        System.out.println("nome = "+nomeFormatadoArquivo);
        RandomAccessFile arq = new RandomAccessFile(nomeFormatadoArquivo, "rw");

        Map<String, Integer> dict = getDictionary();
        Map<String, Integer> baseDictionary = getBaseDictionary();

        createDictionary(arq, dict, baseDictionary);

        List<Integer> result = encoder(arq, dict);

        String arquivoCompactado = "Backup/LZW/"+nomeArquivo+"CompressaoLZW"+backup+".db";
        RandomAccessFile arqSaida = new RandomAccessFile(arquivoCompactado, "rw");

        writeResultFile(result, arqSaida);
        calculateCompressRatio(arq, arqSaida);

    }

    // private void printResult(List<Integer> result, Map<String, Integer> dictionary2) {
    //     for (String key : dictionary2.keySet()) {
    //         String msg = new String(key, "UTF-8");
    //     }
    // }

    
    
    public Map<String, Integer> getDictionary() {
        return dictionary;
    }

    public Map<String, Integer> getBaseDictionary() {
        return baseDictionary;
    }

    public List<Integer> encoder(RandomAccessFile arq, Map<String, Integer> dict) throws IOException {
        arq.seek(0);

        String atual;
        List<Integer> result = new ArrayList<>();
        String found = "";
        while (arq.getFilePointer() < arq.length()) {
            atual = Byte.toString(arq.readByte());
            String toAdd = found + atual;
            if (dict.containsKey(toAdd)) {
                found = toAdd;
            } else {
                result.add(dict.get(found));
                dict.put(toAdd, dict.size());
                found = atual;
            }
        }
        if (!found.isEmpty()) {
            result.add(dict.get(found));
        }
        return result;
    }


    public void decoder(List<Integer> result, Map<String, Integer> baseDictioary, String nomeArquivo) throws IOException {
        
        String nomeFormatadoArquivo = "Backup/LZW/"+nomeArquivo+"CompressaoLZW"+".db";
        RandomAccessFile decodedFile = new RandomAccessFile(nomeFormatadoArquivo, "rw");

        // RandomAccessFile decodedFile = new RandomAccessFile("decodedFile.db", "rw");

        Map<Integer, String> inverted = invertDictionary(baseDictioary);
        String first = inverted.get(result.remove(0)); // Retira primeiro número da lista resultado

        // System.out.println("first = " + first);
        decodedFile.writeByte((Integer.parseInt(first)));  // Escreve primeiro código

        while (result.size() > 0) {
            String newChar = inverted.get(result.remove(0));  // Retira próximo número/código da lista resultado

            // System.out.println("newChar = " + newChar);
                if (Integer.parseInt(newChar) < 256) { // Se código retirado for de somente um caracter
                    String resulString = first + newChar;
                    // System.out.println("resultString = " + resulString);
                    inverted.put(inverted.size(), first + newChar);

                    decodedFile.writeByte(Integer.parseInt(newChar));
                } else {
                    // Get smallest substring
                    int i = 0;
                    String subString = newChar.substring(i, i + 1);
                    while (Integer.parseInt(subString) < 256) {
                        i++;
                        subString += newChar.substring(i, i + 1);
                    }
                    subString = subString.substring(0, subString.length() - 1);
                    // System.out.println("subString = " + subString);
                    inverted.put(inverted.size(), first + subString);

                    decodedFile.writeByte(Integer.parseInt(subString));

                    String remainder = new String(newChar);
                    remainder = remainder.substring(subString.length());

                    if (Integer.parseInt(remainder) < 256) {
                        decodedFile.writeByte(Integer.parseInt(remainder));
                    } else {
                        writeRemainderString(remainder, decodedFile);
                    }
                }
            first = newChar;
        }
        // System.out.println("inverted = " + inverted + '\n');
    }

    public void writeRemainderString(String remainder, RandomAccessFile decodedFile) throws NumberFormatException, IOException {
        //System.out.println("remainder = " + remainder);
        int i = 0;
        String subString = remainder.substring(i, i + 1);
        while (Integer.parseInt(subString) < 256 && i < remainder.length()) {
            i++;
            subString += remainder.substring(i, i + 1);
        }
        subString = subString.substring(0, subString.length() - 1);
        //System.out.println("subString = " + subString);

        decodedFile.writeByte(Integer.parseInt(subString));

        remainder = remainder.substring(subString.length());

        if (Integer.parseInt(remainder) < 256) {
            decodedFile.writeByte(Integer.parseInt(remainder));
        } else {
            writeRemainderString(remainder, decodedFile);
        }
    }
    // public String getSmallestSubString(String newChar) {
    //     int i = 0;
    //     String subString = newChar.substring(i, i + 1);
    //     while (Integer.parseInt(subString) < 256) {
    //         subString += newChar.substring(i, i + 1);
    //         i++;
    //     }
    //     subString = subString.substring(0, subString.length() - 1);
    //     System.out.println("subString = " + subString);
    //     inverted.put(inverted.size(), first + subString);

    //     decodedFile.writeByte(Integer.parseInt(subString));
    // }

    public Map<Integer, String> invertDictionary(Map<String, Integer> baseDictionary) {
        Map<Integer, String> inverted = new HashMap<Integer, String>();
        for (String key : baseDictionary.keySet()) {
            inverted.put(baseDictionary.get(key), key);
        }
        // System.out.println("inverted = " + inverted + '\n');
        return inverted;
    }

    public void createDictionary(RandomAccessFile arq, Map<String, Integer> dict, Map<String, Integer> baseDictionary)
            throws IOException {
        arq.seek(0);
        while (arq.getFilePointer() < arq.length()) {
            Byte readedByte = arq.readByte();
            String atual = Byte.toString(readedByte);
            // System.out.println("String: " + byteToString(readedByte) + ". Byte: " + readedByte);
            if (!dict.containsKey(atual)) {
                dict.put(atual, dict.size());
                baseDictionary.put(atual, baseDictionary.size());
            }
        }
    }

    public String byteToString(byte b) {
        String atual = Byte.toString(b);
        Integer toPrint = Integer.parseInt(atual);
        int number = toPrint;
        return String.valueOf((char) number);
    }



    public void writeResultFile(List<Integer> result, RandomAccessFile destinationFile) throws IOException {
        for (Integer item : result) {
            destinationFile.writeByte(item);
        }
    }

    public void calculateCompressRatio(RandomAccessFile arq, RandomAccessFile arqSaida) throws IOException{
        double porcentagemDePerda = (double) (arqSaida.length() * 100) / arq.length();
        double taxaCompressao = (100- porcentagemDePerda);
        System.out.println("Taxa de compressão - LZW: " + (taxaCompressao) + "%");
        arqSaida.close();
        arq.close();
    }
}
