package bg.softuni.travelNest.service;

import bg.softuni.travelNest.model.dto.AddAttractionDTO;
import bg.softuni.travelNest.model.dto.AttractionDetailsDTO;
import bg.softuni.travelNest.model.dto.TicketDTO;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AttractionService {

    List<String> getAttractionCities();

    List<AttractionDetailsDTO> getAllAttractions(String attractionType);

    AttractionDetailsDTO getAttractionById(UUID attractionId);

    TicketDTO getTickets(UUID uuid);

    void buyTickets(TicketDTO tickets, UUID attractionId);

    UUID add(AddAttractionDTO addAttractionDTO, String attractionType) throws IOException;

    void deleteById(UUID attractionId);
}

