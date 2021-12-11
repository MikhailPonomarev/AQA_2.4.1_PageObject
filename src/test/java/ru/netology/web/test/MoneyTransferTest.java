package ru.netology.web.test;

import static org.junit.jupiter.api.Assertions.*;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static ru.netology.web.data.DataHelper.*;
import static com.codeborne.selenide.Selenide.*;

class MoneyTransferTest {

    @BeforeEach
    public void setUp() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("Transaction from first card to second")
    public void transactionFromFirstCard() {
        var dashboardPage = new DashboardPage();
        int firstCardBalance = dashboardPage.getFirstCardBalance();
        int secondCardBalance = dashboardPage.getSecondCardBalance();

        dashboardPage.secondCardOpen();
        val transferAmount = 4000;
        var transferPage = new TransferPage();
        transferPage.transaction(String.valueOf(transferAmount), getFirstCard());

        int firstCardAfterTransaction = dashboardPage.getFirstCardBalance();
        int secondCardAfterTransaction = dashboardPage.getSecondCardBalance();

        assertEquals(firstCardBalance - transferAmount, firstCardAfterTransaction);
        assertEquals(secondCardBalance + transferAmount, secondCardAfterTransaction);
    }

    @Test
    @DisplayName("Transaction from second card to first")
    public void transactionFromSecondCard() {
        var dashboardPage = new DashboardPage();
        int firstCardBalance = dashboardPage.getFirstCardBalance();
        int secondCardBalance = dashboardPage.getSecondCardBalance();

        dashboardPage.firstCardOpen();
        var transferPage = new TransferPage();
        val transferAmount = 8000;
        transferPage.transaction(String.valueOf(transferAmount), getSecondCard());

        int firstCardAfterTransaction = dashboardPage.getFirstCardBalance();
        int secondCardAfterTransaction = dashboardPage.getSecondCardBalance();

        assertEquals(firstCardBalance + transferAmount, firstCardAfterTransaction);
        assertEquals(secondCardBalance - transferAmount, secondCardAfterTransaction);
    }


    @Test
    @DisplayName("Transaction over limit from first card")
    public void transferOverLimitFromFirstCard() {
        var dashboardPage = new DashboardPage();
        int firstCardBalance = dashboardPage.getFirstCardBalance();

        dashboardPage.secondCardOpen();
        var transferPage = new TransferPage();
        transferPage.transaction(String.valueOf(firstCardBalance + 1), getFirstCard());

        int firstCardAfterTransaction = dashboardPage.getFirstCardBalance();
        int secondCardAfterTransaction = dashboardPage.getSecondCardBalance();

        assertTrue(secondCardAfterTransaction > firstCardAfterTransaction);
        assertTrue(firstCardAfterTransaction >= 0);
    }

    @Test
    @DisplayName("Transaction over limit from second card")
    public void transactionOverLimitFromSecondCard() {
        var dashboardPage = new DashboardPage();
        int secondCardBalance = dashboardPage.getSecondCardBalance();

        dashboardPage.firstCardOpen();
        var transferPage = new TransferPage();
        transferPage.transaction(String.valueOf(secondCardBalance + 1), getSecondCard());

        int firstCardAfterTransaction = dashboardPage.getFirstCardBalance();
        int secondCardAfterTransaction = dashboardPage.getSecondCardBalance();

        assertTrue(firstCardAfterTransaction > secondCardAfterTransaction);
        assertTrue(secondCardAfterTransaction >= 0);
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
    @DisplayName("Transaction with false card number")
    public void falseCardTransaction() {
        var dashboardPage = new DashboardPage();
        dashboardPage.firstCardOpen();
        var transferPage = new TransferPage();
        transferPage.transaction("1000", getFalseCard());
    }
}

