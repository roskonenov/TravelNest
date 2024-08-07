package bg.softuni.travelNest.web;

import bg.softuni.travelNest.model.dto.RegisterDto;
import bg.softuni.travelNest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("registerData")
    public RegisterDto registerDto(){
        return new RegisterDto();
    }

    @GetMapping("/login")
    public String showLogin(){
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("showError", true);
        return "login";
    }

    @GetMapping("/register")
    public String showRegister(){
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterDto registerDto, BindingResult bindingResult, RedirectAttributes rAttr){

        if (bindingResult.hasErrors()){

            rAttr.addFlashAttribute("registerData", registerDto);
            rAttr.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);

           return "redirect:/users/register";
        }

        boolean success = userService.register(registerDto);

        if (!success) {
            rAttr.addFlashAttribute("registerData", registerDto);
            rAttr.addFlashAttribute("userOrEmailExist", true);
            return "redirect:/users/register";
        }

        return "redirect:/";
    }
}
