package models;

import lombok.Data;

@Data
public class RegisterRequest {
    private String userName, password;
}
