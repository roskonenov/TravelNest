package bg.softuni.travelNest.web;

import bg.softuni.travelNest.model.dto.PropertyDTO;
import bg.softuni.travelNest.service.JWTService;
import bg.softuni.travelNest.service.TravelNestUserDetails;
import bg.softuni.travelNest.service.UserService;
import bg.softuni.travelNest.service.impl.HousingServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MyAccountController {

    private final JWTService jwtService;
    private final UserService userService;
    private final HousingServiceImpl housingServiceImpl;

    @GetMapping("/get-user-favorites")
    public ResponseEntity<List<PropertyDTO>> getUserFavorites(HttpServletResponse response){
        Optional<TravelNestUserDetails> currentUser = userService.getCurrentUser();

        if (currentUser.isPresent()) {

            String jwtToken = jwtService.generateToken(currentUser.get().getUuid().toString(), currentUser.get().setClaims());
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
            response.setHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON));
            System.out.println(jwtToken);
            List<PropertyDTO> responseBody = housingServiceImpl.findUserFavorites(currentUser.get().getUuid());
            return ResponseEntity.ok(responseBody);
        }
       return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
   }
}
