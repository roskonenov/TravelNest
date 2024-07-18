package bg.softuni.travelNest.service;

import bg.softuni.travelNest.model.dto.*;
import bg.softuni.travelNest.model.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface HousingService {

    UUID add(AddRentalPropertyDTO addRentalPropertyDTO, MultipartFile image, CurrentUser currentUser) throws IOException;

    HousingDetailsDTO findDetailsById(UUID id);

    List<HousingDTO> findAllAdds();

    void addComment(AddCommentDTO addCommentDTO, UUID housingId, User user);

    void addToFavorites(User user, UUID housingId);
}
