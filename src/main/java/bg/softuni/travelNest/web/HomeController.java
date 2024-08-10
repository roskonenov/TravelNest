package bg.softuni.travelNest.web;

import bg.softuni.travelNest.config.I18NConfig;
import bg.softuni.travelNest.config.Messages;
import bg.softuni.travelNest.service.TravelNestUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.jsf.FacesContextUtils;

import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final Messages messages;

    @GetMapping("/")
    public String showHome(@AuthenticationPrincipal UserDetails userDetails, Model model){
        if (userDetails instanceof TravelNestUserDetails travelNestUserDetails) {
            model.addAttribute("username", travelNestUserDetails.getUsername());
        }else {
            model.addAttribute("username", messages.get("header.section.guest"));
        }
        return "index";
    }

    @GetMapping("/about")
    public String showAbout(){
        return "about";
    }

    @GetMapping("/services")
    public String getServices(){
        return "services";
    }
}
