package models.demoqa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileResponse {
    private String userId;
    private String username;
    private List<BookResponse> books;
}