package ru.netology.data;

import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import ru.netology.rest.RestHelper;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    public DataHelper() {
    }

    @Value
    public static class UserAuth {

        private String login;
        private String password;

        public static UserAuth getUserAuth() {
            return new UserAuth("vasya", "qwerty123");
        }
    }

    @Value
    public static class UserVerification {
        private String login;
        private String code;

        public static UserVerification getUserVerification() {
            return new UserVerification(UserAuth.getUserAuth().getLogin(), DataHelper.getCode());
        }
    }

    @Value
    public static class Transaction {

        private String from;
        private String to;
        private int amount;

        public static Transaction getInfoTransaction(int from, int to, int amount) {
            return new Transaction(getCard(from).getNumber(), getCard(to).getNumber(), amount);
        }
    }

    @Value
    public static class Card {
        private String number;
        private int balance;
    }

    public static Card getCard(int orderNumber) {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("5559 0000 0000 0001", getBalance("5559 0000 0000 0001")));
        cards.add(new Card("5559 0000 0000 0002", getBalance("5559 0000 0000 0002")));
        cards.add(new Card("1234 5678 1234 5678", 1000000));
        return cards.get(orderNumber - 1);
    }

    @SneakyThrows
    public static int getBalance(String numberCard) {
        var runner = new QueryRunner();
        var getBalance = "SELECT balance_in_kopecks FROM cards WHERE number = ?;";

        try (var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass");) {
            int balansInKopesks = runner.query(conn, getBalance, numberCard, new ScalarHandler<>());
            return balansInKopesks / 100;
        }
    }

    @SneakyThrows
    public static String getCode() {
        Thread.sleep(100);
        var runner = new QueryRunner();
        var getCode = "SELECT code FROM auth_codes WHERE user_id= (select id from users where login=?) order by created DESC;";

        try (var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass");) {
            return runner.query(conn, getCode, UserAuth.getUserAuth().getLogin(), new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static void resetBalance() {
        var runner = new QueryRunner();
        var setBalance = "UPDATE cards SET balance_in_kopecks = 1000000;";

        try (var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass");) {
            runner.update(conn, setBalance);
        }
    }

    @SneakyThrows
    public static void cleanDataBase() {
        var runner = new QueryRunner();
        var cleanCodes = "delete from auth_codes";
        var cleanCards = "delete from cards";
        var cleanUsers = "delete from users;";

        try (var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass");) {
            runner.update(conn, cleanCodes);
            runner.update(conn, cleanCards);
            runner.update(conn, cleanUsers);
        }
    }
}