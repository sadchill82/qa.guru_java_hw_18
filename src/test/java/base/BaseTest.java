package base;

import io.qameta.allure.junit5.AllureJunit5;
import listeners.TestListener;
import org.junit.jupiter.api.extension.ExtendWith;
import config.TestConfig;

@ExtendWith({AllureJunit5.class, TestListener.class})
public abstract class BaseTest {

    protected static final String BASE_URL = TestConfig.BASE_URL;
    protected static final String TEST_USERNAME = TestConfig.TEST_USERNAME;
    protected static final String TEST_PASSWORD = TestConfig.TEST_PASSWORD;

}