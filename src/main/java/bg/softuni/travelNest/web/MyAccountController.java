package bg.softuni.travelNest.web;

import bg.softuni.travelNest.config.MyAccountConfig;
import bg.softuni.travelNest.service.JWTService;
import bg.softuni.travelNest.service.TravelNestUserDetails;
import bg.softuni.travelNest.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MyAccountController {

    private final JWTService jwtService;
    private final UserService userService;
    private final MyAccountConfig myAccountConfig;

    @GetMapping("/my-account")
    public void redirectToMyAccount(HttpServletResponse response) throws IOException {
        Optional<TravelNestUserDetails> currentUser = userService.getCurrentUser();

        if (currentUser.isPresent()) {

            String jwtToken = jwtService.generateToken(currentUser.get().getUuid().toString(), currentUser.get().setClaims());
            System.out.println(jwtToken);
            String redirectUrl = myAccountConfig.getBaseUrl() + "?token=" + jwtToken;
            response.sendRedirect(redirectUrl);
        }
   }
}
