package bg.softuni.travelNest.service;

import bg.softuni.travelNest.model.dto.AttractionDTO;

import java.util.List;

public interface AttractionService {

    List<String> getAttractionCities();

    List<AttractionDTO> getAllAttractions();
}

