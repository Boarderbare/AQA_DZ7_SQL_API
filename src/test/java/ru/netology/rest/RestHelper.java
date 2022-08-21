package ru.netology.rest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.netology.data.DataHelper;

import static io.restassured.RestAssured.given;

public class RestHelper {
    private RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public void login() {
        given()
                .spec(requestSpec)
                .pathParam("login", DataHelper.getUserAuth().getLogin())
                .pathParam("password", DataHelper.getUserAuth().getPassword())
                .when()
                .post("/api/auth/{login}/{password}")
                .then()
                .statusCode(200);
    }

    public String getToken() {
        Response response =
                given()
                        .spec(requestSpec)
                        .pathParam("login", DataHelper.getUserAuth().getLogin())
                        .pathParam("password", DataHelper.getCode())
                        .when()
                        .post("api/auth/verification/{login}/{password}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();
        return response.path("token");
    }

    void checkBalance() {
    }

    void transferCardToCard() {
    }
}
