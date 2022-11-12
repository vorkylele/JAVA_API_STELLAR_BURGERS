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

@DisplayName("Обновление данных пользователя")
public class UpdateUserDataTest {

    User user;
    private UserSteps userSteps = new UserSteps();
    ValidatableResponse response;
    private User updateDataUser;
    private String accessToken;
    public static final String AUTH_ERROR_401 = "You should be authorised";

    @Before
    public void setUp() {
        user = GeneratingDataOfUser.createNewUser();
    }

    @Test
    @DisplayName("Изменение 'email' пользователя с авторизацией")
    @Description("Ожидаемый код ответа: 200")
    public void updateUserEmailWithAuth() throws InterruptedException {
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
        updateDataUser = User.editEmailOfUser();
        String expectedEmail = updateDataUser.getEmail();
        ValidatableResponse updateDataOfUser = userSteps.updateDataOfUser(updateDataUser, accessToken);

        updateDataOfUser.assertThat()
                .statusCode(200)
                .and()
                .body("user.email", equalTo(expectedEmail));
    }

    @Test
    @DisplayName("Изменение 'name' пользователя с авторизацией")
    @Description("Ожидаемый код ответа: 200")
    public void updateUserNameWithAuth() throws InterruptedException {
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
        updateDataUser = User.editNameOfUser();
        String expectedName = updateDataUser.getName();
        ValidatableResponse updateDataOfUser = userSteps.updateDataOfUser(updateDataUser, accessToken);

        updateDataOfUser.assertThat()
                .statusCode(200)
                .and()
                .body("user.name", equalTo(expectedName));
    }

    @Test
    @DisplayName("Изменение 'password' пользователя с авторизацией")
    @Description("Ожидаемый код ответа: 200")
    public void updateUserPasswordWithAuth() throws InterruptedException {
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
        updateDataUser = User.editPasswordOfUser();
        ValidatableResponse updateDataOfUser = userSteps.updateDataOfUser(updateDataUser, accessToken);

        updateDataOfUser.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));

        user.setPassword(updateDataUser.getPassword());

        loginResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Ожидаемый код ответа: 401")
    public void updateUserDataWithoutAuth() throws InterruptedException {
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

        updateDataUser = User.editNameOfUser();
        ValidatableResponse updateDataOfUser = userSteps.updateWithoutAuth(updateDataUser);

        updateDataOfUser.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo(AUTH_ERROR_401));
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
