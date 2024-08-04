package bg.softuni.travelNest.service.impl;

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

    private final @Qualifier("attractionsRestClient") RestClient restClient;
    private final ModelMapper modelMapper;
    private final PictureService pictureService;

    public AttractionServiceImpl(@Qualifier("attractionsRestClient") RestClient restClient, ModelMapper modelMapper, PictureService pictureService) {
        this.restClient = restClient;
        this.modelMapper = modelMapper;
        this.pictureService = pictureService;
    }

    @Override
    public List<String> getAttractionCities() {
        return restClient.get()
                .uri("/attractions/cities")
                .accept(MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public List<AttractionDetailsDTO> getAllAttractions() {
        return restClient.get()
                .uri("/attractions/list")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public AttractionDetailsDTO getAttractionById(UUID attractionId) {
        return restClient.get()
                .uri("/attractions/details/{uuid}", attractionId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        ((request, response) -> {
                            throw new ObjectNotFoundException("This attraction does not exist");
                        }))
                .body(AttractionDetailsDTO.class);
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

    @Override
    public UUID add(AddAttractionDTO addAttractionDTO) throws IOException {
        ;
        return Objects.requireNonNull(restClient.post()
                        .uri("/attractions/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .body(modelMapper.map(addAttractionDTO, AttractionDetailsDTO.class)
                                .setCityName(addAttractionDTO.getCity())
                                .setPaid(!addAttractionDTO.getPrice().equals(BigDecimal.ZERO))
                                .setPictureUrl(pictureService.uploadImage(addAttractionDTO.getImage())))
                        .retrieve()
                        .body(AttractionDetailsDTO.class))
                .getId();
    }

    @Override
    public void deleteById(UUID attractionId) {
        restClient.delete()
                .uri("/attractions/delete/{uuid}", attractionId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        ((request, response) -> {
                            throw new ObjectNotFoundException("This attraction does not exist");
                        }));

    }
}
