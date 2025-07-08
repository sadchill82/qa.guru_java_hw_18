package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.ConfigManager;
import io.qameta.allure.Step;
import models.RegisterRequest;
import models.RegisterResponse;

import java.io.File;

import static specs.LogSpec.requestSpec;
import static specs.LogSpec.responseSpec;
import static io.restassured.RestAssured.given;

public class AuthorizationApiSteps {

    @Step("Авторизация пользователя")
    public static RegisterResponse getAuthCookie() {
        RegisterRequest registerRequest;
        registerRequest = new RegisterRequest();
        registerRequest.setUserName(ConfigManager.TEST_USERNAME);
        registerRequest.setPassword(ConfigManager.TEST_PASSWORD);

        return given(requestSpec)
                .body(registerRequest)
                .when()
                .post("/Account/v1/Login")
                .then()
                .spec(responseSpec(200))
                .extract().as(RegisterResponse.class);
    }
}