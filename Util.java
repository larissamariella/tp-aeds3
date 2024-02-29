import java.time.*;
import java.time.format.DateTimeFormatter;

public class Util {
        public static long formatarData(String dateString) {
        DateTimeFormatter mascara = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate data = LocalDate.parse(dateString, mascara);
        Instant instant = data.atStartOfDay().toInstant(ZoneOffset.UTC);
        return instant.getEpochSecond();
    }
}
