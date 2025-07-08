package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.AddBookRequest;
import models.AddBookResponse;

import static io.restassured.RestAssured.given;
import static specs.LogSpec.*;

public class BookShopApiSteps {

    @Step("Запрос на добавление книги в профиль")
    public static AddBookResponse addBook(AddBookRequest addBookRequest, String token) {
        Response response = given(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(addBookRequest)
                .when()
                .post("/BookStore/v1/Books");

        int statusCode = response.getStatusCode();
        if (statusCode == 200 || statusCode == 201) {
            return response.as(AddBookResponse.class);
        } else {
            throw new AssertionError("Failed to add book. Status: " + statusCode +
                    ", Body: " + response.getBody().asString());
        }
    }

    @Step("Запрос на удаление всех книг из профиля")
    public static void deleteAllBooks(String token, String userId) {
        Response response = given(requestSpec)
                .header("Authorization", "Bearer " + token)
                .queryParam("UserId", userId)
                .when()
                .delete("/BookStore/v1/Books");

        int statusCode = response.getStatusCode();
        if (statusCode != 200 && statusCode != 204 && statusCode != 404) {
            throw new AssertionError("Failed to delete books. Status: " + statusCode +
                    ", Body: " + response.getBody().asString());
        }
    }
}