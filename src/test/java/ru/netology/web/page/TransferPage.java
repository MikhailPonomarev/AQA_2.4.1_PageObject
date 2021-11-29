package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.text;
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

    public DashboardPage transaction(String moneyAmount, String account) {
        amountField.doubleClick().sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE);
        amountField.setValue(moneyAmount);
        fromAccountField.doubleClick().sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE);
        fromAccountField.setValue(account);
        transferButton.click();
        return new DashboardPage();
    }

    public void falseTransaction(String moneyAmount, String account) {
        amountField.doubleClick().sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE);
        amountField.setValue(moneyAmount);
        fromAccountField.doubleClick().sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE);
        fromAccountField.setValue(account);
        transferButton.click();
        errorNotification.shouldBe(visible);
        $(".notification__title").shouldHave(text("Ошибка"));
    }

    public DashboardPage cancelTransferPage() {
        cancelButton.click();
        return new DashboardPage();
    }
}
