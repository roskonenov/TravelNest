package bg.softuni.travelNest.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PropertyDTO {
    private UUID id;
    private String title;
    private String city;
    private String pictureUrl;
    private String address;
}
