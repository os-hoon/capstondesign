package cherrysumer.cherrysumer.util;

public class MailCodeMessage {

    public static String mailContent(String code) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"ko\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>이메일 인증 번호</title>\n" +
                "</head>\n" +
                "<body style=\"margin: 0; padding: 0;\">\n" +
                "    <div style=\"width: 100%; text-align: center; background-color: #f9f9f9; padding: 20px 0;\">\n" +
                "        <div style=\"display: inline-block; text-align: center; background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">\n" +
                "            <p style=\"margin: 0 0 10px 0;\">요청하신 인증 번호 입니다.</p>\n" +
                "            <div class=\"code\" style=\"font-size: 28px; font-weight: bold; color: #FF5959; margin: 10px 0;\">" + code + "</div>\n" +
                "            <p style=\"margin: 0 0 10px 0;\">위 인증 번호 6자리를 입력 화면에 입력해주세요.</p>\n" +
                "            <p style=\"margin: 0 0 10px 0;\">해당 인증 번호는 10분 뒤 만료됩니다.</p>\n" +
                "            <p style=\"margin: 0;\">감사합니다.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }
}