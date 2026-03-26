package tests;

import core.Constants;
import core.RequestBase;
import entities.Transaction;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static utils.Util.getCurrentDate;
import static utils.Util.getFutureDate;

public class WCApiTransactionTest extends RequestBase {


    @Test
    public void addNewTransactionSuccessfully(){
        Transaction transaction = getValidTransaction();

        given()
                .body(transaction)
        .when()
                .post("transacoes")
        .then()
                .statusCode(201);
    }

    @Test
    public void validateTransactionNotSendingMandatoryFields(){
        given()
                .body("{}")
        .when()
                .post("transacoes")
        .then()
                .statusCode(400)
                .body("$", hasSize(8)) // Check the amount of mandatory field based on the root
                .body("msg", hasItems("Data da Movimentação é obrigatório",
                        "Data do pagamento é obrigatório",
                        "Conta é obrigatório",
                        "Descrição é obrigatório",
                        "Interessado é obrigatório",
                        "Valor é obrigatório",
                        "Valor deve ser um número",
                        "Situação é obrigatório"));
    }

    @Test
    public void notAddingFutureTransaction(){
        Transaction transaction = getValidTransaction();
        transaction.setData_transacao(getFutureDate(7));

        given()
                .body(transaction)
        .when()
                .post("transacoes")
        .then()
                .statusCode(400)
                .body("$", hasSize(1))
                .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"));
    }

    @Test
    public void accountHavingActiveTransactionMustNoBeDeleted() {
        // Account deletion to avoid numerous amount of accounts created
        given()
        .when()
                .delete("contas/"  + PROD_ACCOUNT_ID)
        .then()
                .statusCode(500) // Error is not being predicted by the application
                .body("name",is("error"))
                .body("constraint", is("transacoes_conta_id_foreign"));
    }

    @Test
    public void deleteTransaction() {
        Transaction transaction = getValidTransaction();

        Integer createdTransaction = given()
                .body(transaction)
        .when()
                .post("transacoes")
        .then()
                .statusCode(201)
                .extract().path("id");

        given()
        .when()
                .delete("transacoes/" + createdTransaction)
        .then()
                .statusCode(204);
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
