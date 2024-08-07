package bg.softuni.travelNest.web;

import bg.softuni.travelNest.model.dto.RentDTO;
import bg.softuni.travelNest.service.TravelNestUserDetails;
import bg.softuni.travelNest.service.RentService;
import bg.softuni.travelNest.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class RentController {

    private final UserService userService;
    private final RentService rentService;

    @ModelAttribute("rentData")
    public RentDTO rentDTO() {
        return new RentDTO();
    }

    @PostMapping("/{entityType}/rent/{uuid}")
    public String rentHousing(@PathVariable("uuid") UUID propertyId,
                              @PathVariable("entityType") String entityType,
                              @AuthenticationPrincipal TravelNestUserDetails travelNestUserDetails,
                              @RequestParam("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
                              @RequestParam("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate,
                              RentDTO rentDTO,
                              RedirectAttributes rAttr) {

        rentDTO.setRenter(userService.findUser(travelNestUserDetails));
        rentDTO.setId(propertyId);

        String message = rentService.rent(rentDTO, entityType);

        rAttr.addFlashAttribute("message", message);

        return "redirect:/" + entityType + "/details/" + propertyId;
    }
}
