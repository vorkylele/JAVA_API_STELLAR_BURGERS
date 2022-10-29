package dto;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import lombok.Data;

@Data
public class User {
    private String email;
    private String password;
    private String name;
    public String accessToken;

    public User() {
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Step("Пользователь для смены почты")
    public static User editEmailOfUser() {
        return new User(Faker.instance().internet().emailAddress(), null,
                null);
    }

    @Step("Пользователь для смены имени")
    public static User editNameOfUser() {
        return new User(null, null,
                Faker.instance().name().firstName());
    }

    @Step("Пользователь для смены пароля")
    public static User editPasswordOfUser() {
        return new User(null, Faker.instance().internet().password(),
                null);
    }

    public static String getAccessToken(ValidatableResponse validatableResponse) {
        return validatableResponse.extract().path("accessToken").toString().substring(7);

    }
}
