package bg.softuni.travelNest.web;

import bg.softuni.travelNest.service.TravelNestUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/services")
    public String getServices(){
        return "services";
    }

    @GetMapping("/")
    public String showHome(@AuthenticationPrincipal UserDetails userDetails, Model model){
        if (userDetails instanceof TravelNestUserDetails travelNestUserDetails) {
            model.addAttribute("username", travelNestUserDetails.getUsername());
        }else {
            model.addAttribute("username", "guest");
        }
        return "index";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

    @GetMapping("/about")
    public String showAbout(){
        return "about";
    }
}
