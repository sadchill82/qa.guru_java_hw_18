package extensions;

import annotations.WithLogin;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import steps.DemoQASteps;

public class LoginExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        WithLogin loginAnnotation = context.getRequiredTestMethod().getAnnotation(WithLogin.class);

        if (loginAnnotation != null) {
            DemoQASteps steps = new DemoQASteps();
            steps.login(loginAnnotation.username(), loginAnnotation.password());

            context.getStore(ExtensionContext.Namespace.create(context.getRequiredTestClass()))
                    .put("demoQASteps", steps);
        }
    }
}