import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Util {
        public static long formatarData(String dateString) {
        DateTimeFormatter mascara = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate data = LocalDate.parse(dateString, mascara);
        Instant instant = data.atStartOfDay().toInstant(ZoneOffset.UTC);
        return instant.getEpochSecond();
    }

    public static void escreverLivro(Livro livro, RandomAccessFile arq) throws IOException {
        arq.seek(0);
        arq.writeInt(livro.id);
        arq.seek(arq.length());

        byte[] ba = livro.toByteArray();
        arq.writeChar('-');
        arq.writeInt((ba.length));
        arq.write(ba);
    }
}
