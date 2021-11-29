package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item");
    private ElementsCollection cardButtons = $$("[data-test-id=action-deposit]");
    public final String balanceStart = "баланс: ";
    public final String balanceFinish = " р.";


    public DashboardPage() {
        heading.shouldBe(visible);
        cards.first().shouldBe(visible);
        cards.last().shouldBe(visible);
        cardButtons.first().shouldBe(visible);
        cardButtons.last().shouldBe(visible);
    }

    public int getFirstCardBalance() {
        val text = cards.first().text();
        return extractBalance(text);
    }

    public int getSecondCardBalance() {
        val text = cards.last().text();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public TransferPage firstCardOpen() {
        cardButtons.first().click();
        return new TransferPage();
    }

    public TransferPage secondCardOpen() {
        cardButtons.last().click();
        return new TransferPage();
    }
}