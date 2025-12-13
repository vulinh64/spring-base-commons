package com.vulinh.data.event.payload;

import com.vulinh.data.UuidIdentifiable;
import java.util.Objects;
import java.util.UUID;

public record NewPostEvent(UUID postId, String title, String excerpt)
    implements WithPostData, UuidIdentifiable {

  public static NewPostEventBuilder builder() {
    return new NewPostEventBuilder();
  }

  public NewPostEvent withPostId(UUID postId) {
    return Objects.equals(this.postId, postId) ? this : new NewPostEvent(postId, title, excerpt);
  }

  public NewPostEvent withTitle(String title) {
    return Objects.equals(this.title, title) ? this : new NewPostEvent(postId, title, excerpt);
  }

  public NewPostEvent withExcerpt(String excerpt) {
    return Objects.equals(this.excerpt, excerpt) ? this : new NewPostEvent(postId, title, excerpt);
  }

  @Override
  public UUID getId() {
    return postId;
  }

  public static class NewPostEventBuilder {

    private UUID postId;
    private String title;
    private String excerpt;

    NewPostEventBuilder() {}

    public NewPostEventBuilder postId(UUID postId) {
      this.postId = postId;
      return this;
    }

    public NewPostEventBuilder title(String title) {
      this.title = title;
      return this;
    }

    public NewPostEventBuilder excerpt(String excerpt) {
      this.excerpt = excerpt;
      return this;
    }

    public NewPostEvent build() {
      return new NewPostEvent(postId, title, excerpt);
    }
  }
}
