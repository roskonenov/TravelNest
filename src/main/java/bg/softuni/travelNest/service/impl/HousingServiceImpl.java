package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.*;
import bg.softuni.travelNest.model.entity.Housing;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.model.entity.base.Comment;
import bg.softuni.travelNest.model.entity.commentEntity.HousingComment;
import bg.softuni.travelNest.repository.*;
import bg.softuni.travelNest.service.CurrentUser;
import bg.softuni.travelNest.service.HousingService;
import bg.softuni.travelNest.service.PictureService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class HousingServiceImpl implements HousingService {

    private static final String COMMENT_TYPE = "HOUSING";

    private final HousingRepository housingRepository;
    private final ModelMapper modelMapper;
    private final PictureService pictureService;
    private final CityRepository cityRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final HousingRentRepository housingRentRepository;


    @Override
    public UUID add(AddRentalPropertyDTO addRentalPropertyDTO, MultipartFile image, CurrentUser currentUser) throws IOException {

        return housingRepository.save(
                modelMapper.map(addRentalPropertyDTO, Housing.class)
                        .setLandlord(userRepository.findByUsername(currentUser.getUsername())
                                .orElseThrow(() -> new ObjectNotFoundException("Current user not found!")))
                        .setCity(cityRepository.findByName(addRentalPropertyDTO.getCity()))
                        .setPictureUrl(pictureService.uploadImage(image))
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
    public List<HousingDTO> findAllAdds() {
        return housingRepository.findAll()
                .stream()
                .map(housingRental -> {
                    HousingDTO map = modelMapper.map(housingRental, HousingDTO.class);
                    map.setCity(housingRental.getCity().getName());
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

        user.getFavorites().add(housingRepository.findById(housingId)
                .orElseThrow(() -> new ObjectNotFoundException("Rental property not found")));
    }

    @Override
    @Transactional
    public void deleteHousing(CurrentUser currentUser, UUID housingId) {
        if (!currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) return;

        Housing housing = housingRepository.findById(housingId)
                .orElseThrow(() -> new ObjectNotFoundException("Rental property not found"));
        commentRepository.deleteAllWhereHousing(housing);
        housingRepository.deleteAllFromUsersFavoritesWhereHousingId(housingId);
        housingRentRepository.deleteAllWhereHousing(housing);
        housingRepository.delete(housing);
    }
}
