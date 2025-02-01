package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amount = $("[data-test-id='amount'] input");
    private SelenideElement from = $("[data-test-id='from'] input");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private SelenideElement errorMessage = $("[data-test-id='error-notification'] .notification__content");

    public void transferMoney(int amountTransfer, DataHelper.CardInfo cardInfo) {
        amount.setValue(String.valueOf(amountTransfer));
        from.setValue(cardInfo.getCardNumber());
        transferButton.click();
    }

    public void findError(String errorText) {
        errorMessage.shouldBe(Condition.visible);
        errorMessage.shouldHave(Condition.text(errorText));
    }
}
