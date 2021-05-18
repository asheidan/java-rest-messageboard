package se.ojoj.restnotes.controllers;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.ojoj.restnotes.models.Message;

@QuarkusTest
public class MessagesControllerListTest {

  @Test
  public void testEmptyDBShouldReturnEmptyList() {
    // Given
    PanacheMock.mock(Message.class);
    Mockito.when(Message.listAll()).thenReturn(Collections.emptyList());

    MessagesController controller = new MessagesController();

    // When
    List actualResult = controller.list();

    // Then
    List expectedResult = Collections.emptyList();
    Assertions.assertIterableEquals(expectedResult, actualResult);
  }

}