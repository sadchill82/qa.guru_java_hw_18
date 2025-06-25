import base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import models.demoqa.BookResponse;
import models.demoqa.ProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import steps.DemoQASteps;
import config.TestConfig;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("DemoQA Profile Management")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoQAProfileTests extends BaseTest {
    private DemoQASteps steps;

    @BeforeEach
    void setUp() {
        steps = new DemoQASteps();

        if (!steps.isAuthorized()) {
            steps.login(TestConfig.TEST_USERNAME, TestConfig.TEST_PASSWORD);
        }
    }

    @Test
    @Tag("profile")
    @Tag("delete")
    @Story("Управление книгами")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Удаление книги из профиля пользователя")
    void testDeleteBookFromProfile() {
        steps.login(TestConfig.TEST_USERNAME, TestConfig.TEST_PASSWORD);

        ProfileResponse profile = steps.getUserProfile();

        if (profile.getBooks().isEmpty()) {
            steps.addBookToProfile(TestConfig.TEST_BOOK_ISBNS[0]);
            profile = steps.getUserProfile();
        }

        assertThat(profile.getBooks()).isNotEmpty();

        int initialBookCount = profile.getBooks().size();
        String bookToDelete = profile.getBooks().get(0).getIsbn();

        steps.deleteBookFromProfile(bookToDelete);

        ProfileResponse updatedProfile = steps.getUserProfile();

        steps.validateBookDeleted(updatedProfile, bookToDelete, initialBookCount);
    }

    @Test
    @Tag("profile")
    @Tag("negative")
    @Story("Управление книгами")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Попытка удаления несуществующей книги")
    void testDeleteNonExistentBook() {
        steps.login(TestConfig.TEST_USERNAME, TestConfig.TEST_PASSWORD);

        String nonExistentIsbn = "9999999999999";

        steps.deleteNonExistentBook(nonExistentIsbn);
    }

    @Test
    @Tag("profile")
    @Tag("get")
    @Story("Получение профиля")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Получение профиля пользователя с проверкой JSON схемы")
    void testGetUserProfileWithSchema() {
        steps.login(TestConfig.TEST_USERNAME, TestConfig.TEST_PASSWORD);

        ProfileResponse profile = steps.getUserProfile();

        steps.validateProfileStructure(profile);
        steps.validateProfileJsonSchema();
    }

    @Test
    @Tag("books")
    @Tag("workflow")
    @Story("Управление книгами")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Полный цикл: добавление и удаление книги")
    void testAddThenDeleteBook() {
        steps.login(TestConfig.TEST_USERNAME, TestConfig.TEST_PASSWORD);

        ProfileResponse initialProfile = steps.getUserProfile();
        int initialCount = initialProfile.getBooks().size();

        String isbnToAdd = TestConfig.TEST_BOOK_ISBNS[1];
        steps.addBookToProfile(isbnToAdd);

        ProfileResponse profileAfterAdd = steps.getUserProfile();
        assertThat(profileAfterAdd.getBooks()).hasSizeGreaterThan(initialCount);

        boolean bookFound = profileAfterAdd.getBooks().stream()
                .anyMatch(book -> isbnToAdd.equals(book.getIsbn()));
        assertThat(bookFound).isTrue();

        steps.deleteBookFromProfile(isbnToAdd);

        ProfileResponse profileAfterDelete = steps.getUserProfile();
        boolean bookStillExists = profileAfterDelete.getBooks().stream()
                .anyMatch(book -> isbnToAdd.equals(book.getIsbn()));
        assertThat(bookStillExists).isFalse();
    }

    @Test
    @Tag("auth")
    @Tag("token")
    @Story("Авторизация")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Проверка генерации и валидности токена")
    void testTokenGeneration() {
        String token = steps.generateToken(TestConfig.TEST_USERNAME, TestConfig.TEST_PASSWORD);

        assertThat(token).isNotNull().isNotEmpty();

        steps.login(TestConfig.TEST_USERNAME, TestConfig.TEST_PASSWORD);
        assertThat(steps.isAuthorized()).isTrue();
    }
}