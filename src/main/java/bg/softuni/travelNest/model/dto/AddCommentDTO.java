package bg.softuni.travelNest.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddCommentDTO {

    @NotBlank(message = "{comment.not.empty}")
    private String text;
}
