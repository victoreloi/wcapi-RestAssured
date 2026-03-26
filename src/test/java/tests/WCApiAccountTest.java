package tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static utils.Util.getCurrentDate;

import core.Constants;
import core.RequestBase;
import entities.Transaction;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class WCApiAccountTest extends RequestBase {

    @Test
    public void addNewAccountSuccessfully () {
        Integer createdAccount = validAccountId();
        given()
        .when()
                .get("contas/" + createdAccount)
        .then()
                .body("id", is(createdAccount));
    }

     @Test
    public void modifyAccountSuccessfully () {
        Integer createdAccount = validAccountId();

        Map<String, String> account = new HashMap<String, String>();
        account.put("nome","Account Modified - " + System.nanoTime());

        given()
                .body(account)
        .when()
                .put("contas/" + createdAccount)
        .then()
                .statusCode(200)
                .body("nome", is(account.get("nome")))
                .extract().path("id");
    }

    @Test
    public void validateIfAddAnExistingAccountFails(){
        Map<String, String> existingAccount = new HashMap<String, String>();
        existingAccount.put("nome","Conta mesmo nome");

        given()
                .body(existingAccount)
        .when()
                .post("contas")
        .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"));
    }

    private Transaction getValidTransaction() {
        Transaction transaction =  new Transaction();
        transaction.setConta_id(Constants.PROD_ACCOUNT_ID);
        transaction.setDescricao("Test transaction");
        transaction.setTipo("REC");
        transaction.setData_transacao(getCurrentDate());
        transaction.setData_pagamento(getCurrentDate());
        transaction.setEnvolvido("Interested Part");
        transaction.setValor(200f);
        transaction.setStatus(true);
        return transaction;
    }

    private Integer validAccountId() {
        Map<String, String> account = new HashMap<String, String>();
        account.put("nome","Account Generated - "  + System.nanoTime());

        return given()
                .body(account)
        .when()
                .post("contas")
        .then()
                .statusCode(201)
                .body("nome", is(account.get("nome")))
                .extract().path("id");
    }



}
