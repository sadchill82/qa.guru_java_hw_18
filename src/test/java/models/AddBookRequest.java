package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBookRequest {
    private String userId;
    private List<Isbn> collectionOfIsbns;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Isbn {
        private String isbn;
    }
}