package se.ojoj.restnotes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@JsonInclude(Include.NON_NULL)
public class Message extends PanacheEntity {

  @Schema(description = "The timestamp associated with the message.")
  public ZonedDateTime timestamp;

  @Schema(description = "The content of the message.", example = "Lorem ipsum doler sit amet.")
  @NotBlank(message = "Body may not be blank.")
  public String body;

  @ManyToOne
  @JsonIgnore  // Don't deserialize this field (check annotations on getter/setter)
  public Client client;

  @JsonProperty
  public Client getClient() {
    return client;
  }

  @JsonIgnore
  public void setClient(Client client) {
    this.client = client;
  }

}
