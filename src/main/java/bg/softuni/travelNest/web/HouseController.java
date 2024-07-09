package bg.softuni.travelNest.web;

import bg.softuni.travelNest.model.enums.City;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@Controller
public class HouseController {

    @ModelAttribute("cities")
    public List<String> cities(){
        return  Arrays.stream(City.values())
                .map(City::toString)
                .toList();
    }

    @GetMapping("/house/rental")
    public String showRentalHousing(){
        return "housing_rental";
    }
}
