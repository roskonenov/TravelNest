package bg.softuni.travelNest.model.dto.imgurDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImgurResponseDTO {

    @JsonProperty("data")
    public ImgurData data;
}





