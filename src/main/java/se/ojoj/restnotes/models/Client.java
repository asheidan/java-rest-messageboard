package se.ojoj.restnotes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceException;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.BadRequestException;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@UserDefinition
@Entity
public class Client extends PanacheEntity {

  @Username
  @NotBlank(message = "Username may not be blank.")
  @Schema(required = true)
  @Column(unique = true)
  public String username;

  @Password
  @Schema(required = true)
  @NotBlank(message = "Password may not be blank.")
  @JsonIgnore
  public String password;

  @Roles
  @Schema(description = "Comma-separated list of roles.")
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
      throw new BadRequestException("Username already taken.");
    }

    return client;
  }

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @JsonProperty
  public void setPassword(String password) {
    this.password = password;
  }

}