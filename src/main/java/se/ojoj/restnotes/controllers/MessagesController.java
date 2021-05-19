package se.ojoj.restnotes.controllers;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.identity.SecurityIdentity;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import se.ojoj.restnotes.models.Client;
import se.ojoj.restnotes.models.Message;
import se.ojoj.restnotes.se.ojoj.restnotes.pagination.PaginationWrapper;

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
  @PermitAll
  @Operation(summary = "Get a list of all messages.")
  @Parameter(name = "offset", description = "The index of the first message, starting at 0.")
  @Parameter(name = "limit", description = "The index of the last message.")
  public PaginationWrapper<Message> list(@QueryParam @DefaultValue("0") int offset,
                                         @QueryParam @DefaultValue("25") int limit) {
    PanacheQuery<Message> messageQuery = Message.find("order by timestamp asc");
    messageQuery.range(offset, offset + limit);

    long count = messageQuery.count();

    List<Message> page = messageQuery.list();

    return new PaginationWrapper<>(page, offset, limit, count);
  }

  @POST
  @RolesAllowed("client")
  @Operation(summary = "Post a new message.")
  @Transactional
  public Message create(@Valid Message message) {
    Client client = Client.findByUsername(identity.getPrincipal().getName());
    if (null == client) {
      throw new ForbiddenException("You do not exist.");
    }

    if (null != message.client) {
      throw new BadRequestException("Client should not be provided when creating new message.");
    }

    if (null == message.timestamp) {
      message.timestamp = ZonedDateTime.now();
    }

    message.client = client;

    message.persistAndFlush();

    return message;
  }

  @Path("{id}")
  @POST
  @RolesAllowed("client")
  @Operation(summary = "Update one of your existing messages.")
  @Parameter(name = "id", description = "The id of the message.")
  @Transactional
  public Message update(@PathParam Long id, Message message) throws ForbiddenException, NotFoundException {
    Client client = Client.findByUsername(identity.getPrincipal().getName());
    if (null == client) {
      throw new ForbiddenException("You do not exist");
    }

    Message entity = Message.find("client = :client AND id = :id",
                                  Parameters.with("client", client)
                                            .and("id", id))
        .withLock(LockModeType.PESSIMISTIC_WRITE)
        .firstResult();
    if (null == entity) {
      throw new NotFoundException("No such message belonging to you were found.");
    }

    entity.body = message.body;

    return entity;
  }

  @Path("{id}")
  @GET
  @RolesAllowed("client")
  @Operation(summary = "Get a single message.")
  @Parameter(name = "id", description = "The id of the message.")
  public Message show(@PathParam Long id) throws NotFoundException {
    Optional<Message> maybeMessage = Message.findByIdOptional(id);

    return maybeMessage.orElseThrow(() -> create404(id));
  }

  @Path("{id}")
  @DELETE
  @RolesAllowed("client")
  @Operation(summary = "Delete a single message.")
  @Parameter(name = "id", description = "The id of the message.")
  @Transactional
  public void destroy(@PathParam Long id) throws ForbiddenException, NotFoundException {
    Client client = Client.findByUsername(identity.getPrincipal().getName());
    if (null == client) {
      throw new ForbiddenException("You do not exist");
    }

    long deleteCount = Message.delete("id = :id AND client = :client",
                                      Parameters.with("id", id).and("client", client));
    if (0 >= deleteCount) {
      throw new NotFoundException("No such message belonging to you were found.");
    }
  }


  private NotFoundException create404(Long id) {
    return new NotFoundException("No message with id " + id + "found.");
  }
}