package se.ojoj.restnotes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PersistenceException;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.BadRequestException;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@UserDefinition
@Entity
public class Client extends PanacheEntityBase {

  @Id
  @GeneratedValue
  @Schema(hidden = true)
  @JsonIgnore
  public Long id;

  @Username
  @Schema(required = true)
  @NotBlank(message = "Username may not be blank.")
  @Column(unique = true)
  public String username;

  @Password
  @Schema(required = true, writeOnly = true)
  @NotBlank(message = "Password may not be blank.")
  @JsonProperty(access = Access.WRITE_ONLY)
  public String password;

  @Roles
  @Schema(description = "Comma-separated list of roles.", readOnly = true)
  @JsonProperty(access = Access.READ_ONLY)
  public String roles;

  public static Client findByUsername(String username) {
    return find("username", username).firstResult();
  }

  public static Client add(String username, String password, String roles) throws BadRequestException {
    Client client = new Client();
    client.username = username;
    client.password = BcryptUtil.bcryptHash(password);
    client.roles = roles;

    try {
      client.persistAndFlush();
    }
    catch (PersistenceException persistenceException) {
      // TODO: Make sure that the exception is actually due to this error before throwing.
      // TODO: Replace exception with Client-specific exception or move to controller.
      throw new BadRequestException("Username already taken.");
    }

    return client;
  }
}