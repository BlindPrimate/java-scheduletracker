package scheduler.services.localization;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtil {
    public static final ZoneId UTC = ZoneId.of("UTC");
    public static final ZoneId userZone = ZoneId.systemDefault();

    public static LocalDateTime getUTC() {
        return LocalDateTime.now().atZone(userZone).withZoneSameInstant(UTC).toLocalDateTime();

    }
    public static LocalDateTime toUTC(LocalDateTime time) {
        return time.atZone(userZone).withZoneSameInstant(UTC).toLocalDateTime();
    }

    public static LocalDateTime toUserTime(LocalDateTime time) {
        return time.atZone(UTC).withZoneSameInstant(userZone).toLocalDateTime();
    }

}
