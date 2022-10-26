package steps;

import dto.User;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;

import static config.Config.*;
import static io.restassured.RestAssured.given;

public class UserSteps extends RestClient{

    private String accessToken;

    @DisplayName("Создание пользователя")
    @Step("Создание пользователя")
    public static Response createUser(User body) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(body)
                .when()
                .post(REGISTER);
    }

    @DisplayName("Авторизация пользователя")
    @Step("Авторизация пользователя")
    public static Response loginUser(User body) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(body)
                .when()
                .post(LOGIN);
    }

    @DisplayName("Получение access token пользователя")
    @Step("Получение access token пользователя")
    public static String getAccessToken(User body) {
        return loginUser(body)
                .then()
                .extract()
                .path("accessToken");
    }

    @DisplayName("Изменение данных пользователя без авторизации")
    @Step("Изменение данных пользователя без авторизации")
    public static Response updateWithoutAuth(User body) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .and()
                .body(body)
                .when()
                .patch(USER);
    }

    @DisplayName("Изменение данных пользователя с авторизацией")
    @Step("Изменение данных пользователя с авторизацией")
    public static Response updateDataOfUser(User body, String accessToken) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .header("Authorization", accessToken)
                .and()
                .body(body)
                .when()
                .patch(USER);
    }

    @DisplayName("Удаление пользователя")
    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .header("Authorization", accessToken)
                .when()
                .delete(USER);
    }
}
