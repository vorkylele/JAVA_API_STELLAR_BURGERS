package deleteuser;

import steps.UserSteps;

public class deleteUser {

    protected static String accessToken;
    protected static UserSteps userSteps;

    public void deleteUser() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken)
                    .then()
                    .statusCode(202);
        }
        accessToken = null;
    }
}
