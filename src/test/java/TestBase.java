import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import steps.DemoQASteps;

public class TestBase {

    protected static DemoQASteps steps = new DemoQASteps();

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://demoqa.com";

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();

        steps.login();
    }
}