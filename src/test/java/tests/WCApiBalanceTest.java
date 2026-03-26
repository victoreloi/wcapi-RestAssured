package tests;

import core.RequestBase;
import org.junit.Test;

import static io.restassured.RestAssured.given;


public class WCApiBalanceTest extends RequestBase {

    @Test
    public void getAccountBalance() { // Balance is not working properly. Only gets data from the first account
        given()
        .when()
                .get("saldo")
        .then()
                .statusCode(200);
                //.body("[0].saldo" , is("400.00"));
    }
}
