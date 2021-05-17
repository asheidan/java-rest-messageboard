package se.ojoj.restnotes.integrationtests;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import java.net.URL;
import org.junit.jupiter.api.Test;
import se.ojoj.restnotes.controllers.MessagesController;
import se.ojoj.restnotes.models.Message;

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
