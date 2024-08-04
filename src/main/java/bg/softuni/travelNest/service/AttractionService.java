package bg.softuni.travelNest.service;

import bg.softuni.travelNest.model.dto.AttractionDTO;
import bg.softuni.travelNest.model.dto.TicketDTO;

import java.util.List;
import java.util.UUID;

public interface AttractionService {

    List<String> getAttractionCities();

    List<AttractionDTO> getAllAttractions();

    AttractionDTO getAttractionById(UUID attractionId);

    TicketDTO getTickets(UUID uuid);

    void buyTickets(TicketDTO tickets, UUID attractionId);
}

