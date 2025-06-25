package models.demoqa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    private String userId;
    private String username;
    private String password;
    private String token;
    private String expires;
    private boolean isActive;
}