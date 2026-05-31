package com.vulinh.data.dto.response;

import com.vulinh.data.base.RecordUuidIdentifiable;
import com.vulinh.data.event.WithIdUsername;
import java.util.UUID;

public record AccountBasicResponse(
    UUID id, String username, String firstName, String lastName, String email, Boolean isEnabled)
    implements RecordUuidIdentifiable, WithIdUsername {

  public AccountBasicResponse {
    isEnabled = isEnabled != null && isEnabled;
  }

  public static Builder builder() {
    return new Builder();
  }

  public AccountBasicResponse withId(UUID id) {
    return new AccountBasicResponse(id, username, firstName, lastName, email, isEnabled);
  }

  public AccountBasicResponse withUsername(String username) {
    return new AccountBasicResponse(id, username, firstName, lastName, email, isEnabled);
  }

  public AccountBasicResponse withFirstName(String firstName) {
    return new AccountBasicResponse(id, username, firstName, lastName, email, isEnabled);
  }

  public AccountBasicResponse withLastName(String lastName) {
    return new AccountBasicResponse(id, username, firstName, lastName, email, isEnabled);
  }

  public AccountBasicResponse withEmail(String email) {
    return new AccountBasicResponse(id, username, firstName, lastName, email, isEnabled);
  }

  public AccountBasicResponse withEnabled(boolean enabled) {
    return new AccountBasicResponse(id, username, firstName, lastName, email, enabled);
  }

  public Builder toBuilder() {
    return new Builder()
        .id(id)
        .username(username)
        .firstName(firstName)
        .lastName(lastName)
        .email(email)
        .isEnabled(isEnabled);
  }

  public static final class Builder {

    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isEnabled;

    private Builder() {}

    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder isEnabled(boolean enabled) {
      this.isEnabled = enabled;
      return this;
    }

    public AccountBasicResponse build() {
      return new AccountBasicResponse(id, username, firstName, lastName, email, isEnabled);
    }
  }
}
