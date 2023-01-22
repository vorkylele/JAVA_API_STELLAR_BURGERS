package com.vorkylele.tests.user;

import com.github.javafaker.Faker;
import com.vorkylele.api.models.User;
import com.vorkylele.api.generatingdata.GeneratingDataOfUser;
import com.vorkylele.utils.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import com.vorkylele.steps.UserSteps;

import static com.vorkylele.helpers.Assertions.*;
import static com.vorkylele.helpers.StatusCodes.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static com.vorkylele.helpers.MessageOfResponse.*;

@DisplayName("Логин пользователя")
@Epic("Логин пользователя")
public class LoginUserTest extends BaseTest {

    @Override
    @Before
    public void setUp() throws InterruptedException {
        user = GeneratingDataOfUser.createNewUser();

        response = UserSteps.createUser(user);
        Thread.sleep(2000);
    }

    @Feature("Авторизация пользователя")
    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Ожидаемый код ответа: 200")
    public void loginUserWithAllValidParams() {
        loginResponse = UserSteps.loginUser((new User(user.getEmail(), user.getPassword(), null)));
        loginResponse
                .statusCode(OK_STATUS)
                .body(ASSERT_SUCCESS, equalTo(true));
    }

    @Feature("Авторизация несуществующего пользователя")
    @Test
    @DisplayName("Авторизация несуществующего пользователя")
    @Description("Ожидаемый код ответа: 401")
    public void loginNonExistentUser() {
        loginResponse = UserSteps.loginUser((new User(Faker.instance().internet().emailAddress(), Faker.instance().number().toString(), null)));
        loginResponse.assertThat()
                .statusCode(UNAUTHORIZED_STATUS)
                .assertThat().body("message", equalTo(LOGIN_ERROR_UNAUTHORIZED_401));
    }
}
