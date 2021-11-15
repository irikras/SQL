package ru.netology.web.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.SQLDbUtils;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

    @BeforeEach
    void SetUp() {
        open("http://localhost:9999");
    }

    @AfterAll
    @SneakyThrows
    static void shouldCleanAll() {
        SQLDbUtils.cleanTables();
    }

    @Test
    @SneakyThrows
    void shouldLogin() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = SQLDbUtils.getVerificationCode();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @SneakyThrows
    void shouldBlockedIfPasswordInvalid() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfoWithInvalidPassword();
        loginPage.validLogin(authInfo);
        loginPage.cleanLoginFields();
        loginPage.validLogin(authInfo);
        loginPage.cleanLoginFields();
        loginPage.validLogin(authInfo);
        var statusSQL = SQLDbUtils.getVerificationCode();
        assertEquals("blocked", statusSQL);
    }
}


