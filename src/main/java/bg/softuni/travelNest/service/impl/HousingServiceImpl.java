package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.config.Messages;
import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.*;
import bg.softuni.travelNest.model.entity.Housing;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.model.entity.base.Comment;
import bg.softuni.travelNest.model.entity.commentEntity.HousingComment;
import bg.softuni.travelNest.model.enums.HousingType;
import bg.softuni.travelNest.repository.*;
import bg.softuni.travelNest.service.TravelNestUserDetails;
import bg.softuni.travelNest.service.PropertyService;
import bg.softuni.travelNest.service.PictureService;
import bg.softuni.travelNest.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
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
    private final Messages messages;


    @Override
    public UUID add(Object addDTO, TravelNestUserDetails travelNestUserDetails) throws IOException {

        if (!(addDTO instanceof AddRentalHousingDTO addRentalHousingDTO)) {
            return null;
        }
        Housing housing = modelMapper.map(addRentalHousingDTO, Housing.class);
        housing.setType(HousingType.valueOf(addRentalHousingDTO.getType().toUpperCase()));
        housing.setOwner(userService.findUser(travelNestUserDetails));
        housing.setCity(cityRepository.findByName(addRentalHousingDTO.getCity().replaceAll("\\.", " ")));
        housing.setPictureUrl(pictureService.uploadImage(addRentalHousingDTO.getImage()));
        return housingRepository.save(housing).getId();
    }

    @Override
    public HousingDetailsDTO findDetailsById(UUID id) {
        return housingRepository.findById(id)
                .map(housingRental -> {
                    HousingDetailsDTO map = modelMapper.map(housingRental, HousingDetailsDTO.class);
                    map.setCity(housingRental.getCity().getName().replaceAll("\\s+", "."));
                    map.setComments(commentRepository.findByType(COMMENT_TYPE)
                            .stream()
                            .map(comment -> (HousingComment) comment)
                            .filter(comment -> comment.getHousing().getId().equals(id))
                            .sorted(Comparator.comparingLong(Comment::getId))
                            .toList());
                    return map;
                })
                .orElseThrow(() -> new ObjectNotFoundException(messages.get("message.error.housing")));
    }

    @Override
    public List<PropertyDTO> findAllAdds() {
        return housingRepository.findAll()
                .stream()
                .map(housingRental -> {
                    PropertyDTO map = modelMapper.map(housingRental, PropertyDTO.class);
                    map.setCity(housingRental.getCity().getName().replaceAll("\\s+", "."));
                    map.setTitle(getTitle(housingRental, messages));
                    return map;
                }).toList();
    }

    @Override
    public void addComment(AddCommentDTO addCommentDTO, UUID housingId, User user) {

        Housing housing = housingRepository.findById(housingId)
                .orElseThrow(() -> new ObjectNotFoundException(messages.get("message.error.housing")));

        commentRepository.saveAndFlush(new HousingComment(addCommentDTO.getText(), housing, user));
    }

    @Override
    @Transactional
    public void addToFavorites(User user, UUID housingId) {
        user.getFavoriteHousings().add(housingRepository.findById(housingId)
                .orElseThrow(() -> new ObjectNotFoundException(messages.get("message.error.housing"))));
    }

    @Override
    @Transactional
    public void deleteProperty(TravelNestUserDetails travelNestUserDetails, UUID housingId) {
        if (!travelNestUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) return;

        Housing housing = housingRepository.findById(housingId)
                .orElseThrow(() -> new ObjectNotFoundException(messages.get("message.error.housing")));
        commentRepository.deleteAllWhereHousing(housing);
        housingRepository.deleteAllFromUsersFavoriteHousingsWhereHousingId(housingId);
        rentRepository.deleteAllWhereHousing(housing);
        housingRepository.delete(housing);
    }

    @Override
    public boolean isFavorite(TravelNestUserDetails travelNestUserDetails, UUID housingId) {
        return userService.findUser(travelNestUserDetails)
                .getFavoriteHousings()
                .contains(housingRepository.findById(housingId).orElse(new Housing()));
    }

    @Override
    public List<PropertyDTO> findUserFavorites(UUID uuid) {
        return housingRepository.findAllByUserFavorites(uuid)
                .orElse(new ArrayList<>())
                .stream()
                .map(housing -> {
                    PropertyDTO map = modelMapper.map(housing, PropertyDTO.class);
                    map.setCity(housing.getCity().getName());
                    map.setTitle(getTitle(housing, messages));
                    return map;
                })
                .toList();
    }

    private static String getTitle(Housing housing, Messages messages) {

        return String.format("%s %d %s",
                messages.get(String.format("housing.fields.%s",
                        housing.getType().toString())),
                housing.getRooms(),
                housing.getRooms() > 1 ?
                        messages.get("housing.fields.many.rooms") :
                        messages.get("housing.fields.one.room"));
    }
}
