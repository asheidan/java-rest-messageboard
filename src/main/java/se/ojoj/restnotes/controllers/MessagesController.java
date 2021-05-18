package se.ojoj.restnotes.controllers;

import io.quarkus.security.identity.SecurityIdentity;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import se.ojoj.restnotes.models.Client;
import se.ojoj.restnotes.models.Message;

@Path("/messages")
@Tag(name = "Messages", description = "Operations related to messages")
@SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme = "basic")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class MessagesController {

  @Inject
  SecurityIdentity identity;

  @GET
  @Operation(summary = "Get a list of all messages.")
  public List<Message> list() {
    return Message.listAll();
  }

  @POST
  @Operation(summary = "Post a new message.")
  @Transactional
  @RolesAllowed("client")
  public Message create(@Valid Message message) {
    if (null == message.timestamp) {
      message.timestamp = ZonedDateTime.now();
    }

    Client client = Client.findByUsername(identity.getPrincipal().getName());
    message.client = client;

    message.persistAndFlush();

    return message;
  }
}