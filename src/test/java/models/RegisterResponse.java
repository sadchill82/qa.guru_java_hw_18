package models;

import lombok.Data;

@Data
public class RegisterResponse {
    String created_date, expires, isActive, password, token, userId, username;
}