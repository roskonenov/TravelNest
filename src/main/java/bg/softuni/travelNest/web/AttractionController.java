package bg.softuni.travelNest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attractions")
@RequiredArgsConstructor
public class AttractionController {

    @GetMapping("/list")
    public String showAttractions(){
        return "attraction_list";
    }



}
