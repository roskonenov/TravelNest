package bg.softuni.travelNest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ContactController {


    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

    @PostMapping("/contact")
    public String sendMessage(){
        return "message_confirm";
    }
}
