package bg.softuni.travelNest.web;

import bg.softuni.travelNest.model.dto.AddAttractionDTO;
import bg.softuni.travelNest.model.dto.AttractionDetailsDTO;
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

import java.io.IOException;
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

    @ModelAttribute("tickets")
    public TicketDTO ticketDTO(){
        return new TicketDTO();
    }

    @ModelAttribute("attractionDetails")
    public AttractionDetailsDTO attractionDTO(){
        return new AttractionDetailsDTO();
    }

    @ModelAttribute("addData")
    public AddAttractionDTO addAttractionDTO(){
        return new AddAttractionDTO();
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
        model.addAttribute("entityType", "attractions");

        return "attraction_details";
    }

    @GetMapping("/add")
    public String showAddAttraction(Model model){
        model.addAttribute("entityType", "attractions");
        model.addAttribute("refAddLink", "/attractions/add");
        return "add_property";
    }

    @PostMapping("/add")
    public String addAttraction(@Valid AddAttractionDTO addAttractionDTO,
                         BindingResult bindingResult,
                                RedirectAttributes rAttr) throws IOException {

        if (bindingResult.hasErrors()) {
            rAttr.addFlashAttribute("addData", addAttractionDTO);
            rAttr.addFlashAttribute("org.springframework.validation.BindingResult.addData", bindingResult);
            rAttr.addFlashAttribute("entityType", "attractions");
            return "redirect:/attractions/add";
        }

        UUID uuid = attractionService.add(addAttractionDTO);

        return uuid != null ? "redirect:/attractions/details/" + uuid
                : "redirect:/attractions/add";
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

    @DeleteMapping("/details/{uuid}")
    public String deleteAttraction(@PathVariable("uuid") UUID attractionId){
        attractionService.deleteById(attractionId);
        return "redirect:/attractions/list";
    }
}
