package bg.softuni.travelNest.service;

import bg.softuni.travelNest.model.dto.*;
import bg.softuni.travelNest.model.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PropertyService {

    UUID add(Object addDTO, TravelNestUserDetails travelNestUserDetails) throws IOException;

    Object findDetailsById(UUID id);

    List<PropertyDTO> findAllAdds();

    void addComment(AddCommentDTO addCommentDTO, UUID housingId, User user);

    void addToFavorites(User user, UUID housingId);

    void deleteProperty(TravelNestUserDetails travelNestUserDetails, UUID housingId);

    boolean isFavorite(TravelNestUserDetails travelNestUserDetails, UUID propertyId);

    List<PropertyDTO> findUserFavorites(UUID uuid);
}
