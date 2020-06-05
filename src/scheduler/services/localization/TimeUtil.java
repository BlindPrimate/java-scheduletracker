package scheduler.services.localization;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static final ZoneId UTC = ZoneId.of("UTC");
    public static final ZoneId USER_ZONE = ZoneId.systemDefault();
    public static final DateTimeFormatter STANDARD_CALENDAR = DateTimeFormatter.ofPattern("yy-MM-dd");
    public static final DateTimeFormatter STANDARD12HOUR = DateTimeFormatter.ofPattern("hh:mm a");

    public static LocalDateTime getUTC() {
        return LocalDateTime.now().atZone(USER_ZONE).withZoneSameInstant(UTC).toLocalDateTime();

    }
    public static LocalDateTime toUTC(LocalDateTime time) {
        return time.atZone(USER_ZONE).withZoneSameInstant(UTC).toLocalDateTime();
    }

    public static LocalDateTime toUserTime(LocalDateTime time) {
        return time.atZone(UTC).withZoneSameInstant(USER_ZONE).toLocalDateTime();
    }

}
