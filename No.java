public class No {
    byte caracter;
    int frequencia;
    No esq;
    No dir;

    public No() {
        this.caracter = ' ';
        this.frequencia = 0;
        this.esq = null;
        this.dir = null;
    }

    public No(byte caracter, int frequencia) {
        this.caracter = caracter;
        this.frequencia = frequencia;
        this.esq = null;
        this.dir = null;
    }

    @Override
    public String toString() {
        return "No [caracter=" + caracter + ", frequencia=" + frequencia + "]";
    }

}