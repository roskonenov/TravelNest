package bg.softuni.travelNest.web;

import bg.softuni.travelNest.model.dto.AddCommentDTO;
import bg.softuni.travelNest.model.dto.AddRentalPropertyDTO;
import bg.softuni.travelNest.model.enums.City;
import bg.softuni.travelNest.service.CurrentUser;
import bg.softuni.travelNest.service.HousingService;
import bg.softuni.travelNest.service.RentService;
import bg.softuni.travelNest.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/house")
public class HousingController {

    private final HousingService housingService;
    private final UserService userService;
    private final RentService rentService;

    @ModelAttribute("cities")
    public List<String> cities() {
        return Arrays.stream(City.values())
                .map(City::toString)
                .toList();
    }

    @ModelAttribute("addRentalData")
    public AddRentalPropertyDTO rentalPropertyDTO() {
        return new AddRentalPropertyDTO();
    }

    @ModelAttribute("commentData")
    public AddCommentDTO addCommentDTO() {
        return new AddCommentDTO();
    }

    @GetMapping("/rental")
    public String showRentalHousing(Model model) {
        model.addAttribute("housingData", housingService.findAllAdds());
        return "housing_rental";
    }

    @GetMapping("/details/{uuid}")
    public String showDetails(@PathVariable("uuid") UUID housingId,
                              @AuthenticationPrincipal CurrentUser currentUser,
                              Model model) {

        if (userService.isFavorite(currentUser, housingId)) {
            model.addAttribute("isFavorite", true);
        }
        model.addAttribute("rentPeriods", rentService.getHousingRentPeriods(housingId));
        model.addAttribute("housingDetails", housingService.findDetailsById(housingId));
        return "housing_details";
    }

    @PostMapping("/details/{uuid}")
    public String addComment(@PathVariable("uuid") UUID housingId,
                             @AuthenticationPrincipal CurrentUser currentUser,
                             @Valid AddCommentDTO addCommentDTO,
                             BindingResult bindingResult,
                             RedirectAttributes rAttr) {

        if (bindingResult.hasErrors()) {
            rAttr.addFlashAttribute("commentData", addCommentDTO);
            rAttr.addFlashAttribute("org.springframework.validation.BindingResult.commentData", bindingResult);
            return "redirect:/house/details/" + housingId;
        }

        housingService.addComment(addCommentDTO, housingId, userService.findUser(currentUser));
        return "redirect:/house/details/" + housingId;
    }

    @GetMapping("/add")
    public String showAddHousing() {
        return "add_housing";
    }

    @PostMapping("/add")
    public String addHousing(@Valid AddRentalPropertyDTO addRentalPropertyDTO,
                             @RequestParam("image") MultipartFile image,
                             @AuthenticationPrincipal CurrentUser currentUser,
                             BindingResult bindingResult,
                             RedirectAttributes rAttr) throws IOException {

        if (bindingResult.hasErrors()) {
            rAttr.addFlashAttribute("addRentalData", addRentalPropertyDTO);
            rAttr.addFlashAttribute("org.springframework.validation.BindingResult.addRentalData", bindingResult);
            return "redirect:/house/add";
        }
        return "redirect:/house/details/" + housingService.add(addRentalPropertyDTO, image, currentUser);
    }

    @PostMapping("/add-to-favorites/{uuid}")
    public String addToFavorites(@PathVariable("uuid") UUID housingId,
                                 @AuthenticationPrincipal CurrentUser currentUser) {

        housingService.addToFavorites(userService.findUser(currentUser), housingId);
        return "redirect:/house/details/" + housingId;
    }
}
