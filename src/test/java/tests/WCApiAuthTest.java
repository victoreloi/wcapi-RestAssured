package tests;

import core.RequestBase;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class WCApiAuthTest extends RequestBase {

    @Test
    public void loginErrorWithoutToken() {
        given()
                .header("", "")
        .when()
                .get("contas")
        .then()
                .statusCode(401);
    }
}
