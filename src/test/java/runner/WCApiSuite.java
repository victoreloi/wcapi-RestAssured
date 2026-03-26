package runner;

import core.RequestBase;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.WCApiAuthTest;
import tests.WCApiBalanceTest;
import tests.WCApiTransactionTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        WCApiSuite.class,
        WCApiTransactionTest.class,
        WCApiBalanceTest.class,
        WCApiAuthTest.class
})
public class WCApiSuite extends RequestBase {

    @BeforeClass
    public static void logIn() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "test_ve@email.com");
        credentials.put("senha", "test_ve");

        String token = given()
                .body(credentials)
                .when()
                .post("signin")
                .then()
                .statusCode(200)
                .extract().path("token");

        requestSpecification.header("Authorization", "JWT " + token);

        //get("reset").then().statusCode(200); // Assuring data mass is set
    } 

}
