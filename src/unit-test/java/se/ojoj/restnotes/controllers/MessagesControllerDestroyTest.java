package se.ojoj.restnotes.controllers;

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
  public void testDeletingNonexistantMessageShouldRaise () {
    // Given
    setupIdentity("testUser", "client", identity);

    // When / Then
    Assertions.assertThrows(
        NotFoundException.class,
        () -> controller.destroy(-1234907L));
  }

  @Test
  @TestTransaction
  public void testDeletingOtherClientsMessageShouldRaise () {
    // Given
    setupIdentity("testUser", "client", identity);

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
  public void testDeletingNormalMessageShouldRemoveMessage () {
    // Given
    Client client = setupIdentity("testUser", "client", identity);

    Message message = new Message();
    message.body = "Not important body.";
    message.client = client;
    message.persistAndFlush();

    // When
    controller.destroy(message.id);

    // Then
    Assertions.assertNull(Message.findById(message.id));
  }
}
