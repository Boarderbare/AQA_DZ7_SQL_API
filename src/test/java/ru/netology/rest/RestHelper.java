package ru.netology.rest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;
import ru.netology.data.DataHelper;

import static io.restassured.RestAssured.given;

@UtilityClass
public class RestHelper {

    private final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void login(DataHelper.UserAuth user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);
    }

    public static String getToken(DataHelper.UserVerification info) {
        Response response =
                given()
                        .spec(requestSpec)
                        .body(info)
                        .when()
                        .post("api/auth/verification")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();
        return response.path("token");
    }

    public static void transferCardToCard(String token, DataHelper.Transaction info) {
        given()
                .spec(requestSpec)
                .auth().oauth2(token)
                .body(info)
                .when()
                .post("/api/transfer")
                .then()
                .statusCode(200);
    }

    public static void transferErrorCardToCard(String token, DataHelper.Transaction info) {
        given()
                .spec(requestSpec)
                .auth().oauth2(token)
                .body(info)
                .when()
                .post("/api/transfer")
                .then()
                .statusCode(404);
    }
}
