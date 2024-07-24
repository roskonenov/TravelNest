package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.*;
import bg.softuni.travelNest.model.entity.Housing;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.model.entity.base.Comment;
import bg.softuni.travelNest.model.entity.commentEntity.HousingComment;
import bg.softuni.travelNest.model.enums.HousingType;
import bg.softuni.travelNest.repository.*;
import bg.softuni.travelNest.service.CurrentUser;
import bg.softuni.travelNest.service.PropertyService;
import bg.softuni.travelNest.service.PictureService;
import bg.softuni.travelNest.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class HousingServiceImpl implements PropertyService {

    private static final String COMMENT_TYPE = "HOUSING";

    private final HousingRepository housingRepository;
    private final ModelMapper modelMapper;
    private final PictureService pictureService;
    private final UserService userService;
    private final CityRepository cityRepository;
    private final CommentRepository commentRepository;
    private final RentRepository rentRepository;


    @Override
    public UUID add(Object addDTO, CurrentUser currentUser) throws IOException {

        if (!(addDTO instanceof AddRentalHousingDTO addRentalHousingDTO)) {
            return null;
        }
        return housingRepository.save(
                (Housing)  modelMapper.map(addRentalHousingDTO, Housing.class)
                        .setType(HousingType.valueOf(addRentalHousingDTO.getType().toUpperCase()))
                        .setOwner(userService.findUser(currentUser))
                        .setCity(cityRepository.findByName(addRentalHousingDTO.getCity()))
                        .setPictureUrl(pictureService.uploadImage(addRentalHousingDTO.getImage()))
        ).getId();
    }

    @Override
    public HousingDetailsDTO findDetailsById(UUID id) {
        return housingRepository.findById(id)
                .map(housingRental -> {
                    HousingDetailsDTO map = modelMapper.map(housingRental, HousingDetailsDTO.class);
                    map.setCity(housingRental.getCity().getName());
                    map.setComments(commentRepository.findByType(COMMENT_TYPE)
                            .stream()
                            .map(comment -> (HousingComment) comment)
                            .filter(comment -> comment.getHousing().getId().equals(id))
                            .sorted(Comparator.comparingLong(Comment::getId))
                            .toList());
                    return map;
                })
                .orElseThrow(() -> new ObjectNotFoundException("Rental property not found"));
    }

    @Override
    public List<PropertyDTO> findAllAdds() {
        return housingRepository.findAll()
                .stream()
                .map(housingRental -> {
                    PropertyDTO map = modelMapper.map(housingRental, PropertyDTO.class);
                    map.setCity(housingRental.getCity().getName());
                    map.setTitle(String.format("%s %d %s",
                            housingRental.getType().toString().toLowerCase(),
                            housingRental.getRooms(),
                            housingRental.getRooms() > 1 ? "rooms" : "room"));
                    return map;
                }).toList();
    }

    @Override
    public void addComment(AddCommentDTO addCommentDTO, UUID housingId, User user) {

        Housing housing = housingRepository.findById(housingId)
                .orElseThrow(() -> new ObjectNotFoundException("Rental property not found"));

        commentRepository.saveAndFlush(new HousingComment(addCommentDTO.getText(), housing, user));
    }

    @Override
    @Transactional
    public void addToFavorites(User user, UUID housingId) {
        user.getFavoriteHousings().add(housingRepository.findById(housingId)
                .orElseThrow(() -> new ObjectNotFoundException("Rental property not found")));
    }

    @Override
    @Transactional
    public void deleteProperty(CurrentUser currentUser, UUID housingId) {
        if (!currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) return;

        Housing housing = housingRepository.findById(housingId)
                .orElseThrow(() -> new ObjectNotFoundException("Rental property not found"));
        commentRepository.deleteAllWhereHousing(housing);
        housingRepository.deleteAllFromUsersFavoriteHousingsWhereHousingId(housingId);
        rentRepository.deleteAllWhereHousing(housing);
        housingRepository.delete(housing);
    }

    @Override
    public boolean isFavorite(CurrentUser currentUser, UUID housingId) {
        return userService.findUser(currentUser)
                .getFavoriteHousings()
                .contains(housingRepository.findById(housingId).orElse(new Housing()));
    }
}
