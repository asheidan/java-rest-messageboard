package se.ojoj.restnotes.controllers;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

@Path("/messages")
@Tag(name = "Messages", description = "Operations related to messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessagesController {

    @GET
    @Operation(summary = "Get a list of all messages.")
    public List<Object> list() {
        return Collections.emptyList();
    }
}