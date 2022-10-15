package steps;

import config.Config;
import dto.User;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserSteps {
    public static final RequestSpecification REQUEST_SPECIFICATION = new RequestSpecBuilder()
            .setBaseUri(Config.BASE_URL)
            .setBasePath("/auth")
            .addHeader("Content-Type", "application/json")
            .setContentType(ContentType.JSON)
            .build();

    @Step("Создание пользователя")
    public static Response createUser(User body) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(body)
                .when()
                .post("/register");
    }

    @Step("Авторизация пользователя")
    public static Response loginUser(User body) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(body)
                .when()
                .post("/login");
    }

    @Step("Получение access token пользователя")
    public static String getAccessToken(User body) {
        return loginUser(body)
                .then()
                .extract()
                .path("accessToken");
    }

    @Step("Изменение данных пользователя без авторизации")
    public static Response updateWithoutAuth(User body) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .and()
                .body(body)
                .when()
                .patch("/user");
    }

    @Step("Изменение данных пользователя с авторизацией")
    public static Response updateDataOfUser(User body, String accessToken) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .header("Authorization", accessToken)
                .and()
                .body(body)
                .when()
                .patch("/user");
    }
}
