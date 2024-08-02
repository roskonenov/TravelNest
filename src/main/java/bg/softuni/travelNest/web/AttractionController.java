package bg.softuni.travelNest.web;

import bg.softuni.travelNest.service.AttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionService attractionService;

    @ModelAttribute("cities")
    public List<String> showCities(){
       return attractionService.getAttractionCities();
    }

    @GetMapping("attractions/list")
    public String showAttractions(Model model){
        model.addAttribute("attractionList", attractionService.getAllAttractions());
        model.addAttribute("refAddLink", "/attractions/add");
        return "attraction_list";
    }



}
