package tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import core.RequestBase;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class WCApiSmokeTests extends RequestBase {

    private String token;

    @Before
    public void logIn () {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "test_ve@email.com");
        credentials.put("senha", "test_ve");

        token = given()
                .body(credentials)
        .when()
                .post("signin")
        .then()
                .statusCode(200)
                .extract().path("token");
    }

    @Test
    public void loginErrorWithoutToken() {
        given()
        .when()
                .get("contas")
        .then()
                .statusCode(401);
    }

    @Test
    public void addNewAccountSuccessfully () {
        // Post to log in > Post to create account

        Map<String, String> conta = new HashMap<String, String>();
        conta.put("nome","New Account");

        Integer createdAccountId = given()
                .header("Authorization", "JWT " + token)
                .body(conta)
        .when()
                .post("contas")
        .then()
                .statusCode(201)
                .body("nome", is(conta.get("nome")))
                .extract().path("id");

        //Account deletion to avoid numerous amount of accounts created
//        given()
//                
//                .header("Authorization", "JWT " + token)
//        .when()
//                .delete("contas/"  + createdAccountId)
//        .then()
//                
//                .statusCode(200);
    }

     @Test
    public void modifyNewAccountSuccessfully () {
        // Post to log in > Post to create account > Put to validate modifications
        Map<String, String> conta = new HashMap<String, String>();
        conta.put("nome","New Account");

        Integer createdAccountId = given()
                .header("Authorization", "JWT " + token)
                .body(conta)
        .when()
                .post("contas")
        .then()
                .statusCode(201)
                .body("nome", is(conta.get("nome")))
                .extract().path("id");

        // put to modify
        conta.replace("nome", "Changed Account");

        given()
                .header("Authorization", "JWT " + token)
                .body(conta)
        .when()
                .put("contas/" + createdAccountId)
        .then()
                .statusCode(200)
                .body("nome", is(conta.get("nome")))
                .extract().path("id");

        // Account deletion to avoid numerous amount of accounts created
        given()
                .header("Authorization", "JWT " + token)
        .when()
                .delete("contas/"  + createdAccountId)
        .then()
                .statusCode(200);
    }

    @Test
    public void validateIfAddAnExistingAccountFails(){

        Map<String, String> contaExistente = new HashMap<String, String>();
        contaExistente.put("nome","Test Account");

        given()
                .header("Authorization", "JWT " + token)
                .body(contaExistente)
        .when()
                .post("contas")
        .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"));
    }

}
