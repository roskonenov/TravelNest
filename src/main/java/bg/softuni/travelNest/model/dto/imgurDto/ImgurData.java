package bg.softuni.travelNest.model.dto.imgurDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImgurData {
    @JsonProperty("link")
    private String link;
}
