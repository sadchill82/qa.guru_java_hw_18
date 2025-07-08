package test;

import pages.ProfilePage;
import api.BookShopApiSteps;
import helpers.ConfigManager;
import helpers.DataStorage;
import helpers.WithLogin;
import models.AddBookRequest;
import models.RegisterResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BookDeleteTest extends TestBase {
    ProfilePage profilePage = new ProfilePage();

    @WithLogin
    @Test
    @DisplayName("Удаление книги")
    void bookDeleteTest() {
        RegisterResponse loginData = DataStorage.registerResponse;
        String isbn = DataStorage.isbn;

        BookShopApiSteps.deleteAllBooks(loginData.getToken(), loginData.getUserId());

        AddBookRequest addBookRequest = new AddBookRequest(
                loginData.getUserId(),
                List.of(new AddBookRequest.Isbn(isbn))
        );

        BookShopApiSteps.addBook(addBookRequest, loginData.getToken());

        profilePage.openProfilePage()
                .clickDeleteAllBooksButton()
                .confirmDeleteAllBooks()
                .isBookRemovedSuccessful();
    }

    @WithLogin
    @Test
    @DisplayName("Удаление случайной книги из профиля")
    void bookDeleteRandomTest() {
        RegisterResponse loginData = DataStorage.registerResponse;
        String randomIsbn = ConfigManager.getRandomBookIsbn();

        BookShopApiSteps.deleteAllBooks(loginData.getToken(), loginData.getUserId());

        AddBookRequest addBookRequest = new AddBookRequest(
                loginData.getUserId(),
                List.of(new AddBookRequest.Isbn(randomIsbn))
        );

        BookShopApiSteps.addBook(addBookRequest, loginData.getToken());

        profilePage.openProfilePage()
                .clickDeleteAllBooksButton()
                .confirmDeleteAllBooks()
                .isBookRemovedSuccessful();
    }

    @WithLogin
    @Test
    @DisplayName("Удаление нескольких книг из профиля")
    void deleteMultipleBooksTest() {
        RegisterResponse loginData = DataStorage.registerResponse;

        BookShopApiSteps.deleteAllBooks(loginData.getToken(), loginData.getUserId());

        for (int i = 0; i < 3; i++) {
            String isbn = ConfigManager.getBookIsbn(i);
            AddBookRequest addBookRequest = new AddBookRequest(
                    loginData.getUserId(),
                    List.of(new AddBookRequest.Isbn(isbn))
            );
            BookShopApiSteps.addBook(addBookRequest, loginData.getToken());
        }

        profilePage.openProfilePage()
                .clickDeleteAllBooksButton()
                .confirmDeleteAllBooks()
                .isBookRemovedSuccessful();
    }
}