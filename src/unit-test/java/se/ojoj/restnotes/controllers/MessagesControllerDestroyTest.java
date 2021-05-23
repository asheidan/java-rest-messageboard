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
public class MessagesControllerDestroyTest extends MessagesControllerBaseTest {

  @Inject
  MessagesController controller;

  @InjectMock
  SecurityIdentity identity;

  @Test
  @TestTransaction
  public void testWithoutIdentityShouldRaise() {
    // Given
    Message message = new Message();
    message.body = "Whatever.";
    message.persist();

    // When / Then
    Assertions.assertThrows(
        ForbiddenException.class,
        () -> controller.destroy(message.id));
  }

  @Test
  @TestTransaction
  public void testDeletingNonexistantMessageShouldRaise() {
    // Given
    setupIdentity("testUser", identity);

    // When / Then
    Assertions.assertThrows(
        NotFoundException.class,
        () -> controller.destroy(-1234907L));
  }

  @Test
  @TestTransaction
  public void testDeletingOtherClientsMessageShouldRaise() {
    // Given
    setupIdentity("testUser", identity);

    Client otherClient = Client.add("otherUser", "", "client");

    Message message = new Message();
    message.body = "Not important body.";
    message.client = otherClient;
    message.persistAndFlush();

    // When / Then
    Assertions.assertThrows(
        NotFoundException.class,
        () -> controller.destroy(message.id));

  }

  @Test
  @TestTransaction
  public void testDeletingNormalMessageShouldNotRaise() {
    // Given
    Client client = setupIdentity("testUser", identity);

    Message message = new Message();
    message.body = "Not important body.";
    message.client = client;
    message.persistAndFlush();

    Long messageId = message.id;

    // When / Then
    controller.destroy(messageId);
  }
}
