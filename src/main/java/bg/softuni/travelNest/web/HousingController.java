package bg.softuni.travelNest.web;

import bg.softuni.travelNest.model.dto.AddCommentDTO;
import bg.softuni.travelNest.model.dto.AddRentalHousingDTO;
import bg.softuni.travelNest.model.enums.City;
import bg.softuni.travelNest.model.enums.HousingType;
import bg.softuni.travelNest.service.CurrentUser;
import bg.softuni.travelNest.service.RentService;
import bg.softuni.travelNest.service.UserService;
import bg.softuni.travelNest.service.impl.HousingServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/housing")
public class HousingController {

    private final HousingServiceImpl housingService;
    private final UserService userService;
    private final RentService rentService;

    @ModelAttribute("cities")
    public List<String> cities() {
        return Arrays.stream(City.values())
                .map(City::toString)
                .toList();
    }

    @ModelAttribute("housingTypes")
    public List<String> types() {
        return Arrays.stream(HousingType.values())
                .map(HousingType::toString)
                .toList();
    }

    @ModelAttribute("addRentalData")
    public AddRentalHousingDTO rentalPropertyDTO() {
        return new AddRentalHousingDTO();
    }

    @ModelAttribute("commentData")
    public AddCommentDTO addCommentDTO() {
        return new AddCommentDTO();
    }

    @GetMapping("/rental")
    public String showRentalHousing(Model model) {
        model.addAttribute("propertyData", housingService.findAllAdds());
        model.addAttribute("refDetailsLink", "/housing/details/{uuid}");
        model.addAttribute("refPropertyAddLink", "/housing/add");
        return "property_rental";
    }

    @GetMapping("/details/{uuid}")
    public String showHousingDetails(@PathVariable("uuid") UUID propertyId,
                                     @AuthenticationPrincipal CurrentUser currentUser,
                                     Model model) {

        if (housingService.isFavorite(currentUser, propertyId)) {
            model.addAttribute("isFavorite", true);
        }
        model.addAttribute("rentPeriods", rentService.getHousingRentPeriods(propertyId));
        model.addAttribute("propertyDetails", housingService.findDetailsById(propertyId));
        model.addAttribute("propertyType", "housing");
        return "property_details";
    }

    @PostMapping("/add-to-favorites/{uuid}")
    public String addToFavorites(@PathVariable("uuid") UUID housingId,
                                 @AuthenticationPrincipal CurrentUser currentUser) {

        housingService.addToFavorites(userService.findUser(currentUser), housingId);
        return "redirect:/housing/details/" + housingId;
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
            return "redirect:/housing/details/" + housingId;
        }

        housingService.addComment(addCommentDTO, housingId, userService.findUser(currentUser));
        return "redirect:/housing/details/" + housingId;
    }

    @DeleteMapping("/details/{uuid}")
    public String deleteHousing(@PathVariable("uuid") UUID housingId,
                                @AuthenticationPrincipal CurrentUser currentUser) {
        housingService.deleteProperty(currentUser, housingId);
        return "redirect:/housing/rental";
    }

    @GetMapping("/add")
    public String showAddHousing(Model model) {
        model.addAttribute("propertyType", "housing");
        model.addAttribute("action", "/housing/add");
        return "add_property";
    }

    @PostMapping("/add")
    public String addHousing(@Valid AddRentalHousingDTO addRentalHousingDTO,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal CurrentUser currentUser,
                             RedirectAttributes rAttr) throws IOException {

        if (bindingResult.hasErrors()) {
            rAttr.addFlashAttribute("addRentalData", addRentalHousingDTO);
            rAttr.addFlashAttribute("org.springframework.validation.BindingResult.addRentalData", bindingResult);
            rAttr.addFlashAttribute("propertyType", "housing");
            return "redirect:/housing/add";
        }

        UUID uuid = housingService.add(addRentalHousingDTO, currentUser);

        return uuid != null ? "redirect:/housing/details/" + uuid
                : "redirect:/housing/add";
    }
}
