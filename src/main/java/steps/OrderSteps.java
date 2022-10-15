package steps;

import config.Config;
import dto.Order;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.List;

public class OrderSteps {
    public RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Content-Type", "application/json")
                .setBaseUri(Config.BASE_URL)
                .build();
    }

    @Step("Получение заказов пользователя с авторизацией")
    public ValidatableResponse getOrdersWithAuth(String accessToken) {
        return (ValidatableResponse) RestAssured.given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get("/orders")
                .then()
                .log().ifError();
    }

    @Step("Получение заказов пользователя без авторизации")
    public ValidatableResponse getOrdersWithoutAuth() {
        return (ValidatableResponse) RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .get("/orders")
                .then()
                .log().ifError();
    }

    @Step("Запрос списка ингредиентов")
    public List<String> getIngredients() {
        return RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .get("/ingredients")
                .then().log().ifError()
                .extract().path("data._id");
    }

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrderWithAuth(Order order, String accessToken) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post("/orders")
                .then()
                .log().ifError();
    }

    @Step("Получение заказа с авторизацией")
    public ValidatableResponse getOrder(String accessToken) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get("/orders")
                .then()
                .log().ifError();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post("/orders")
                .then()
                .log().ifError();
    }
}
