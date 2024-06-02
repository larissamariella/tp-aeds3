public class Huffman {
    public class No {
        char caracter;
        int frequencia;
        No esq;
        No dir;

        public No(char caracter, int frequencia){
            this.caracter = caracter;
            this.frequencia = frequencia;
            this.esq = null;
            this.dir = null;
        }
    }

    public class Arvore {
        No raiz;

        public Arvore() {
            raiz = null;
        }

        public void inserir(String texto){
            No [] vetor = new No[texto.length()];
            int pos = 0;

            for(int i=0; i<texto.length(); i++){
                char caracter = texto.charAt(i);

                for(int j=0; j<vetor.length; j++){
                    if(vetor[j].caracter != caracter){
                        vetor[pos] = new No(caracter, 0);
                        pos++;
                    }
                    if(texto.charAt(i) == texto.charAt(j)){
                        vetor[pos].frequencia++;
                    }
                }
            }

            mostrarElementos(vetor);
            
        }

        public void mostrarElementos(No [] vetor){
            for(int i=0; i<vetor.length; i++){
                System.out.println(vetor[i].caracter + " - " + vetor[i].frequencia);
            }
        }

        public void inserirArvore(){

        }


    }
    
}
