package se.ojoj.restnotes.controllers;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.ojoj.restnotes.models.Client;
import se.ojoj.restnotes.models.Message;
import se.ojoj.restnotes.se.ojoj.restnotes.pagination.PaginationWrapper;

@QuarkusTest
public class MessagesControllerListTest extends MessagesControllerBaseTest {

  @Inject
  MessagesController controller;

  @InjectMock
  SecurityIdentity identity;

  @Test
  public void testEmptyDBShouldReturnEmptyList() {
    // Given
    //PanacheMock.mock(Message.class);
    //Mockito.when(Message.listAll()).thenReturn(Collections.emptyList());

    // When
    PaginationWrapper actualResult = controller.list(0, 25);

    // Then
    PaginationWrapper expectedResult = new PaginationWrapper(Collections.emptyList(), 0, 25, 0);
    Assertions.assertEquals(expectedResult, actualResult);
  }

  @Test
  @TestTransaction
  public void testFirstMessageShouldBeOnFirstPage() {
    // Given
    Client client = setupIdentity("testUser", "client", identity);

    Message message = new Message();
    message.body = "Run, you foo!";
    message.client = client;
    message.persistAndFlush();

    // When
    PaginationWrapper<Message> actualResult = controller.list(0, 1);

    // Then
    Message actualMessage = actualResult.page.get(0);
    Assertions.assertEquals(message.id, actualMessage.id);
    Assertions.assertEquals(1L, actualResult.total);
  }

  @Test
  @TestTransaction
  public void testPaginationOffsetShouldExclude() {
    // Given
    Client client = setupIdentity("testUser", "client", identity);

    Message message = new Message();
    message.body = "Run, you bar!";
    message.client = client;
    message.persistAndFlush();

    // When
    PaginationWrapper<Message> actualResult = controller.list(1, 1);

    // Then
    boolean actualEmpty = actualResult.page.isEmpty();
    Assertions.assertEquals(true, actualEmpty);
  }

}