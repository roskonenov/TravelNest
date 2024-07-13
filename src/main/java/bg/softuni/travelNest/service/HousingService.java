package bg.softuni.travelNest.service;

import bg.softuni.travelNest.model.dto.AddCommentDTO;
import bg.softuni.travelNest.model.dto.AddRentalPropertyDTO;
import bg.softuni.travelNest.model.dto.HousingDTO;
import bg.softuni.travelNest.model.dto.HousingDetailsDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface HousingService {

    UUID add(AddRentalPropertyDTO addRentalPropertyDTO, MultipartFile image) throws IOException;

    HousingDetailsDTO findById(UUID id);

    List<HousingDTO> findAll();

    void addComment(AddCommentDTO addCommentDTO, UUID housingId, CurrentUser currentUser);
}
