package tests;

import io.restassured.response.Response;
import models.LoginBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;


public class FirstRestApiTest extends BaseTest {

    @Test
    @DisplayName("Проверка получения первой страницы списка пользователей")
    void getUserListTest () {
        Response response = given()
                .log().uri()
                .log().method()
                .when()
                .get("/users?page=1")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/listUserSchema.json"))
                .extract().response();

        assertAll("Проверка полученных данных о списке польователей",
                () -> assertThat(response.path("page"), is(1)),
                () -> assertThat(response.path("total_pages"), is(2))
        );
    }

    @Test
    @DisplayName("Проверка пользователя с id=1")
    void checkUserTest (){
        Response response = given()
                .log().uri()
                .log().method()
                .when()
                .get("/users/1")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        assertAll("Проверка данных пользователя",
                () -> assertThat(response.path("data.id"), is(1)),
                () -> assertThat(response.path("data.first_name"), equalTo("George")),
                () -> assertThat(response.path("data.last_name"), equalTo("Bluth"))
        );
    }

    @Test
    @DisplayName("Проверка получения ошибки 404")
    void checkErrorIfUserNotFound() {
        given()
                .log().uri()
                .log().method()
                .when()
                .get("/users/20")
                .then()
                .log().status()
                .statusCode(404);
    }

    @Test
    @DisplayName("Проверка успешной авторизации")
    void successfulLoginTest () {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setEmail("eve.holt@reqres.in");
        loginData.setPassword("cityslicka");

        Response response = given()
                .log().uri()
                .log().method()
                .contentType(JSON)
                .body(loginData)
                .log().body()
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        assertThat(response.path("token"), is(notNullValue()));
    }

    @Test
    @DisplayName("Проверка не успешной авторизации")
    void failedLoginTest () {
        String responseBody = "{\"email\": \"eve@reqres.in\", \"password\": \"cityslicka\"}";
        Response response = given()
                .log().uri()
                .log().method()
                .contentType(JSON)
                .body(responseBody)
                .log().body()
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .extract().response();

        assertThat(response.path("error"), equalTo("user not found"));
    }

}
