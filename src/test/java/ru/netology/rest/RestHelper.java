package ru.netology.rest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.netology.data.DataHelper;


import static io.restassured.RestAssured.given;

public class RestHelper {
    private final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public void login() {
        given()
                .spec(requestSpec)
                .body(DataHelper.UserAuth.getUserAuth())
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);
    }

    public String getToken() {
        Response response =
                given()
                        .spec(requestSpec)
                        .body(DataHelper.UserVerification.getUserVerification())
                        .when()
                        .post("api/auth/verification")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        return response.path("token");
    }

    public void transferCardToCard(DataHelper.Transaction info) {
        given()
                .spec(requestSpec)
                .auth().oauth2(getToken())
                .body(info)
                .when()
                .post("/api/transfer")
                .then()
                .statusCode(200);
    }

    public void transferErrorCardToCard(DataHelper.Transaction info) {
        given()
                .spec(requestSpec)
                .auth().oauth2(getToken())
                .body(info)
                .when()
                .post("/api/transfer")
                .then()
                .statusCode(404);
    }

//    public List<DataHelper.Card> checkCards() {
//        List<DataHelper.Card> cards =
//                given()
//                        .spec(requestSpec)
//                        .auth().oauth2(getToken())
//                        .when()
//                        .get("api/cards")
//                        .then()
//                        .statusCode(200)
//                        .extract()
//                        .as(ArrayList<DataHelper.Card>());
//        return cards;
//    }

}
