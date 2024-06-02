import java.util.ArrayList;

public class ListaInvertida {
    private String termo;
    private ArrayList<Long> listaDeIds; // grava as posições dos livros com aquele termo no arquivo

    public ListaInvertida() {
        this.termo = "";
        this.listaDeIds = new ArrayList<Long>();
    }

    public ListaInvertida(String termo, long posicao) {
        this.termo = termo;
        this.listaDeIds = new ArrayList<Long>();
        this.listaDeIds.add(posicao);
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public ArrayList<Long> getPosicao() {
        return listaDeIds;
    }

    public void setPosicao(long posicao) {
        this.listaDeIds.add(posicao);
    }
}