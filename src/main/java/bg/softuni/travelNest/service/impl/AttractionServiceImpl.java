package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.config.Messages;
import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.AddAttractionDTO;
import bg.softuni.travelNest.model.dto.AttractionDetailsDTO;
import bg.softuni.travelNest.model.dto.TicketDTO;
import bg.softuni.travelNest.service.AttractionService;
import bg.softuni.travelNest.service.PictureService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AttractionServiceImpl implements AttractionService {

    private final @Qualifier("attractionRestClient") RestClient restClient;
    private final ModelMapper modelMapper;
    private final PictureService pictureService;
    private final Messages messages;

    public AttractionServiceImpl(@Qualifier("attractionRestClient") RestClient restClient, ModelMapper modelMapper, PictureService pictureService, Messages messages) {
        this.restClient = restClient;
        this.modelMapper = modelMapper;
        this.pictureService = pictureService;
        this.messages = messages;
    }

    @Override
    public List<String> getAttractionCities() {
        return restClient.get()
                .uri("/attraction/cities")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public List<AttractionDetailsDTO> getAllAttractions(String attractionType) {
        return restClient.get()
                .uri("/{attraction-type}/list", attractionType)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public AttractionDetailsDTO getAttractionById(UUID attractionId) {
        return restClient.get()
                .uri("/attraction/details/{uuid}", attractionId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        ((request, response) -> {
                            throw new ObjectNotFoundException(messages.get("message.error.attraction"));
                        }))
                .body(AttractionDetailsDTO.class);
    }

    @Override
    public TicketDTO getTickets(UUID uuid) {
        return restClient.get()
                .uri("/attraction/tickets/{uuid}", uuid)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(TicketDTO.class);
    }

    @Override
    public void buyTickets(TicketDTO tickets, UUID attractionId) {
        restClient.post()
                .uri("/attraction/details/{uuid}", attractionId)
                .body(tickets)
                .retrieve();
    }

    @Override
    public UUID add(AddAttractionDTO addAttractionDTO, String attractionType) throws IOException {
        ;
        return Objects.requireNonNull(restClient.post()
                        .uri("/{attraction-type}/add", attractionType)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(modelMapper.map(addAttractionDTO, AttractionDetailsDTO.class)
                                .setCity(addAttractionDTO.getCity().replaceAll("\\.", " "))
                                .setPaid(!addAttractionDTO.getPrice().equals(BigDecimal.ZERO))
                                .setPictureUrl(pictureService.uploadImage(addAttractionDTO.getImage())))
                        .retrieve()
                        .body(AttractionDetailsDTO.class))
                .getId();
    }

    @Override
    public void deleteById(UUID attractionId) {
        restClient.delete()
                .uri("/attraction/delete/{uuid}", attractionId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        ((request, response) -> {
                            throw new ObjectNotFoundException(messages.get("message.error.attraction"));
                        }));

    }
}
