package order;

import com.github.javafaker.Faker;
import dto.Order;
import dto.User;
import generatingdata.GeneratingDataOfUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.*;

@DisplayName("Создание/получение заказа/заказов")
public class OrdersTest {

    User user;
    private String accessToken;
    private OrderSteps orderSteps = new OrderSteps();
    private UserSteps userSteps = new UserSteps();
    ValidatableResponse response;
    public static final String LOGIN_ERROR_UNAUTHORIZED_401 = "You should be authorised";
    public static final String BAD_REQUEST_UNAUTHORIZED_400 = "Ingredient ids must be provided";

    @Before
    public void setUp() {
        user = GeneratingDataOfUser.createNewUser();
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя c авторизацией")
    @Description("Ожидаемый код ответа: 200")
    public void getOrdersWithAuth() throws InterruptedException {
        response = userSteps.createUser(user);

        response.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        Thread.sleep(2000);

        ValidatableResponse loginResponse = userSteps.loginUser((new User(user.getEmail(), user.getPassword(), null)));

        loginResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));

        accessToken = userSteps.getAccessToken(user);
        Order order = new Order(orderSteps.getIngredients());
        orderSteps.createOrderWithAuth(order, accessToken);
        ValidatableResponse validatableResponse = orderSteps.getOrder(accessToken);
        validatableResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("orders", isA(List.class))
                .body("orders.name[0]", endsWith("бургер"))
                .body("orders.number[0]", isA(Integer.class));
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя без авторизации")
    @Description("Ожидаемый код ответа: 401")
    public void getOrdersWithoutAuth() throws InterruptedException {
        response = userSteps.createUser(user);

        response.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        Thread.sleep(2000);

        ValidatableResponse loginResponse = userSteps.loginUser((new User(user.getEmail(), user.getPassword(), null)));

        loginResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));

        accessToken = userSteps.getAccessToken(user);
        Order order = new Order(orderSteps.getIngredients());
        orderSteps.createOrderWithAuth(order, accessToken);
        ValidatableResponse validatableResponse = orderSteps.getOrdersWithoutAuth();
        validatableResponse.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo(LOGIN_ERROR_UNAUTHORIZED_401));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Ожидаемый код ответа: 200")
    public void createOrderWithAuth() throws InterruptedException {
        response = userSteps.createUser(user);

        response.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        Thread.sleep(2000);

        ValidatableResponse loginResponse = userSteps.loginUser((new User(user.getEmail(), user.getPassword(), null)));

        loginResponse.assertThat()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));

        accessToken = userSteps.getAccessToken(user);
        Order order = new Order(orderSteps.getIngredients());
        orderSteps.createOrderWithAuth(order, accessToken);
        ValidatableResponse validatableResponse = orderSteps.createOrderWithAuth(order, accessToken);
        validatableResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("name", endsWith("бургер"))
                .body("order.number", isA(Integer.class));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Ожидаемый код ответа: 200")
    public void createOrderWithoutAuth() {
        Order order = new Order(orderSteps.getIngredients());
        ValidatableResponse validatableResponse = orderSteps.createOrderWithoutAuth(order);
        validatableResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("name", endsWith("бургер"))
                .body("order.number", isA(Integer.class));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Ожидаемый код ответа: 200")
    public void makeOrderWithIngredients() {
        ArrayList<String> ingredient = new ArrayList<>();
        List<String> allIngredients = (orderSteps.getIngredients());
        for (int i = ThreadLocalRandom.current().nextInt(0, allIngredients.size());
             i <= allIngredients.size(); i += 1) {
            ingredient.add(allIngredients.get
                    (ThreadLocalRandom.current().nextInt(0, allIngredients.size())));
        }
        Order order = new Order(ingredient);
        ValidatableResponse validatableResponse = orderSteps.createOrderWithoutAuth(order);
        validatableResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("name", endsWith("бургер"))
                .body("order.number", isA(Integer.class));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Ожидаемый код ответа: 400")
    public void createOrderWithoutIngredients() {
        ArrayList<String> ingredient = new ArrayList<>();
        Order order = new Order(ingredient);
        ValidatableResponse validatableResponse = orderSteps.createOrderWithoutAuth(order);
        validatableResponse.assertThat()
                .statusCode(400)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo(BAD_REQUEST_UNAUTHORIZED_400));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Ожидаемый код ответа: 500")
    public void makeOrderWithWrongHashIngredients() {
        String randomTextForHash = Faker.instance().chuckNorris().fact();
        String randomHash = (DigestUtils.md5Hex(randomTextForHash));
        ArrayList<String> ingredient = new ArrayList<>();
        ingredient.add(randomHash);
        Order order = new Order(ingredient);
        ValidatableResponse validatableResponse = orderSteps.createOrderWithoutAuth(order);
        validatableResponse.assertThat()
                .statusCode(500);
    }

    @After
    public void deleteData() {
        if (response != null) {
            String token = User.getAccessToken(response);
            user.setAccessToken(token);
            userSteps.deleteUser(user);
        }
    }
}
