package user;

import dto.User;
import generatingOfClasses.GeneratingDataOfUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import steps.UserSteps;

import static org.hamcrest.core.IsEqual.equalTo;

@DisplayName("Создание пользователя")
public class RegisterOfUserTest {

    public static final String REGISTER_ERROR_EXISTS_403 = "User already exists";
    public static final String REGISTER_ERROR_REQUIRED_403 = "Email, password and name are required fields";

    @Test
    @DisplayName("Создание нового пользователя с валидными данными")
    @Description("Ожидаемый код ответа: 200")
    public void createUserWithAllValidParams() throws InterruptedException {
        User request = GeneratingDataOfUser.createNewUser();
        Response response = UserSteps.createUser(request);

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    @Description("Ожидаемый код ответа: 403")
    public void createTwoIdenticalUser() throws InterruptedException {
        User request = GeneratingDataOfUser.createNewUser();
        Response response = UserSteps.createUser(request);

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
        Thread.sleep(2000);

        Response errorResponse = UserSteps.createUser(request);
        errorResponse.then()
                .statusCode(403)
                .and()
                .assertThat().body("message", equalTo(REGISTER_ERROR_EXISTS_403));
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Создание нового пользователя с валидными данными, без поля 'email")
    @Description("Ожидаемый код ответа: 403")
    public void createUserWithoutEmail() throws InterruptedException {
        User request = GeneratingDataOfUser.createNewUserWithoutEmail();
        Response response = UserSteps.createUser(request);

        response.then()
                .statusCode(403)
                .and()
                .assertThat().body("message", equalTo(REGISTER_ERROR_REQUIRED_403));
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Создание нового пользователя с валидными данными, без поля 'password")
    @Description("Ожидаемый код ответа: 403")
    public void createUserWithoutPassword() throws InterruptedException {
        User request = GeneratingDataOfUser.createNewUserWithoutPassword();
        Response response = UserSteps.createUser(request);

        response.then()
                .statusCode(403)
                .and()
                .assertThat().body("message", equalTo(REGISTER_ERROR_REQUIRED_403));
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Создание нового пользователя с валидными данными, без поля 'name")
    @Description("Ожидаемый код ответа: 403")
    public void createUserWithoutName() throws InterruptedException {
        User request = GeneratingDataOfUser.createNewUserWithoutName();
        Response response = UserSteps.createUser(request);

        response.then()
                .statusCode(403)
                .and()
                .assertThat().body("message", equalTo(REGISTER_ERROR_REQUIRED_403));
        Thread.sleep(2000);
    }
}
