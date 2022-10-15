package user;

import dto.User;
import generatingOfClasses.GeneratingDataOfUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import steps.UserSteps;

import static org.hamcrest.core.IsEqual.equalTo;

@DisplayName("Обновление данных пользователя")
public class UpdateUserDataTest {
    private User updateDataUser;
    private String accessToken;
    public static final String AUTH_ERROR_401 = "You should be authorised";

    @Test
    @DisplayName("Изменение 'email' пользователя с авторизацией")
    @Description("Ожидаемый код ответа: 200")
    public void updateUserEmailWithAuth() throws InterruptedException {
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

        accessToken = UserSteps.getAccessToken(request);
        updateDataUser = GeneratingDataOfUser.editEmailOfUser();
        String expectedEmail = updateDataUser.getEmail();
        Response updateDataOfUser = UserSteps.updateDataOfUser(updateDataUser, accessToken);

        updateDataOfUser.then()
                .statusCode(200)
                .and()
                .assertThat().body("user.email", equalTo(expectedEmail));
    }

    @Test
    @DisplayName("Изменение 'name' пользователя с авторизацией")
    @Description("Ожидаемый код ответа: 200")
    public void updateUserNameWithAuth() throws InterruptedException {
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

        accessToken = UserSteps.getAccessToken(request);
        updateDataUser = GeneratingDataOfUser.editNameOfUser();
        String expectedName = updateDataUser.getName();
        Response updateDataOfUser = UserSteps.updateDataOfUser(updateDataUser, accessToken);

        updateDataOfUser.then()
                .statusCode(200)
                .and()
                .assertThat().body("user.name", equalTo(expectedName));
    }

    @Test
    @DisplayName("Изменение 'password' пользователя с авторизацией")
    @Description("Ожидаемый код ответа: 200")
    public void updateUserPasswordWithAuth() throws InterruptedException {
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

        accessToken = UserSteps.getAccessToken(request);
        updateDataUser = GeneratingDataOfUser.editPasswordOfUser();
        Response updateDataOfUser = UserSteps.updateDataOfUser(updateDataUser, accessToken);

        updateDataOfUser.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));

        request.setPassword(updateDataUser.getPassword());

        loginResponse.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Ожидаемый код ответа: 401")
    public void updateUserDataWithoutAuth() throws InterruptedException {
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

        updateDataUser = GeneratingDataOfUser.editNameOfUser();
        Response updateDataOfUser = UserSteps.updateWithoutAuth(updateDataUser);

        updateDataOfUser.then()
                .statusCode(401)
                .and()
                .assertThat().body("message", equalTo(AUTH_ERROR_401));
    }

}
