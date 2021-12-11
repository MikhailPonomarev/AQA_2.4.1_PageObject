package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromAccountField = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement cancelButton = $("[data-test-id=action-cancel]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public TransferPage() {
        amountField.shouldBe(visible);
        fromAccountField.shouldBe(visible);
        transferButton.shouldBe(visible);
        cancelButton.shouldBe(visible);
    }

    public void transaction(String moneyAmount, DataHelper.CardInfo cardNumber) {
        amountField.doubleClick().sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE);
        amountField.setValue(moneyAmount);
        fromAccountField.doubleClick().sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE);
        fromAccountField.setValue(cardNumber.getCardNumber());
        transferButton.click();
    }


    public DashboardPage cancelTransferPage() {
        cancelButton.click();
        return new DashboardPage();
    }
}
