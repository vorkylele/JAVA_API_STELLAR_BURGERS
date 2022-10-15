package generatingOfClasses;

import com.github.javafaker.Faker;
import dto.User;
import io.qameta.allure.Step;

public class GeneratingDataOfUser {
    static Faker faker = new Faker();

    public static User createNewUser() {
        return new User(faker.internet().emailAddress(),
                faker.number().toString(),
                faker.name().firstName());
    }

    public static User createNewUserWithoutEmail() {
        return new User(null, faker.number().toString(),
                faker.name().firstName());
    }

    public static User createNewUserWithoutPassword() {
        return new User(faker.internet().emailAddress(), null,
                faker.name().firstName());
    }

    public static User createNewUserWithoutName() {
        return new User(faker.internet().emailAddress(), faker.number().toString(),
                null);
    }

    @Step("Пользователь для смены почты")
    public static User editEmailOfUser() {
        return new User(faker.internet().emailAddress(), null,
                null);
    }

    @Step("Пользователь для смены имени")
    public static User editNameOfUser() {
        return new User(null, null,
                faker.name().firstName());
    }

    @Step("Пользователь для смены пароля")
    public static User editPasswordOfUser() {
        return new User(null, faker.number().toString(),
                null);
    }
}
