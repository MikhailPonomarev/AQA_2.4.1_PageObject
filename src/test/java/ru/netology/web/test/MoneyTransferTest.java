package ru.netology.web.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;
import ru.netology.web.page.VerificationPage;

import static com.codeborne.selenide.Selenide.*;

class MoneyTransferTest {
    private final String firstAccount = "5559 0000 0000 0001";
    private final String secondAccount = "5559 0000 0000 0002";

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        loginPage.validLogin(authInfo);

        var verificationPage = new VerificationPage();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("Various transactions between cards")
    public void transactionsBetweenCards() {
        var dashboardPage = new DashboardPage();

        int firstCardBalance = dashboardPage.getFirstCardBalance();
        int secondCardBalance = dashboardPage.getSecondCardBalance();

        if (firstCardBalance == secondCardBalance || firstCardBalance == 0) {
            dashboardPage.firstCardOpen();
            var transferPage = new TransferPage();
            transferPage.transaction("1000", secondAccount);

            int firstCardAfterTransaction = dashboardPage.getFirstCardBalance();
            int expected = firstCardBalance + 1000;

            assertEquals(expected, firstCardAfterTransaction);
        } else if (firstCardBalance > secondCardBalance || secondCardBalance == 0) {
            dashboardPage.secondCardOpen();
            var transferPage = new TransferPage();
            transferPage.transaction("5000", firstAccount);

            int secondCardAfterTransaction = dashboardPage.getSecondCardBalance();
            int expected = secondCardBalance + 5000;

            assertEquals(expected, secondCardAfterTransaction);
        } else {
            dashboardPage.firstCardOpen();
            var transferPage = new TransferPage();
            transferPage.transaction(String.valueOf(secondCardBalance), secondAccount);

            int firstCardAfterTransaction = dashboardPage.getFirstCardBalance();
            int expected = firstCardBalance + secondCardBalance;

            assertEquals(expected, firstCardAfterTransaction);
        }
    }

    @Test
    @DisplayName("Should cancel transfer page")
    public void cancelTransferPage() {
        var dashboardPage = new DashboardPage();
        dashboardPage.firstCardOpen();
        var transferPage = new TransferPage();
        transferPage.cancelTransferPage();

        dashboardPage.secondCardOpen();
        transferPage.cancelTransferPage();
    }

    @Test
    @DisplayName("Transaction over limit")
    public void transferOverLimit() {
        var dashboardPage = new DashboardPage();
        int firstCardBalance = dashboardPage.getFirstCardBalance();
        int secondCardBalance = dashboardPage.getSecondCardBalance();

        if (firstCardBalance > secondCardBalance) {
            dashboardPage.secondCardOpen();
            var transferPage = new TransferPage();
            transferPage.transaction(String.valueOf(firstCardBalance + 1), firstAccount);

            int firstCardAfterTransaction = dashboardPage.getFirstCardBalance();
            int secondCardAfterTransaction = dashboardPage.getSecondCardBalance();

            assertTrue(secondCardAfterTransaction > firstCardAfterTransaction);
            assertTrue(firstCardAfterTransaction >= 0);
        } else {
            dashboardPage.firstCardOpen();
            var transferPage = new TransferPage();
            transferPage.transaction(String.valueOf(secondCardBalance + 1), secondAccount);

            int firstCardAfterTransaction = dashboardPage.getFirstCardBalance();
            int secondCardAfterTransaction = dashboardPage.getSecondCardBalance();

            assertTrue(firstCardAfterTransaction > secondCardAfterTransaction);
            assertTrue(secondCardAfterTransaction >= 0);
        }
    }

    @Test
    @DisplayName("Transaction with false account")
    public void falseAccountTransaction() {
        var dashboardPage = new DashboardPage();
        dashboardPage.firstCardOpen();
        var transferPage = new TransferPage();
        String falseAccount = "5559 0000 0000 0003";
        transferPage.falseTransaction("1000", falseAccount);
    }
}

