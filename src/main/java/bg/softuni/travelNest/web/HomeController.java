package bg.softuni.travelNest.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/services")
    public String getServices(){
        return "services";
    }

    @GetMapping("/projects")
    public String getProjects(){
        return "projects";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }

    @GetMapping("/")
    public String showHome(){
        return "index";
    }

    @GetMapping("/testimonial")
    public String testimonial(){
        return "testimonial";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/about")
    public String showAbout(){
        return "about";
    }
}
