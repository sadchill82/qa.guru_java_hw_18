package models;

import lombok.Data;

import java.util.List;

@Data
public class AddBookResponse {
    private List<AddBookRequest.Isbn> books;
}