package tests;

import io.qameta.allure.Feature;
import io.restassured.response.Response;
import models.LoginBodyModel;
import models.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static specs.RequestSpec.*;


public class ReqresLoginRestApiTest extends BaseTest {

    @Test
    @Tag("Login tests")
    @Feature("Тесты для авторизации на сайте https://reqres.in/")
    @DisplayName("Проверка успешной авторизации")
    void successfulLoginTest () {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setEmail("eve.holt@reqres.in");
        loginData.setPassword("cityslicka");

        LoginResponseModel response = step("Отправка запроса на авторизацию", () ->
                given(requestSpec)
                .body(loginData)
                .when()
                .post("/login")
                .then()
                .spec(successfulResponseSpec)
                .extract().as(LoginResponseModel.class)
        );

        step("Проверка получения токена", () -> assertThat(response.getToken(), is(notNullValue())));
    }

    @Test
    @Tag("Login tests")
    @Feature("Тесты для авторизации на сайте https://reqres.in/")
    @DisplayName("Проверка не успешной авторизации")
    void failedLoginTest () {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setEmail("eve@reqres.in");
        loginData.setPassword("cityslicka");

        Response response = step("Отправка запроса на авторизацию", () ->
                given(requestSpec)
                .body(loginData)
                .when()
                .post("/login")
                .then()
                .spec(failedLoginResponseSpec)
                .extract().response()
        );

        step("Проверка получения сообщения \"user not found\"", () ->assertThat(response.path("error"), equalTo("user not found")));
    }

}
