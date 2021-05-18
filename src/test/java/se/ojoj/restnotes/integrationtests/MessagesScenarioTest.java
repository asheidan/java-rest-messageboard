package se.ojoj.restnotes.integrationtests;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
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

  @Test
  @TestTransaction
  public void testPostedMessageShouldBeReturned() {
    String createResult = with()
        .contentType(ContentType.JSON)
        .body("{\"body\": \"Lorem ipsum doler sit amet.\"}")
        .post("/messages")
        .asString();

    Integer messageId = JsonPath.from(createResult).get("id");

    with()
        .when().get("/messages/" + messageId)
        .then()
          .statusCode(200)
          .body("body", is("Lorem ipsum doler sit amet."));

  }

}
