package bg.softuni.travelNest.web;

import bg.softuni.travelNest.model.dto.RentDTO;
import bg.softuni.travelNest.service.CurrentUser;
import bg.softuni.travelNest.service.RentService;
import bg.softuni.travelNest.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/house/rent/{uuid}")
    public String rentHousing(@PathVariable("uuid") UUID housingId,
                              @AuthenticationPrincipal CurrentUser currentUser,
                              @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                              RentDTO rentDTO,
                              RedirectAttributes rAttr) {

        rentDTO.setRenter(userService.findUser(currentUser));
        rentDTO.setId(housingId);

        String message = rentService.rent(rentDTO);

        rAttr.addFlashAttribute("message", message);

        return "redirect:/house/details/" + housingId;
    }
}
