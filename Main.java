import java.io.File;
public class Main {
    public static void main(String[] args) {
        try{
            File arquivo = new File("arquivo.db");
            LerCSV csv = new LerCSV();
            if (!arquivo.exists()) {
                //Inicializa Thread para animação de carregamento da base de dados
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
                        e.printStackTrace();
                    }
                }
            }
            CRUD.menu();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}