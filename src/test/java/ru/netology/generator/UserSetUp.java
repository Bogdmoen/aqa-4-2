package ru.netology.generator;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.*;
import ru.netology.domain.UserInfo;

import java.util.Locale;

public class UserSetUp {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static Faker faker = new Faker(new Locale("eng"));

    public static void postUser (UserInfo registration) {
        RestAssured.given()
                .spec(requestSpec)
                .body(registration)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static UserInfo setActiveUser() {
        String login = faker.name().firstName();
        String password = faker.bothify("??####?##");
        String status = "active";
        UserInfo userInfo = new UserInfo(login, password, status);
        postUser(userInfo);
        return userInfo;
    }

    public static UserInfo setBlockedUser() {
        String login = faker.name().firstName();
        String password = faker.bothify("??####?##");
        String status = "blocked";
        UserInfo userInfo = new UserInfo(login, password, status);
        postUser(userInfo);
        return userInfo;
    }

}
