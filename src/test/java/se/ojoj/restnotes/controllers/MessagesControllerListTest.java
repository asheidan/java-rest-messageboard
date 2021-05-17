package se.ojoj.restnotes.controllers;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class MessagesControllerListTest {

    @Test
    public void testListEndpointShouldReturnOK() {
        given()
          .when().get("/messages")
          .then()
             .statusCode(200);
    }

}