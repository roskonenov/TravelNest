package bg.softuni.travelNest.web;

import bg.softuni.travelNest.model.dto.AddAttractionDTO;
import bg.softuni.travelNest.model.dto.AttractionDetailsDTO;
import bg.softuni.travelNest.model.dto.TicketDTO;
import bg.softuni.travelNest.service.AttractionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionService attractionService;

    @ModelAttribute("cities")
    public List<String> showCities() {
        return attractionService.getAttractionCities()
                .stream()
                .map(city -> city.replaceAll("\\s+", "."))
                .toList();
    }

    @ModelAttribute("tickets")
    public TicketDTO ticketDTO() {
        return new TicketDTO();
    }

    @ModelAttribute("attractionDetails")
    public AttractionDetailsDTO attractionDTO() {
        return new AttractionDetailsDTO();
    }

    @ModelAttribute("addData")
    public AddAttractionDTO addAttractionDTO() {
        return new AddAttractionDTO();
    }

    @GetMapping("/{attraction-type}/list")
    public String showAttractions(@PathVariable("attraction-type") String attractionType, Model model) {
        model.addAttribute("attractionType", attractionType);
        model.addAttribute("attractionList", attractionService.getAllAttractions(attractionType));
        model.addAttribute("refAddLink", String.format("/%s/add", attractionType));
        model.addAttribute("refDetailsLink", String.format("/%s/details/{uuid}", attractionType));
        return "attraction_list";
    }

    @GetMapping("/{attraction-type}/details/{uuid}")
    public String showAttractionDetails(Model model,
                                        @PathVariable UUID uuid,
                                        @PathVariable("attraction-type") String attractionType) {

        TicketDTO tickets = attractionService.getTickets(uuid);
        String message = String.format("You have %d bought tickets", tickets.getCount());

        model.addAttribute("attractionDetails", attractionService.getAttractionById(uuid));
        model.addAttribute("tickets", tickets);
        model.addAttribute("message", message);
        model.addAttribute("entityType", attractionType);

        return "attraction_details";
    }

    @GetMapping("/{attraction-type}/add")
    public String showAddAttraction(Model model,
                                    @PathVariable("attraction-type") String attractionType) {
        model.addAttribute("entityType", attractionType);
        model.addAttribute("refAddLink", String.format("/%s/add", attractionType));
        return "add_property";
    }

    @PostMapping("/{attraction-type}/add")
    public String addAttraction(@PathVariable("attraction-type") String attractionType,
                                @Valid AddAttractionDTO addAttractionDTO,
                                BindingResult bindingResult,
                                RedirectAttributes rAttr) throws IOException {

        if (bindingResult.hasErrors()) {
            rAttr.addFlashAttribute("addData", addAttractionDTO);
            rAttr.addFlashAttribute("org.springframework.validation.BindingResult.addData", bindingResult);
            rAttr.addFlashAttribute("entityType", attractionType);
            return String.format("redirect:/%s/add", attractionType);
        }

        UUID uuid = attractionService.add(addAttractionDTO, attractionType);

        return uuid != null ? String.format("redirect:/%s/details/%s", attractionType, uuid)
                : String.format("redirect:/%s/add", attractionType);
    }

    @PostMapping("/{attraction-type}/details/{uuid}")
    public String buyTickets(@PathVariable("attraction-type") String attractionType,
                             @PathVariable("uuid") UUID attractionId,
                             @AuthenticationPrincipal UserDetails userDetails,
                             @Valid TicketDTO tickets,
                             BindingResult bindingResult,
                             RedirectAttributes rAttr) {

        tickets.setUsername(userDetails.getUsername());
        if (bindingResult.hasErrors()) {
            rAttr.addFlashAttribute("tickets", tickets);
            rAttr.addFlashAttribute("org.springframework.validation.BindingResult.tickets", bindingResult);
            return String.format("redirect:/%s/details/%s", attractionType, attractionId);
        }
        attractionService.buyTickets(tickets, attractionId);
        return String.format("redirect:/%s/details/%s", attractionType, attractionId);
    }

    @DeleteMapping("/{attraction-type}/details/{uuid}")
    public String deleteAttraction(@PathVariable("uuid") UUID attractionId,
                                   @PathVariable("attraction-type") String attractionType) {
        attractionService.deleteById(attractionId);
        return String.format("redirect:/%s/list", attractionType);
    }
}
