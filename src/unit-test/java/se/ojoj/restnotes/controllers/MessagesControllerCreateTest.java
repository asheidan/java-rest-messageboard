package se.ojoj.restnotes.controllers;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import java.security.Principal;
import javax.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.ojoj.restnotes.models.Client;
import se.ojoj.restnotes.models.Message;

@QuarkusTest
public class MessagesControllerCreateTest {
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
  public void testMessageShouldReceiveTimestamp() {
    // Given
    setupIdentity("user", "client");

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
    setupIdentity("testUser", "client");

    Message postBody = new Message();
    postBody.body = "Lorem ipsum.";

    // When
    Message actualResult = controller.create(postBody);

    // Then
    Assertions.assertNotNull(actualResult.id);
  }

  private Client setupIdentity(String username, String role) {
    Mockito.when(identity.isAnonymous()).thenReturn(false);

    Mockito.when(identity.hasRole(role)).thenReturn(true);

    Principal mockedPrincipal = Mockito.mock(Principal.class);
    Mockito.when(mockedPrincipal.getName()).thenReturn(username);

    Mockito.when(identity.getPrincipal()).thenReturn(mockedPrincipal);

    return null;

    //return Client.add(username, "", role);
  }
}
