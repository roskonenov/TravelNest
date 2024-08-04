package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.AttractionDTO;
import bg.softuni.travelNest.model.dto.TicketDTO;
import bg.softuni.travelNest.service.AttractionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Service

public class AttractionServiceImpl implements AttractionService {

    private final @Qualifier("attractionsRestClient") RestClient restClient;

    public AttractionServiceImpl(@Qualifier("attractionsRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<String> getAttractionCities() {
        return restClient.get()
                .uri("/attractions/cities")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public List<AttractionDTO> getAllAttractions() {
        return restClient.get()
                .uri("/attractions/list")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public AttractionDTO getAttractionById(UUID attractionId) {
        return restClient.get()
                .uri("/attractions/details/{uuid}", attractionId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        ((request, response) -> {
                            throw new ObjectNotFoundException("This attraction does not exist");
                        }))
                .body(AttractionDTO.class);
    }

    @Override
    public TicketDTO getTickets(UUID uuid) {
        return restClient.get()
                .uri("/attractions/tickets/{uuid}", uuid)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(TicketDTO.class);
    }

    @Override
    public void buyTickets(TicketDTO tickets, UUID attractionId) {
        restClient.post()
                .uri("/attractions/details/{uuid}", attractionId)
                .body(tickets)
                .retrieve();
    }
}
