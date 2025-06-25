package listeners;

import io.qameta.allure.Attachment;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class TestListener implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        saveTextLog("Test failed: " + cause.getMessage());
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        saveTextLog("Test passed successfully");
    }

    @Attachment(value = "Test Log", type = "text/plain")
    public String saveTextLog(String message) {
        return message;
    }
}