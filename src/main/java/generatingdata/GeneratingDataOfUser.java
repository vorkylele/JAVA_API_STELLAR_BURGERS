package generatingdata;

import com.github.javafaker.Faker;
import dto.User;

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
}
