package api;

import io.qameta.allure.Step;
import models.AddBookRequest;
import models.AddBookResponse;

import static io.restassured.RestAssured.given;
import static specs.LogSpec.*;

public class BookShopApiSteps {

    @Step("Запрос на добавление книги в корзину")
    public static AddBookResponse addBook(AddBookRequest addBookRequest, String token) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(addBookRequest)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(responseSpec(201))
                .extract().as(AddBookResponse.class);
    }

    @Step("Запрос на удаление всех книг из корзины")
    public static String deleteAllBooks(String token, String userId) {
        return given(requestSpec)
                .header("Authorization", "Bearer " + token)
                .queryParam("UserId", userId)
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .spec(responseSpec(204)).toString();
    }
}