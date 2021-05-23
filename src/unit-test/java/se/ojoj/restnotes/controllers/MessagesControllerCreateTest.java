package se.ojoj.restnotes.controllers;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import se.ojoj.restnotes.models.Client;
import se.ojoj.restnotes.models.Message;

@QuarkusTest
public class MessagesControllerCreateTest extends MessagesControllerBaseTest {

  @Inject
  MessagesController controller;

  @InjectMock
  SecurityIdentity identity;

  @Test
  @TestTransaction
  public void testUnauthedShouldRaiseException() {
    // Given
    Message postBody = new Message();
    postBody.body = "Foo!";

    // When / Then
    Assertions.assertThrows(
        ForbiddenException.class,
        () -> controller.create(postBody));
  }

  @Test
  @TestTransaction
  public void testRemovedUserShouldRaiseException() {
    // Given
    setupIdentity("removedUser", identity, false);

    Message postBody = new Message();
    postBody.body = "Foo!";

    // When / Then
    Assertions.assertThrows(
        ForbiddenException.class,
        () -> controller.create(postBody));

  }

  @Test
  @TestTransaction
  public void testMessageShouldReceiveTimestamp() {
    // Given
    setupIdentity("user", identity);

    Message postBody = new Message();
    postBody.body = "Lorem ipsum.";

    // When
    Message actualResult = controller.create(postBody);

    // Then
    Assertions.assertNotNull(actualResult.timestamp);
  }

  @Test
  @TestTransaction
  public void testMessageShouldReceiveId() {
    // Given
    setupIdentity("testUser", identity);

    Message postBody = new Message();
    postBody.body = "Lorem ipsum.";

    // When
    Message actualResult = controller.create(postBody);

    // Then
    Assertions.assertNotNull(actualResult.id);
  }

  @Test
  @TestTransaction
  public void testProvidingClientShouldRaiseBadRequest() {
    // Given
    setupIdentity("testUser", identity);

    Client fakeClient = Client.add("fakeUser", "", "client");

    Message postBody = new Message();
    postBody.body = "Lorem ipsum.";
    postBody.client = fakeClient;

    // When / Then
    Assertions.assertThrows(
        BadRequestException.class,
        () -> controller.create(postBody));
  }
}
