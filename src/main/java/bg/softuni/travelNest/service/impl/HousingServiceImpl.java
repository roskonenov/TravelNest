package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.AddCommentDTO;
import bg.softuni.travelNest.model.dto.AddRentalPropertyDTO;
import bg.softuni.travelNest.model.dto.HousingDTO;
import bg.softuni.travelNest.model.dto.HousingDetailsDTO;
import bg.softuni.travelNest.model.entity.HousingRental;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.model.entity.base.Comment;
import bg.softuni.travelNest.model.entity.commentEntity.HousingComment;
import bg.softuni.travelNest.repository.CityRepository;
import bg.softuni.travelNest.repository.CommentRepository;
import bg.softuni.travelNest.repository.HousingRepository;
import bg.softuni.travelNest.repository.UserRepository;
import bg.softuni.travelNest.service.CurrentUser;
import bg.softuni.travelNest.service.HousingService;
import bg.softuni.travelNest.service.PictureService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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


    @Override
    public UUID add(AddRentalPropertyDTO addRentalPropertyDTO, MultipartFile image, CurrentUser currentUser) throws IOException {

        return housingRepository.save(
            modelMapper.map(addRentalPropertyDTO, HousingRental.class)
                    .setLandlord(userRepository.findByUsername(currentUser.getUsername())
                            .orElseThrow(() -> new ObjectNotFoundException("Current user not found!")))
                    .setCity(cityRepository.findByName(addRentalPropertyDTO.getCity()))
                    .setPictureUrl(pictureService.uploadImage(image))
        ).getId();
    }

    @Override
    public HousingDetailsDTO findById(UUID id) {
        return housingRepository.findById(id)
                .map(housingRental -> {
                    HousingDetailsDTO map = modelMapper.map(housingRental, HousingDetailsDTO.class);
                    map.setCity(housingRental.getCity().getName());
                    map.setComments(commentRepository.findByType(COMMENT_TYPE)
                            .stream()
                            .map(comment -> (HousingComment) comment)
                            .filter(comment -> comment.getHousingRental().getId().equals(id))
                                    .sorted(Comparator.comparingLong(Comment::getId))
                            .toList());
                    return map;
                })
                .orElseThrow(() -> new ObjectNotFoundException("Rental property not found"));
    }

    @Override
    public List<HousingDTO> findAllNotRented() {
        return housingRepository.findAll()
                .stream()
                .filter(HousingRental::isAvailable)
                .map(housingRental -> {
                    HousingDTO map = modelMapper.map(housingRental, HousingDTO.class);
                    map.setCity(housingRental.getCity().getName());
                    return map;
                }).toList();
    }

    @Override
    public void addComment(AddCommentDTO addCommentDTO, UUID housingId, CurrentUser currentUser) {

        HousingRental housingRental = housingRepository.findById(housingId)
                .orElseThrow(() -> new ObjectNotFoundException("This property does not exist"));

        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException("This user does not exist"));

        commentRepository.saveAndFlush(new HousingComment(addCommentDTO.getText(), housingRental, user));
    }

    @Override
    @Transactional
    public void addToFavorites(CurrentUser currentUser, UUID housingId) {

        userRepository.findByUsername(currentUser.getUsername())
                .map(User::getFavorites)
                .ifPresent(favorites -> favorites.add(housingRepository.findById(housingId)
                        .orElseThrow(() -> new ObjectNotFoundException("No such property!"))));
    }
}
