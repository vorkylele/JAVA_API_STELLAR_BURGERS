package steps;

import dto.Order;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import java.util.List;

import static config.Config.INGREDIENTS;
import static config.Config.ORDERS;

public class OrderSteps extends RestClient{

    @DisplayName("Получение заказов пользователя без авторизации")
    @Step("Получение заказов пользователя без авторизации")
    public ValidatableResponse getOrdersWithoutAuth() {
        return (ValidatableResponse) RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS)
                .then()
                .log().ifError();
    }

    @DisplayName("Запрос списка ингредиентов")
    @Step("Запрос списка ингредиентов")
    public List<String> getIngredients() {
        return RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS)
                .then()
                .log().ifError()
                .extract().path("data._id");
    }

    @DisplayName("Создание заказа с авторизацией")
    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrderWithAuth(Order order, String accessToken) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS)
                .then()
                .log().ifError();
    }

    @DisplayName("Получение заказа с авторизацией")
    @Step("Получение заказа с авторизацией")
    public ValidatableResponse getOrder(String accessToken) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS)
                .then()
                .log().ifError();
    }

    @DisplayName("Создание заказа без авторизации")
    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDERS)
                .then()
                .log().ifError();
    }
}
