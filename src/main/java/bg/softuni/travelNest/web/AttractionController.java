package bg.softuni.travelNest.web;

import bg.softuni.travelNest.model.dto.AttractionDTO;
import bg.softuni.travelNest.model.dto.TicketDTO;
import bg.softuni.travelNest.service.AttractionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attractions")
public class AttractionController {

    private final AttractionService attractionService;

    @ModelAttribute("cities")
    public List<String> showCities(){
       return attractionService.getAttractionCities();
    }

    @ModelAttribute("attractionDetails")
    public AttractionDTO attractionDTO(){
        return new AttractionDTO();
    }

    @ModelAttribute("tickets")
    public TicketDTO ticketDTO(){
        return new TicketDTO();
    }

    @GetMapping("/list")
    public String showAttractions(Model model){
        model.addAttribute("attractionList", attractionService.getAllAttractions());
        model.addAttribute("refAddLink", "/attractions/add");
        model.addAttribute("refDetailsLink", "/attractions/details/{uuid}");
        return "attraction_list";
    }

    @GetMapping("/details/{uuid}")
    public String showAttractionDetails(Model model,
                                        @PathVariable UUID uuid){

        TicketDTO tickets = attractionService.getTickets(uuid);
        String message = String.format("You have %d bought tickets", tickets.getCount());

        model.addAttribute("attractionDetails", attractionService.getAttractionById(uuid));
        model.addAttribute("tickets", tickets);
        model.addAttribute("message", message);

        return "attraction_details";
    }

    @PostMapping("/details/{uuid}")
    public String buyTickets(@PathVariable("uuid") UUID attractionId,
                             @AuthenticationPrincipal UserDetails userDetails,
                             @Valid TicketDTO tickets,
                             BindingResult bindingResult,
                             RedirectAttributes rAttr){

        tickets.setUsername(userDetails.getUsername());
        if (bindingResult.hasErrors()){
            rAttr.addFlashAttribute("tickets", tickets);
            rAttr.addFlashAttribute("org.springframework.validation.BindingResult.tickets", bindingResult);
            return "redirect:/attractions/details/" + attractionId;
        }
        attractionService.buyTickets(tickets, attractionId);
        return "redirect:/attractions/details/" + attractionId;
    }
}
