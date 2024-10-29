package cherrysumer.cherrysumer.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ConvertDate {

    public static String convertDate(LocalDateTime date) {
        // 연도.월.일 형식
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yy.MM.dd");

        // 요일 가져오기
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String dayOfWeekKorean = dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.KOREAN);

        // 시각 부분 포맷
        String amPm = date.getHour() >= 12 ? "오후" : "오전";
        int hour = date.getHour() % 12 == 0 ? 12 : date.getHour() % 12;
        String formattedTime = String.format("%s %d:%02d", amPm, hour, date.getMinute());

        // 최종 문자열 조합
        String formattedDate = date.format(dateFormatter) + "." + dayOfWeekKorean + " " + formattedTime;
        return formattedDate;
    }
}
