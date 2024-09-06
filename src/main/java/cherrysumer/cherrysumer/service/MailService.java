package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.web.dto.MailRequestDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.security.NoSuchAlgorithmException;

public interface MailService {
    void sendMail(String email, String title, String content);

    void sendCode(String email) throws NoSuchAlgorithmException, MessagingException;

    void checkCode(MailRequestDTO.verificationRequestDTO request);
}
