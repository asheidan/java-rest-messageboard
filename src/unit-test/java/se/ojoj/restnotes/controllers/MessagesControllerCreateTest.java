package se.ojoj.restnotes.controllers;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import se.ojoj.restnotes.models.Message;

@QuarkusTest
public class MessagesControllerCreateTest {
  @Test
  @TestTransaction
  public void testMessageShouldReceiveTimestamp() {
    // Given
    Message postBody = new Message();
    postBody.body = "Lorem ipsum.";

    MessagesController controller = new MessagesController();

    // When
    Message actualResult = controller.create(postBody);

    // Then
    Assertions.assertNotNull(actualResult.timestamp);
  }

  @Test
  @TestTransaction
  public void testMessageShouldReceiveId() {
    // Given
    Message postBody = new Message();
    postBody.body = "Lorem ipsum.";

    MessagesController controller = new MessagesController();

    // When
    Message actualResult = controller.create(postBody);

    // Then
    Assertions.assertNotNull(actualResult.id);
  }
}
