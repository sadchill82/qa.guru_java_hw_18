package test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import helpers.ConfigManager;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class TestBase {

    @BeforeAll
    static void beforeAll() {
        Configuration.browserSize = ConfigManager.getScreenResolution();
        Configuration.browser = ConfigManager.getBrowser();
        Configuration.browserVersion = ConfigManager.getBrowserVersion();
        Configuration.baseUrl = "https://demoqa.com";
        Configuration.pageLoadStrategy = "eager";

        String selenoidUrl = ConfigManager.getSelenoidUrl();
        if (selenoidUrl != null) {
            Configuration.remote = selenoidUrl;

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                    "enableVNC", ConfigManager.isEnableVNC(),
                    "enableVideo", ConfigManager.isEnableVideo()
            ));
            Configuration.browserCapabilities = capabilities;
        }

        RestAssured.baseURI = "https://demoqa.com";
    }

    @BeforeEach
    void beforeEach() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last Screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        Selenide.closeWebDriver();
    }
}