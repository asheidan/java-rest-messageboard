package se.ojoj.restnotes.integrationtests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class MessagesScenarioTest {

  @Test
  public void testListEndpointShouldReturnOK() {
    given()
        .when().get("/messages")
        .then()
        .statusCode(200);
  }

  @Test
  public void testEmptyDBShouldReturnEmptyList() {
    given()
        .when().get("/messages")
        .then()
        .statusCode(200)
        .body(is("[]"));
  }

}
