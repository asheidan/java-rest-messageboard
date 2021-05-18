package se.ojoj.restnotes.models;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import javax.persistence.Entity;

@UserDefinition
@Entity
public class Client extends PanacheEntity {

  @Username
  public String username;

  @Password
  public String password;

  @Roles
  public String roles;

  public static void add(String username, String password, String roles) {
    Client client = new Client();
    client.username = username;
    client.password = BcryptUtil.bcryptHash(password);
    client.roles = roles;
    client.persist();
  }
}
