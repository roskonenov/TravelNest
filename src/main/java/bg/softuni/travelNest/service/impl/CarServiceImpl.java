package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.*;
import bg.softuni.travelNest.model.entity.Car;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.model.entity.base.Comment;
import bg.softuni.travelNest.model.entity.commentEntity.CarComment;
import bg.softuni.travelNest.repository.CarRepository;
import bg.softuni.travelNest.repository.CityRepository;
import bg.softuni.travelNest.repository.CommentRepository;
import bg.softuni.travelNest.repository.RentRepository;
import bg.softuni.travelNest.service.CurrentUser;
import bg.softuni.travelNest.service.PictureService;
import bg.softuni.travelNest.service.PropertyService;
import bg.softuni.travelNest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.Object;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements PropertyService {

    private static final String COMMENT_TYPE = "CAR";

    private final PictureService pictureService;
    private final UserService userService;
    private final CityRepository cityRepository;
    private final CarRepository carRepository;
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;
    private final RentRepository rentRepository;


    @Override
    public UUID add(Object addDTO, CurrentUser currentUser) throws IOException {
        if (!(addDTO instanceof AddRentalCarDTO addRentalCarDTO)) {
            return null;
        }
        return carRepository.save(
                (Car)  modelMapper.map(addRentalCarDTO, Car.class)
                        .setOwner(userService.findUser(currentUser))
                        .setCity(cityRepository.findByName(addRentalCarDTO.getCity()))
                        .setPictureUrl(pictureService.uploadImage(addRentalCarDTO.getImage()))
        ).getId();
    }

    @Override
    public Object findDetailsById(UUID id) {
        return carRepository.findById(id)
                .map(carRental -> {
                    CarDetailsDTO map = modelMapper.map(carRental, CarDetailsDTO.class);
                    map.setCity(carRental.getCity().getName());
                    map.setComments(commentRepository.findByType(COMMENT_TYPE)
                            .stream()
                            .map(comment -> (CarComment) comment)
                            .filter(comment -> comment.getCar().getId().equals(id))
                            .sorted(Comparator.comparingLong(Comment::getId))
                            .toList());
                    return map;
                })
                .orElseThrow(() -> new ObjectNotFoundException("Rental property not found"));
    }

    @Override
    public List<PropertyDTO> findAllAdds() {
        return carRepository.findAll()
                .stream()
                .map(carRental -> {
                    PropertyDTO map = modelMapper.map(carRental, PropertyDTO.class);
                    map.setCity(carRental.getCity().getName());
                    return map;
                }).toList();
    }

    @Override
    public void addComment(AddCommentDTO addCommentDTO, UUID housingId, User user) {
        Car car = carRepository.findById(housingId)
                .orElseThrow(() -> new ObjectNotFoundException("Rental property not found"));

        commentRepository.saveAndFlush(new CarComment(addCommentDTO.getText(), user, car));
    }

    @Override
    @Transactional
    public void addToFavorites(User user, UUID carId) {
        user.getFavoriteCars().add(carRepository.findById(carId)
                .orElseThrow(() -> new ObjectNotFoundException("Rental property not found")));
    }

    @Override
    @Transactional
    public void deleteHousing(CurrentUser currentUser, UUID carId) {
        if (!currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) return;

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ObjectNotFoundException("Rental property not found"));
        commentRepository.deleteAllWhereCar(car);
        carRepository.deleteAllFromUsersFavoriteCarsWhereHousingId(carId);
        rentRepository.deleteAllWhereCar(car);
        carRepository.delete(car);
    }

    @Override
    public boolean isFavorite(CurrentUser currentUser, UUID propertyId) {
        return userService.findUser(currentUser)
                .getFavoriteCars()
                .contains(carRepository.findById(propertyId).orElse(new Car()));
    }
}
