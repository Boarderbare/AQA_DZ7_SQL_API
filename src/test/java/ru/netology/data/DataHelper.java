package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.util.Locale;

public class DataHelper {
    public DataHelper() {
    }

    @Value
    public static class UserAuth {
        private String login;
        private String password;
    }

    @Value
    public static class UserInfo {
        private String id;
        private String password;
    }

    @Value
    public static class Card {
        private String id;
        private String number;
        private String balance;
    }

    public static UserAuth getUserAuth() {
        return new UserAuth("vasya", "qwerty123");
    }

    public static UserAuth getFakerUser() {
        Faker faker = new Faker(new Locale("en"));
        return new UserAuth(faker.name().username(), faker.internet().password());
    }

    @SneakyThrows
    public static UserInfo getUserInfo() {

        var runner = new QueryRunner();
        var getId = "SELECT id FROM users WHERE login=?;";
        var getPassword= "SELECT id FROM users WHERE login=?;";

        try (var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass");) {
           String id = runner.query(conn, getId, getUserAuth().getLogin(),new ScalarHandler<>());
           String pass = runner.query(conn, getPassword, getUserAuth().getLogin(),new ScalarHandler<>());
            return new UserInfo(id, pass);
        }

    }

    @SneakyThrows
    public static String getCode() {
        var runner = new QueryRunner();
        var getCode = "SELECT code FROM auth_codes WHERE user_id= (select id from users where login=?) order by created DESC;";

        try (var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass");) {
            return runner.query(conn, getCode, getUserAuth().getLogin(), new ScalarHandler<>());
        }
    }


    @SneakyThrows
    public static void cleanData() {
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