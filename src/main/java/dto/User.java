package dto;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String email;
    private String password;
    private String name;

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
}
