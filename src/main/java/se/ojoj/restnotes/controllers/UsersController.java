package se.ojoj.restnotes.controllers;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import se.ojoj.restnotes.models.Client;

@Path("/users")
@Tag(name = "Users", description = "Operations related to users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersController {

  @POST
  @Operation(summary = "Register as a new client.")
  @PermitAll
  @Transactional
  public Client create(@Valid Client client) throws BadRequestException {
    return Client.add(client.username, client.password, "client");
  }
}
