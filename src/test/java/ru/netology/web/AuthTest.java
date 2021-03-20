package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.domain.UserInfo;
import ru.netology.generator.UserSetUp;

import java.util.Locale;

import static com.codeborne.selenide.Selenide.*;

public class AuthTest {
    private final UserInfo activeUser = UserSetUp.setActiveUser();
    private final UserInfo blockedUser = UserSetUp.setBlockedUser();
    Faker faker = new Faker(new Locale("eng"));

    @BeforeEach
    public void setUp() {

        open("http://localhost:9999");
    }

    @Test
    public void shouldHaveForm() {
        $(".heading").shouldHave(Condition.exactText("Интернет Банк"));
        $("[data-test-id='login'] .input__top").shouldHave(Condition.exactText("Логин"));
        $("[data-test-id='password'] .input__top").shouldHave(Condition.exactText("Пароль"));
    }

    @Test
    public void shouldAuthActiveUser() {
        $("[data-test-id='login'] .input__control").setValue(activeUser.getLogin());
        $("[data-test-id='password'] .input__control").setValue(activeUser.getPassword());
        $(".button[data-test-id='action-login']").click();
        $(".heading").shouldHave(Condition.exactText("Личный кабинет"));
    }

    @Test
    public void shouldNotAuthBlockedUser() {
        $("[data-test-id='login'] .input__control").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] .input__control").setValue(blockedUser.getPassword());
        $(".button[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    public void shouldNotAuthWithEmptyFields() {
        $(".button[data-test-id='action-login']").click();
        $("[data-test-id='login'] .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
        $("[data-test-id='password'] .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldNotAuthWithEmptyPassword() {
        $("[data-test-id='login'] .input__control").setValue(activeUser.getLogin());
        $(".button[data-test-id='action-login']").click();
        $("[data-test-id='login'] .input__sub").shouldNotHave(Condition.exist);
        $("[data-test-id='password'] .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldNotAuthWithEmptyLogin() {
        $("[data-test-id='password'] .input__control").setValue(activeUser.getPassword());
        $(".button[data-test-id='action-login']").click();
        $("[data-test-id='password'] .input__sub").shouldNotHave(Condition.exist);
        $("[data-test-id='login'] .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldNotAuthWithNonExistingUser() {
        $("[data-test-id='login'] .input__control").setValue(faker.name().firstName());
        $("[data-test-id='password'] .input__control").setValue(faker.numerify("#####"));
        $(".button[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldNotAuthActiveWithWrongPassword() {
        $("[data-test-id='login'] .input__control").setValue(activeUser.getLogin());
        $("[data-test-id='password'] .input__control").setValue(faker.numerify("#####"));
        $(".button[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldNotAuthBlockedWithWrongPassword() {
        $("[data-test-id='login'] .input__control").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] .input__control").setValue(faker.numerify("#####"));
        $(".button[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

}
