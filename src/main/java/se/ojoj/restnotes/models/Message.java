package se.ojoj.restnotes.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.time.ZonedDateTime;
import javax.persistence.Entity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@JsonInclude(Include.NON_NULL)
public class Message extends PanacheEntity {

  @Schema(description = "The timestamp associated with the message.")
  public ZonedDateTime timestamp;

  @Schema(description = "The content of the message.")
  public String body;

}
