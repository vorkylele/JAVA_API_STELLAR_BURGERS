package user;

import com.github.javafaker.Faker;
import deleteuser.deleteUser;
import dto.User;
import generatingOfClasses.GeneratingDataOfUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import steps.UserSteps;

import static org.hamcrest.core.IsEqual.equalTo;

@DisplayName("Логин пользователя")
public class LoginUserTest extends deleteUser {

    public static final String LOGIN_ERROR_UNAUTHORIZED_401 = "email or password are incorrect";

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Ожидаемый код ответа: 200")
    public void loginUserWithAllValidParams() throws InterruptedException {
        User request = GeneratingDataOfUser.createNewUser();
        Response response = UserSteps.createUser(request);

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
        Thread.sleep(2000);

        Response loginResponse = UserSteps.loginUser((new User(request.getEmail(), request.getPassword(), null)));

        loginResponse.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация несуществующего пользователя")
    @Description("Ожидаемый код ответа: 401")
    public void loginNonExistentUser() {
        Faker faker = new Faker();
        Response loginResponse = UserSteps.loginUser((new User(faker.internet().emailAddress(), faker.number().toString(), null)));

        loginResponse.then()
                .statusCode(401)
                .and()
                .assertThat().body("message", equalTo(LOGIN_ERROR_UNAUTHORIZED_401));
    }

    @After
    public void deleteData() {
        deleteUser();
    }
}
