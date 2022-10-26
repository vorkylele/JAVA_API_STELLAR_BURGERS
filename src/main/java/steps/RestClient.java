package steps;

import config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static config.Config.AUTH;

public class RestClient {
    public RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(Config.BASE_URL)
                .build();
    }

    public static final RequestSpecification REQUEST_SPECIFICATION = new RequestSpecBuilder()
            .setBaseUri(Config.BASE_URL)
            .setBasePath(AUTH)
            .setContentType(ContentType.JSON)
            .build();
}
