package user;

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

@DisplayName("Создание пользователя")
public class RegisterOfUserTest {

    User user;
    private UserSteps userSteps = new UserSteps();
    ValidatableResponse response;
    public static final String REGISTER_ERROR_EXISTS_403 = "User already exists";
    public static final String REGISTER_ERROR_REQUIRED_403 = "Email, password and name are required fields";

    @Before
    public void setUp() {
        user = GeneratingDataOfUser.createNewUser();
    }

    @Test
    @DisplayName("Создание нового пользователя с валидными данными")
    @Description("Ожидаемый код ответа: 200")
    public void createUserWithAllValidParams() throws InterruptedException {
        response = userSteps.createUser(user);

        response.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        Thread.sleep(2000);

    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    @Description("Ожидаемый код ответа: 403")
    public void createTwoIdenticalUser() throws InterruptedException {
        response = userSteps.createUser(user);

        response.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        Thread.sleep(2000);

        ValidatableResponse errorResponse = userSteps.createUser(user);
        errorResponse.assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo(REGISTER_ERROR_EXISTS_403));
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Создание нового пользователя с валидными данными, без поля 'email")
    @Description("Ожидаемый код ответа: 403")
    public void createUserWithoutEmail() throws InterruptedException {
        User request = GeneratingDataOfUser.createNewUserWithoutEmail();
        response = userSteps.createUser(request);

        response.assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo(REGISTER_ERROR_REQUIRED_403));
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Создание нового пользователя с валидными данными, без поля 'password")
    @Description("Ожидаемый код ответа: 403")
    public void createUserWithoutPassword() throws InterruptedException {
        User request = GeneratingDataOfUser.createNewUserWithoutPassword();
        response = userSteps.createUser(request);

        response.assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo(REGISTER_ERROR_REQUIRED_403));
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Создание нового пользователя с валидными данными, без поля 'name")
    @Description("Ожидаемый код ответа: 403")
    public void createUserWithoutName() throws InterruptedException {
        User request = GeneratingDataOfUser.createNewUserWithoutName();
        response = userSteps.createUser(request);

        response.assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo(REGISTER_ERROR_REQUIRED_403));
        Thread.sleep(2000);
    }

    @After
    public void deleteData() {
        if (response.extract().body().path("success").equals(true)) {
            String accessToken = User.getAccessToken(response);
            user.setAccessToken(accessToken);
            userSteps.deleteUser(user);
        }
    }
}
