package tests;

import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static specs.RequestSpec.*;


public class ReqresRestApiTest extends BaseTest {

    @Test
    @Tag("Other tests")
    @Feature("Различные тесты на сайте https://reqres.in/")
    @DisplayName("Проверка получения первой страницы списка пользователей")
    void getUserListTest () {
        Response response = step("Отправка запроса на авторизацию", () ->
                given(requestSpec)
                .when()
                .get("/users?page=1")
                .then()
                        .spec(successfulResponseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/listUserSchema.json"))
                .extract().response()
        );

        step("Проверка данных пользователя", () ->
                assertAll("Проверка полученных данных о списке польователей",
                        () -> assertThat(response.path("page"), is(1)),
                        () -> assertThat(response.path("total_pages"), is(2))
        ));
    }

    @Test
    @Tag("Other tests")
    @Feature("Различные тесты на сайте https://reqres.in/")
    @DisplayName("Проверка пользователя с id=1")
    void checkUserTest (){
        Response response = step("Отправка запроса на авторизацию", () ->
                given(requestSpec)
                .when()
                .get("/users/1")
                .then()
                .spec(successfulResponseSpec)
                .extract().response()
        );

        step("Проверка данных пользователя", () ->
                assertAll("Проверка данных пользователя",
                        () -> assertThat(response.path("data.id"), is(1)),
                        () -> assertThat(response.path("data.first_name"), equalTo("George")),
                        () -> assertThat(response.path("data.last_name"), equalTo("Bluth"))
        )        );
    }

    @Test
    @Tag("Other tests")
    @Feature("Различные тесты на сайте https://reqres.in/")
    @DisplayName("Проверка получения ошибки 404")
    void checkErrorIfUserNotFound() {
        step("Отправка запроса на авторизацию", () ->
                given(requestSpec)
                .when()
                .get("/users/20")
                .then()
                .spec(errorResponseSpec)
        );

    }

}
