package cherrysumer.cherrysumer.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtil {

    private static class TIME {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    public static String convertTime(LocalDateTime upload) {
        LocalDateTime now = LocalDateTime.now();

        if(ChronoUnit.SECONDS.between(upload, now) < TIME.SEC) {
            return ChronoUnit.SECONDS.between(upload, now) + "초 전";
        } else if(ChronoUnit.MINUTES.between(upload, now) < TIME.MIN) {
            return ChronoUnit.MINUTES.between(upload, now) + "분 전";
        } else if(ChronoUnit.HOURS.between(upload, now) < TIME.HOUR) {
            return ChronoUnit.HOURS.between(upload, now) + "시간 전";
        } else if(ChronoUnit.DAYS.between(upload, now) < TIME.DAY) {
            return ChronoUnit.DAYS.between(upload, now) + "일 전";
        } else if(ChronoUnit.MONTHS.between(upload, now) < TIME.MONTH) {
            return ChronoUnit.MONTHS.between(upload, now) + "달 전";
        } else {
            return ChronoUnit.YEARS.between(upload, now) + "년 전";
        }
    }
}
