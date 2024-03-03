public class LoadingAnimation implements Runnable {
    private volatile boolean running = true;
    private int dotsCount = 0;

    @Override
    public void run() {
        System.out.print("Aguarde, a base de dados está sendo carregada");
        while (running) {
            try {
                Thread.sleep(500); // Intervalo de atualização da animação
                updateDots();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }

    private void updateDots() {
        if (dotsCount == 3) {
            dotsCount = 0;
            System.out.print("\b\b\b   "); // Apaga os três pontos anteriores
            System.out.print("\b\b\b");   // Move o cursor de volta três posições
        } else {
            dotsCount++;
            System.out.print(".");
        }
    }

    public void stop() {
        running = false;
        System.out.flush();
    }
}
