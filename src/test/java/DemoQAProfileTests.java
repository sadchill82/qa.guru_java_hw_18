import annotations.WithLogin;
import io.qameta.allure.Feature;
import models.demoqa.ProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import config.TestConfig;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("DemoQA Profile Management")
public class DemoQAProfileTests extends TestBase {

    @Test
    @DisplayName("Удаление книги из профиля пользователя")
    @WithLogin
    void testDeleteBookFromProfile() {
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
    @DisplayName("Попытка удаления несуществующей книги")
    @WithLogin
    void testDeleteNonExistentBook() {
        String nonExistentIsbn = "9999999999999";
        steps.deleteNonExistentBook(nonExistentIsbn);
    }

    @Test
    @DisplayName("Получение профиля пользователя с проверкой JSON схемы")
    @WithLogin
    void testGetUserProfileWithSchema() {
        ProfileResponse profile = steps.getUserProfile();
        steps.validateProfileStructure(profile);
        steps.validateProfileJsonSchema();
    }

    @Test
    @DisplayName("Полный цикл: добавление и удаление книги")
    @WithLogin
    void testAddThenDeleteBook() {
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
}