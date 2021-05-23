package se.ojoj.restnotes.controllers;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import se.ojoj.restnotes.models.Client;
import se.ojoj.restnotes.models.Message;

@QuarkusTest
public class MessagesControllerUpdateTest extends MessagesControllerBaseTest {

  @Inject
  MessagesController controller;

  @InjectMock
  SecurityIdentity identity;

  @Test
  @TestTransaction
  public void testUnauthedShouldRaiseException() {
    // Given
    Message updateBody = new Message();
    updateBody.body = "This is clearly an updated body.";

    // When / Then
    Assertions.assertThrows(
        ForbiddenException.class,
        () -> controller.update(42L, updateBody));
  }

  @Test
  @TestTransaction
  public void testUsingRemovedIdentityShouldRaise() {
    // Given
    setupIdentity("testUser", identity, false);

    Client otherClient = Client.add("fakeUser", "", "client");

    Message existingMessage = new Message();
    existingMessage.body = "Lorem ipsum.";
    existingMessage.client = otherClient;
    existingMessage.persistAndFlush();

    Message postBody = new Message();
    postBody.body = "Lorem ipsum.";

    // When / Then
    Assertions.assertThrows(
        ForbiddenException.class,
        () -> controller.update(existingMessage.id, postBody));
  }

  @Test
  @TestTransaction
  public void testUpdatingAnotherClientMessageShouldRaise() {
    // Given
    setupIdentity("testUser", identity);

    Client otherClient = Client.add("otherUser", "", "client");
    Message otherMessage = new Message();
    otherMessage.body = "Not very interesting message.";
    otherMessage.client = otherClient;
    otherMessage.persistAndFlush();

    Message postBody = new Message();
    postBody.body = "Much more interesting message.";

    // When / Then
    Assertions.assertThrows(
        NotFoundException.class,
        () -> controller.update(otherMessage.id, postBody));
  }

  @Test
  @TestTransaction
  public void testClientUpdatingTheirOwnMessageShouldUpdateMessage() {
    // Given
    Client client = setupIdentity("testUser", identity);

    Message existingMessage = new Message();
    existingMessage.body = "Not very interesting message.";
    existingMessage.client = client;
    existingMessage.persistAndFlush();

    Message postBody = new Message();
    postBody.body = "Much more interesting message.";

    // When
    Message actualMessage = controller.update(existingMessage.id, postBody);

    // Then
    Assertions.assertEquals("Much more interesting message.", actualMessage.body);
  }
}
