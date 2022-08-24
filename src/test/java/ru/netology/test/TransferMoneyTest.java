package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.rest.RestHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransferMoneyTest {
    @BeforeEach
    public void resetBalance() {
        DataHelper.resetBalance();
    }

    @AfterAll
    public static void cleanDataBase() {
        DataHelper.cleanDataBase();
    }

    @Test
    void shouldTransferFromFirstCardToSecond() {
        RestHelper.login();
        var token = RestHelper.getToken();
        var amountTransfer = 1;
        var numberCartFrom = 1;
        var numberCartTo = 2;
        var infoTransaction = DataHelper.Transaction.getInfoTransaction(numberCartFrom, numberCartTo, amountTransfer);
        var cardFirstBeforeTransfer = DataHelper.getCard(numberCartFrom).getBalance();
        var cardSecondBeforeTransfer = DataHelper.getCard(numberCartTo).getBalance();
        RestHelper.transferCardToCard(token,infoTransaction);
        var cardBalanceFirstAfterTransfer = DataHelper.getCard(numberCartFrom).getBalance();
        var cardBalanceSecondAfterTransfer = DataHelper.getCard(numberCartTo).getBalance();

        assertEquals(cardFirstBeforeTransfer, cardBalanceFirstAfterTransfer + amountTransfer);
        assertEquals(cardSecondBeforeTransfer, cardBalanceSecondAfterTransfer - amountTransfer);
        assertTrue(cardBalanceSecondAfterTransfer > 0 && cardBalanceFirstAfterTransfer > 0);
    }

    @Test
    void shouldTransferFromSecondCardToFirst() {
        RestHelper.login();
        var token = RestHelper.getToken();
        var amountTransfer = 9999;
        var numberCartFrom = 2;
        var numberCartTo = 1;
        var infoTransaction = DataHelper.Transaction.getInfoTransaction(numberCartFrom, numberCartTo, amountTransfer);
        var cardFirstBeforeTransfer = DataHelper.getCard(numberCartTo).getBalance();
        var cardSecondBeforeTransfer = DataHelper.getCard(numberCartFrom).getBalance();
        RestHelper.transferCardToCard(token,infoTransaction);
        var cardBalanceFirstAfterTransfer = DataHelper.getCard(numberCartTo).getBalance();
        var cardBalanceSecondAfterTransfer = DataHelper.getCard(numberCartFrom).getBalance();

        assertEquals(cardFirstBeforeTransfer, cardBalanceFirstAfterTransfer - amountTransfer);
        assertEquals(cardSecondBeforeTransfer, cardBalanceSecondAfterTransfer + amountTransfer);
        assertTrue(cardBalanceSecondAfterTransfer > 0 && cardBalanceFirstAfterTransfer > 0);
    }

    @Test
    void shouldNotTransferCardNoExistFromUser() {
        RestHelper.login();
        var token = RestHelper.getToken();
        var amountTransfer = 9999;
        var numberCartFrom = 3;
        var numberCartTo = 1;
        var infoTransaction = DataHelper.Transaction.getInfoTransaction(numberCartFrom, numberCartTo, amountTransfer);
        RestHelper.transferCardToCard(token,infoTransaction);
    }

    @Test
    void shouldNotTransferNotEnoughFounds() {
        RestHelper.login();
        var token = RestHelper.getToken();
        var amountTransfer = 100001;
        var numberCartFrom = 2;
        var numberCartTo = 1;
        var infoTransaction = DataHelper.Transaction.getInfoTransaction(numberCartFrom, numberCartTo, amountTransfer);
        RestHelper.transferCardToCard(token,infoTransaction);
    }
}

