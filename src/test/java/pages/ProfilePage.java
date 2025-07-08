package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ProfilePage {
    private final SelenideElement deleteAllButton = $(".text-right.button.di #submit");
    private final SelenideElement confirmDeleteAllButton = $("#closeSmallModal-ok");

    public void removeAds() {
        Selenide.executeJavaScript("$('#fixedban').remove()");
        Selenide.executeJavaScript("$('footer').remove()");
    }

    @Step("Открываем страницу профиля")
    public ProfilePage openProfilePage() {
        open("/profile");
        removeAds();
        return this;
    }

    @Step("Нажатие на удаление всех книг из профиля")
    public ProfilePage clickDeleteAllBooksButton() {
        deleteAllButton.click();
        return this;
    }

    @Step("Подтверждение удаления книг в окне")
    public ProfilePage confirmDeleteAllBooks() {
        confirmDeleteAllButton.click();
        return this;
    }

    @Step("Проверка, что книг нет в профиле")
    public ProfilePage verifyBookIsRemoved(String bookTitle) {
        $("#see-book-" + bookTitle).shouldNotBe(visible);
        return this;
    }

    @Step("Проверка, что конкретная книга удалена")
    public ProfilePage isBookRemovedSuccessful() {
        verifyBookIsRemoved("Learning JavaScript Design Patterns");
        return this;
    }
}