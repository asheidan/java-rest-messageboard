package se.ojoj.restnotes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@JsonInclude(Include.NON_NULL)
public class Message extends PanacheEntityBase {


  @Id
  @GeneratedValue
  @Schema(description = "The unique identifier for the message.", readOnly = true)
  @JsonProperty(index = 0, access = Access.READ_ONLY)
  public Long id;

  @Schema(description = "The timestamp associated with the message.", readOnly = true)
  @JsonProperty(access = Access.READ_ONLY)
  public ZonedDateTime timestamp;

  @Schema(description = "The content of the message.", example = "Lorem ipsum doler sit amet.")
  @NotBlank(message = "Body may not be blank.")
  @JsonProperty(required = true)
  public String body;

  @ManyToOne
  @Schema(hidden = true)
  @JsonIgnore
  public Client client;

  @Schema(readOnly = true, example = "Ernest")
  @JsonProperty(value = "author", access = Access.READ_ONLY)
  public String clientName() {
    return client.username;
  }
}
