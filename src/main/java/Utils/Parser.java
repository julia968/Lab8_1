package Utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Parser {
    public static LocalDate convertTimeStampToLocalDate(Timestamp timestamp) {
        ZoneId zoneId = ZoneId.systemDefault();
        return timestamp.toLocalDateTime().atZone(zoneId).toLocalDate();
    }
    public static Timestamp parseLocalDateToTimestamp(LocalDate localDate) {
        return Timestamp.valueOf(localDate.atStartOfDay());
    }
}
