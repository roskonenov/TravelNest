package bg.softuni.travelNest.web;


import bg.softuni.travelNest.model.dto.AddCommentDTO;
import bg.softuni.travelNest.model.dto.AddRentalCarDTO;
import bg.softuni.travelNest.model.enums.City;
import bg.softuni.travelNest.model.enums.Engine;
import bg.softuni.travelNest.service.TravelNestUserDetails;
import bg.softuni.travelNest.service.RentService;
import bg.softuni.travelNest.service.UserService;
import bg.softuni.travelNest.service.impl.CarServiceImpl;
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
@RequestMapping("/car")
public class CarController {

    private final CarServiceImpl carService;
    private final UserService userService;
    private final RentService rentService;

    @ModelAttribute("cities")
    public List<String> cities() {
        return Arrays.stream(City.values())
                .map(City::toString)
                .toList();
    }

    @ModelAttribute("engines")
    public List<String> engines() {
        return Arrays.stream(Engine.values())
                .map(Engine::toString)
                .toList();
    }

    @ModelAttribute("addRentalData")
    public AddRentalCarDTO addRentalCarDTO(){
        return new AddRentalCarDTO();
    }

    @ModelAttribute("commentData")
    public AddCommentDTO addCommentDTO() {
        return new AddCommentDTO();
    }

    @GetMapping("/rental")
    public String showRentalCars(Model model) {
        model.addAttribute("propertyData", carService.findAllAdds());
        model.addAttribute("refDetailsLink", "/car/details/{uuid}");
        model.addAttribute("refPropertyAddLink", "/car/add");
        return "property_rental";
    }

    @GetMapping("/add")
    public String showAddCar(Model model) {
        model.addAttribute("propertyType", "car");
        model.addAttribute("action", "/car/add");
        return "add_property";
    }

    @PostMapping("/add")
    public String addCar(@Valid AddRentalCarDTO addRentalCarDTO,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal TravelNestUserDetails travelNestUserDetails,
                         RedirectAttributes rAttr) throws IOException {

        if (bindingResult.hasErrors()) {
            rAttr.addFlashAttribute("addRentalData", addRentalCarDTO);
            rAttr.addFlashAttribute("org.springframework.validation.BindingResult.addRentalData", bindingResult);
            rAttr.addFlashAttribute("propertyType", "car");
            return "redirect:/car/add";
        }

        UUID uuid = carService.add(addRentalCarDTO, travelNestUserDetails);

        return uuid != null ? "redirect:/car/details/" + uuid
                : "redirect:/car/add";
    }

    @GetMapping("/details/{uuid}")
    public String showCarDetails(@PathVariable("uuid") UUID propertyId,
                                 @AuthenticationPrincipal TravelNestUserDetails travelNestUserDetails,
                                 Model model) {

        if (carService.isFavorite(travelNestUserDetails, propertyId)) {
            model.addAttribute("isFavorite", true);
        }
        model.addAttribute("rentPeriods", rentService.getCarRentPeriods(propertyId));
        model.addAttribute("propertyDetails", carService.findDetailsById(propertyId));
        model.addAttribute("propertyType", "car");
        return "property_details";
    }

    @PostMapping("/details/{uuid}")
    public String addComment(@PathVariable("uuid") UUID carId,
                             @AuthenticationPrincipal TravelNestUserDetails travelNestUserDetails,
                             @Valid AddCommentDTO addCommentDTO,
                             BindingResult bindingResult,
                             RedirectAttributes rAttr) {

        if (bindingResult.hasErrors()) {
            rAttr.addFlashAttribute("commentData", addCommentDTO);
            rAttr.addFlashAttribute("org.springframework.validation.BindingResult.commentData", bindingResult);
            return "redirect:/car/details/" + carId;
        }

        carService.addComment(addCommentDTO, carId, userService.findUser(travelNestUserDetails));
        return "redirect:/car/details/" + carId;
    }

    @PostMapping("/add-to-favorites/{uuid}")
    public String addToFavorites(@PathVariable("uuid") UUID carId,
                                 @AuthenticationPrincipal TravelNestUserDetails travelNestUserDetails) {

        carService.addToFavorites(userService.findUser(travelNestUserDetails), carId);
        return "redirect:/car/details/" + carId;
    }

    @DeleteMapping("/details/{uuid}")
    public String deleteCar(@PathVariable("uuid") UUID carId,
                            @AuthenticationPrincipal TravelNestUserDetails travelNestUserDetails) {
        carService.deleteProperty(travelNestUserDetails, carId);
        return "redirect:/car/rental";
    }
}
