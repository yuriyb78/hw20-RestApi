package specs;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;

public class RequestSpec {

    public static RequestSpecification requestSpecification = with()
            .log().uri()
            .log().headers()
            .log().method()
            .contentType(JSON)
            .log().body();
}
