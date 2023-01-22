package com.vorkylele.tests.order;

import com.github.javafaker.Faker;
import com.vorkylele.api.models.Order;
import com.vorkylele.api.models.User;
import com.vorkylele.api.generatingdata.GeneratingDataOfUser;
import com.vorkylele.steps.OrderSteps;
import com.vorkylele.utils.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import com.vorkylele.steps.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static com.vorkylele.helpers.StatusCodes.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.*;
import static com.vorkylele.helpers.MessageOfResponse.*;


@DisplayName("Создание/получение заказа/заказов")
@Epic("Создание/получение заказа/заказов")
public class OrdersTest extends BaseTest {

    private ValidatableResponse validatableResponse;
    private Order order;

    @Override
    @Before
    public void setUp() throws InterruptedException {
        user = GeneratingDataOfUser.createNewUser();

        response = UserSteps.createUser(user);
        Thread.sleep(2000);

        loginResponse = UserSteps.loginUser((new User(user.getEmail(), user.getPassword(), null)));
        Thread.sleep(2000);
    }

    @Feature("Получение заказов конкретного пользователя c авторизацией")
    @Test
    @DisplayName("Получение заказов конкретного пользователя c авторизацией")
    @Description("Ожидаемый код ответа: 200")
    public void getOrdersWithAuth() {
        accessToken = UserSteps.getAccessToken(user);

        order = new Order(OrderSteps.getIngredients());
        OrderSteps.createOrderWithAuth(order, accessToken);
        validatableResponse = OrderSteps.getOrder(accessToken);
        validatableResponse
                .statusCode(OK_STATUS)
                .body("success", equalTo(true))
                .body("orders", isA(List.class))
                .body("orders.name[0]", endsWith("бургер"))
                .body("orders.number[0]", isA(Integer.class));
    }

    @Feature("Получение заказов конкретного пользователя без авторизации")
    @Test
    @DisplayName("Получение заказов конкретного пользователя без авторизации")
    @Description("Ожидаемый код ответа: 401")
    public void getOrdersWithoutAuth() {
        accessToken = UserSteps.getAccessToken(user);

        order = new Order(OrderSteps.getIngredients());
        OrderSteps.createOrderWithAuth(order, accessToken);

        validatableResponse = OrderSteps.getOrdersWithoutAuth();
        validatableResponse
                .statusCode(UNAUTHORIZED_STATUS)
                .body("success", equalTo(false))
                .body("message", equalTo(AUTH_ERROR_401));
    }

    @Feature("Создание заказа с авторизацией")
    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Ожидаемый код ответа: 200")
    public void createOrderWithAuth() {
        accessToken = UserSteps.getAccessToken(user);

        order = new Order(OrderSteps.getIngredients());
        OrderSteps.createOrderWithAuth(order, accessToken);

        validatableResponse = OrderSteps.createOrderWithAuth(order, accessToken);
        validatableResponse
                .statusCode(OK_STATUS)
                .body("success", equalTo(true))
                .body("name", endsWith("бургер"))
                .body("order.number", isA(Integer.class));
    }

    @Feature("Создание заказа без авторизации")
    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Ожидаемый код ответа: 200")
    public void createOrderWithoutAuth() {
        order = new Order(OrderSteps.getIngredients());

        validatableResponse = OrderSteps.createOrderWithoutAuth(order);
        validatableResponse
                .statusCode(OK_STATUS)
                .body("success", equalTo(true))
                .body("name", endsWith("бургер"))
                .body("order.number", isA(Integer.class));
    }

    @Feature("Создание заказа с ингредиентами")
    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Ожидаемый код ответа: 200")
    public void makeOrderWithIngredients() {
        List<String> allIngredients = (OrderSteps.getIngredients());

        order = new Order(allIngredients);

        validatableResponse = OrderSteps.createOrderWithoutAuth(order);
        validatableResponse
                .statusCode(OK_STATUS)
                .body("success", equalTo(true))
                .body("name", endsWith("бургер"))
                .body("order.number", isA(Integer.class));
    }

    @Feature("Создание заказа без ингредиентов")
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Ожидаемый код ответа: 400")
    public void createOrderWithoutIngredients() {
        List<String> ingredient = new ArrayList<>();
        order = new Order(ingredient);

        validatableResponse = OrderSteps.createOrderWithoutAuth(order);
        validatableResponse
                .statusCode(BAD_REQUEST_STATUS)
                .body("success", equalTo(false))
                .body("message", equalTo(BAD_REQUEST_UNAUTHORIZED_400));
    }

    @Feature("Создание заказа с неверным хешем ингредиентов")
    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Ожидаемый код ответа: 500")
    public void makeOrderWithWrongHashIngredients() {
        String randomTextForHash = Faker.instance().chuckNorris().fact();
        String randomHash = (DigestUtils.md5Hex(randomTextForHash));

        List<String> ingredient = new ArrayList<>();
        ingredient.add(randomHash);

        order = new Order(ingredient);

        validatableResponse = OrderSteps.createOrderWithoutAuth(order);
        validatableResponse
                .statusCode(INTERNAL_SERVER_ERROR_STATUS);
    }
}
