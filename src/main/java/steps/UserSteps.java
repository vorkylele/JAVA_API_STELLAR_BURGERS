package steps;

import dto.User;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import static config.Config.*;

public class UserSteps extends RestClient{

    private String accessToken;

    @DisplayName("Создание пользователя")
    @Step("Создание пользователя")
    public ValidatableResponse createUser(User body) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(body)
                .when()
                .post(AUTH + REGISTER)
                .then().log().ifError();
    }

    @DisplayName("Авторизация пользователя")
    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(User user) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(AUTH + LOGIN)
                .then()
                .log().ifError();

    }

    @DisplayName("Получение access token пользователя")
    @Step("Получение access token пользователя")
    public String getAccessToken(User user) {
        return loginUser(user)
                .extract()
                .path("accessToken");
    }

    @DisplayName("Изменение данных пользователя без авторизации")
    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse updateWithoutAuth(User user) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(AUTH +USER)
                .then()
                .log().ifError();
    }

    @DisplayName("Изменение данных пользователя с авторизацией")
    @Step("Изменение данных пользователя с авторизацией")
    public ValidatableResponse updateDataOfUser(User user, String accessToken) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(AUTH +USER)
                .then()
                .log().ifError();
    }

    @DisplayName("Удаление пользователя")
    @Step("Удаление пользователя")
    public void deleteUser(User user) {
        RestAssured.given()
                .spec(getBaseSpec())
                .auth().oauth2(user.getAccessToken())
                .body(user)
                .when()
                .delete(AUTH +USER)
                .then()
                .assertThat()
                .statusCode(202)
                .log().ifError();
    }
}
