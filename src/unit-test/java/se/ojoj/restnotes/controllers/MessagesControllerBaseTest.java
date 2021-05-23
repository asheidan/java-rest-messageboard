package se.ojoj.restnotes.controllers;

import io.quarkus.security.identity.SecurityIdentity;
import java.security.Principal;
import org.mockito.Mockito;
import se.ojoj.restnotes.models.Client;

public abstract class MessagesControllerBaseTest {

  protected Client setupIdentity(String username, SecurityIdentity identity) {
    return setupIdentity(username, identity, true);
  }

  protected Client setupIdentity(String username, SecurityIdentity identity,
      boolean createUser) {
    final String role = "client";

    Mockito.when(identity.isAnonymous()).thenReturn(false);

    Mockito.when(identity.hasRole(role)).thenReturn(true);

    Principal mockedPrincipal = Mockito.mock(Principal.class);
    Mockito.when(mockedPrincipal.getName()).thenReturn(username);

    Mockito.when(identity.getPrincipal()).thenReturn(mockedPrincipal);

    if (! createUser) {

      return null;
    }

    return Client.add(username, "", role);
  }
}
