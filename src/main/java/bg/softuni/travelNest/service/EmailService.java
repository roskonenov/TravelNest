package bg.softuni.travelNest.service;

import bg.softuni.travelNest.model.dto.EmailMessageDTO;
import bg.softuni.travelNest.model.entity.User;

public interface EmailService {

    void sendEmail(EmailMessageDTO emailMessageDTO, User user);
}
