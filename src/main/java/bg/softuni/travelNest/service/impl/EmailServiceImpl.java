package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.model.dto.EmailMessageDTO;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(EmailMessageDTO emailMessageDTO, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("roskonenov@gmail.com");
        message.setTo("roskonenov@gmail.com");
        message.setSubject("Message from user " + user.getUsername());
        message.setText(String.format("User %s and name %s, with email: %s and phone number %d, sent message: %s",
                user.getUsername(),
                emailMessageDTO.getName(),
                emailMessageDTO.getEmail(),
                emailMessageDTO.getNumber(),
                emailMessageDTO.getText()));

        javaMailSender.send(message);
    }
}
