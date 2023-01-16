package com.vorkylele.tests.user;

import com.vorkylele.api.generatingdata.GeneratingDataOfUser;
import com.vorkylele.steps.UserSteps;
import com.vorkylele.utils.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static com.vorkylele.helpers.Assertions.*;
import static com.vorkylele.helpers.StatusCodes.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static com.vorkylele.helpers.MessageOfResponse.*;

@DisplayName("Создание пользователя")
@Epic("Создание пользователя")
public class RegisterOfUserTest extends BaseTest {

    @Feature("Создание нового пользователя с валидными данными")
    @Test
    @DisplayName("Создание нового пользователя с валидными данными")
    @Description("Ожидаемый код ответа: 200")
    public void createUserWithAllValidParams() {
        response = UserSteps.createUser(user);
        response
                .statusCode(OK_STATUS)
                .and()
                .body(ASSERT_SUCCESS, equalTo(true));
    }

    @Feature("Создание двух одинаковых пользователей")
    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    @Description("Ожидаемый код ответа: 403")
    public void createTwoIdenticalUser() throws InterruptedException {
        response = UserSteps.createUser(user);
        Thread.sleep(2000);

        errorResponse = UserSteps.createUser(user);
        errorResponse
                .statusCode(FORBIDDEN_STATUS)
                .and()
                .body(ASSERT_MESSAGE, equalTo(REGISTER_ERROR_EXISTS_403));
    }

    @Feature("Создание нового пользователя с валидными данными, без поля 'email'")
    @Test
    @DisplayName("Создание нового пользователя с валидными данными, без поля 'email'")
    @Description("Ожидаемый код ответа: 403")
    public void createUserWithoutEmail() {
        userModified = GeneratingDataOfUser.createNewUserWithoutEmail();

        response = UserSteps.createUser(userModified);
        response
                .statusCode(FORBIDDEN_STATUS)
                .and()
                .body(ASSERT_MESSAGE, equalTo(REGISTER_ERROR_REQUIRED_403));
    }

    @Feature("Создание нового пользователя с валидными данными, без поля 'password'")
    @Test
    @DisplayName("Создание нового пользователя с валидными данными, без поля 'password'")
    @Description("Ожидаемый код ответа: 403")
    public void createUserWithoutPassword() {
        userModified = GeneratingDataOfUser.createNewUserWithoutPassword();

        response = UserSteps.createUser(userModified);
        response
                .statusCode(FORBIDDEN_STATUS)
                .and()
                .body(ASSERT_MESSAGE, equalTo(REGISTER_ERROR_REQUIRED_403));
    }

    @Feature("Создание нового пользователя с валидными данными, без поля 'name'")
    @Test
    @DisplayName("Создание нового пользователя с валидными данными, без поля 'name'")
    @Description("Ожидаемый код ответа: 403")
    public void createUserWithoutName() {
        userModified = GeneratingDataOfUser.createNewUserWithoutName();

        response = UserSteps.createUser(userModified);
        response
                .statusCode(FORBIDDEN_STATUS)
                .and()
                .body(ASSERT_MESSAGE, equalTo(REGISTER_ERROR_REQUIRED_403));
    }
}
