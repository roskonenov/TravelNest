package bg.softuni.travelNest.web;

import bg.softuni.travelNest.model.dto.EmailMessageDTO;
import bg.softuni.travelNest.service.EmailService;
import bg.softuni.travelNest.service.TravelNestUserDetails;
import bg.softuni.travelNest.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final UserService userService;

    private final EmailService emailService;

    @ModelAttribute("messageData")
    public EmailMessageDTO emailMessageDTO() {
        return new EmailMessageDTO();
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @PostMapping("/contact")
    public String sendMessage(@AuthenticationPrincipal TravelNestUserDetails travelNestUserDetails,
                              @Valid EmailMessageDTO emailMessageDTO,
                              BindingResult bindingResult,
                              RedirectAttributes rAttr) {
        if (bindingResult.hasErrors()) {
            rAttr.addFlashAttribute("messageData", emailMessageDTO);
            rAttr.addFlashAttribute("org.springframework.validation.BindingResult.messageData", bindingResult);
            return "redirect:/contact";
        }
        emailService.sendEmail(emailMessageDTO, userService.findUser(travelNestUserDetails));
        return "message_confirm";
    }
}
