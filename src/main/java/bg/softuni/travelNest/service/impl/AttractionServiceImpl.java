package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.model.dto.AttractionDTO;
import bg.softuni.travelNest.service.AttractionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

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
}
