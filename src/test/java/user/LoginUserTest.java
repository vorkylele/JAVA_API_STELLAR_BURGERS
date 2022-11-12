package user;

import com.github.javafaker.Faker;
import dto.User;
import generatingdata.GeneratingDataOfUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static org.hamcrest.core.IsEqual.equalTo;

@DisplayName("Логин пользователя")
public class LoginUserTest {

    User user;
    private UserSteps userSteps = new UserSteps();
    ValidatableResponse response;
    public static final String LOGIN_ERROR_UNAUTHORIZED_401 = "email or password are incorrect";

    @Before
    public void setUp() {
        user = GeneratingDataOfUser.createNewUser();
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Ожидаемый код ответа: 200")
    public void loginUserWithAllValidParams() throws InterruptedException {
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
    }

    @Test
    @DisplayName("Авторизация несуществующего пользователя")
    @Description("Ожидаемый код ответа: 401")
    public void loginNonExistentUser() {
        Faker faker = new Faker();
        ValidatableResponse loginResponse = userSteps.loginUser((new User(faker.internet().emailAddress(), faker.number().toString(), null)));

        loginResponse.assertThat()
                .statusCode(401)
                .and()
                .assertThat().body("message", equalTo(LOGIN_ERROR_UNAUTHORIZED_401));
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
