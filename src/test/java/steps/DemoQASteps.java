package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.demoqa.*;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.DemoQASpecifications.*;

public class DemoQASteps {
    private String authToken;
    private String userId;

    @Step("Авторизация пользователя с именем {username}")
    public void login(String username, String password) {
        LoginRequest request = LoginRequest.builder()
                .userName(username)
                .password(password)
                .build();

        LoginResponse response = given()
                .spec(baseRequestSpec)
                .body(request)
                .when()
                .post("/Account/v1/Login")
                .then()
                .spec(successResponseSpec)
                .extract().as(LoginResponse.class);

        this.authToken = response.getToken();
        this.userId = response.getUserId();

        if (this.userId == null || this.userId.isEmpty()) {
            this.userId = getUserIdFromProfile(username);
        }
    }

    @Step("Получение userId из профиля пользователя {username}")
    private String getUserIdFromProfile(String username) {
        Response response = given()
                .spec(authorizedRequestSpec(authToken))
                .when()
                .get("/Account/v1/User/{username}", username);

        if (response.getStatusCode() == 200) {
            ProfileResponse profile = response.as(ProfileResponse.class);
            return profile.getUserId();
        }

        return username;
    }

    @Step("Получение профиля пользователя")
    public ProfileResponse getUserProfile() {
        return given()
                .spec(authorizedRequestSpec(authToken))
                .when()
                .get("/Account/v1/User/{userId}", userId)
                .then()
                .spec(successResponseSpec)
                .extract().as(ProfileResponse.class);
    }

    @Step("Удаление книги с ISBN {isbn} из профиля")
    public void deleteBookFromProfile(String isbn) {
        DeleteBookRequest request = DeleteBookRequest.builder()
                .isbn(isbn)
                .userId(userId)
                .build();

        given()
                .spec(authorizedRequestSpec(authToken))
                .body(request)
                .when()
                .delete("/BookStore/v1/Book")
                .then()
                .spec(noContentResponseSpec);
    }

    @Step("Попытка удаления несуществующей книги с ISBN {isbn}")
    public void deleteNonExistentBook(String isbn) {
        DeleteBookRequest request = DeleteBookRequest.builder()
                .isbn(isbn)
                .userId(userId)
                .build();

        Response response = given()
                .spec(authorizedRequestSpec(authToken))
                .body(request)
                .when()
                .delete("/BookStore/v1/Book");

        assertThat(response.getStatusCode())
                .as("Удаление несуществующей книги должно возвращать ошибку")
                .isIn(400, 404);
    }

    @Step("Добавление книги с ISBN {isbn} в профиль")
    public void addBookToProfile(String isbn) {
        AddBookRequest.CollectionOfIsbn bookIsbn = AddBookRequest.CollectionOfIsbn.builder()
                .isbn(isbn)
                .build();

        AddBookRequest request = AddBookRequest.builder()
                .userId(userId)
                .collectionOfIsbns(Arrays.asList(bookIsbn))
                .build();

        Response response = given()
                .spec(authorizedRequestSpec(authToken))
                .body(request)
                .when()
                .post("/BookStore/v1/Books");

        assertThat(response.getStatusCode())
                .as("Добавление книги должно быть успешным")
                .isIn(200, 201);
    }

    @Step("Проверка, что книга с ISBN {deletedIsbn} была удалена из профиля")
    public void validateBookDeleted(ProfileResponse profile, String deletedIsbn, int initialCount) {
        assertThat(profile.getBooks()).hasSize(initialCount - 1);
        assertThat(profile.getBooks())
                .extracting(BookResponse::getIsbn)
                .doesNotContain(deletedIsbn);
    }

    @Step("Проверка структуры профиля пользователя")
    public void validateProfileStructure(ProfileResponse profile) {
        assertThat(profile.getUserId()).isNotNull();
        assertThat(profile.getUsername()).isNotNull();
        assertThat(profile.getBooks()).isNotNull();
    }

    @Step("Проверка соответствия профиля JSON схеме")
    public void validateProfileJsonSchema() {
        given()
                .spec(authorizedRequestSpec(authToken))
                .when()
                .get("/Account/v1/User/{userId}", userId)
                .then()
                .spec(successResponseSpec)
                .body(matchesJsonSchemaInClasspath("profile-schema.json"));
    }

    @Step("Попытка получения пользователя с ID {userId}")
    public Response tryGetUserById(String userId) {
        return given()
                .spec(authorizedRequestSpec(authToken))
                .when()
                .get("/Account/v1/User/" + userId);
    }

    @Step("Создание нового пользователя")
    public void createUser(String username, String password) {
        CreateUserRequest request = CreateUserRequest.builder()
                .userName(username)
                .password(password)
                .build();

        Response response = given()
                .spec(baseRequestSpec)
                .body(request)
                .when()
                .post("/Account/v1/User");

        assertThat(response.getStatusCode())
                .as("Создание пользователя должно быть успешным")
                .isIn(200, 201);
    }

    @Step("Получение токена авторизации")
    public String generateToken(String username, String password) {
        LoginRequest request = LoginRequest.builder()
                .userName(username)
                .password(password)
                .build();

        Response response = given()
                .spec(baseRequestSpec)
                .body(request)
                .when()
                .post("/Account/v1/GenerateToken");

        if (response.getStatusCode() == 200) {
            return response.jsonPath().getString("token");
        }

        return null;
    }

    @Step("Проверка авторизации пользователя")
    public boolean isAuthorized() {
        if (authToken == null || authToken.isEmpty()) {
            return false;
        }

        Response response = given()
                .spec(authorizedRequestSpec(authToken))
                .when()
                .get("/Account/v1/Authorized");

        return response.getStatusCode() == 200;
    }

    @Step("Удаление всех книг из профиля пользователя")
    public void deleteAllBooksFromProfile() {
        given()
                .spec(authorizedRequestSpec(authToken))
                .queryParam("UserId", userId)
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .statusCode(204);
    }
}