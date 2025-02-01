package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

public class MoneyTransferTest {
    DashboardPage dashboardPage;
    DataHelper.CardInfo firstCard;
    DataHelper.CardInfo secondCard;

    int balanceFirstCard;
    int balanceSecondCard;

    @BeforeEach
    public void setUp() {
        Selenide.open("http://localhost:9999");

        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCode(authInfo);

        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);

        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();

        balanceFirstCard = dashboardPage.getCardBalance(firstCard);
        balanceSecondCard = dashboardPage.getCardBalance(secondCard);
    }

    @Test
    public void shouldTransferFirstCardSuccess() {
        int amount = DataHelper.getValidAmount(balanceSecondCard);

        var transferPage = dashboardPage.selectCardToTransfer(firstCard);
        transferPage.transferMoney(amount, secondCard);

        int expected = balanceFirstCard + amount;
        int actual = dashboardPage.getCardBalance(firstCard);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldTransferSecondCardSuccess() {
        int amount = DataHelper.getValidAmount(balanceFirstCard);

        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        transferPage.transferMoney(amount, firstCard);

        int expected = balanceSecondCard + amount;
        int actual = dashboardPage.getCardBalance(secondCard);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldFailedWithBadCardNum() {
        int amount = DataHelper.getValidAmount(balanceFirstCard);

        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        transferPage.transferMoney(amount, DataHelper.getBadCardNumber());
        transferPage.findError("Ошибка! Произошла ошибка");
    }

    @Test
    public void shouldFailedWithBadAmount() {
        int amount = DataHelper.getInvalidAmount(balanceSecondCard);

        var transferPage = dashboardPage.selectCardToTransfer(firstCard);
        transferPage.transferMoney(amount, secondCard);
        transferPage.findError("Ошибка! Произошла ошибка");
    }
}
