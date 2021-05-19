package se.ojoj.restnotes.integrationtests;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class MessagesScenariosTest {

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

  @Disabled("Waiting for test-fixtures for integration-tests")
  @Test
  @TestTransaction
  @TestSecurity(user = "testUser", roles = "client")
  public void testPostedMessageShouldBeReturned() {

    Response response = with()
        .contentType(ContentType.JSON)
        .body("{\"body\": \"Lorem ipsum doler sit amet.\"}")
        .post("/messages");

    // Make sure post succeeded before proceeding
    response.then().statusCode(200);

    String createResult = response.asString();
    Integer messageId = JsonPath.from(createResult).get("id");

    with()
        .when().get("/messages/" + messageId)
        .then()
          .statusCode(200)
          .body("body", is("Lorem ipsum doler sit amet."));

  }

}
