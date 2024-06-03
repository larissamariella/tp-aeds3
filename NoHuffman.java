public class NoHuffman {
    byte caracter;
    int frequencia;
    NoHuffman esq;
    NoHuffman dir;

    public NoHuffman() {
        this.caracter = ' ';
        this.frequencia = 0;
        this.esq = null;
        this.dir = null;
    }

    public NoHuffman(byte caracter, int frequencia) {
        this.caracter = caracter;
        this.frequencia = frequencia;
        this.esq = null;
        this.dir = null;
    }

    @Override
    public String toString() {
        return "NoHuffman [caracter=" + caracter + ", frequencia=" + frequencia + "]";
    }
}