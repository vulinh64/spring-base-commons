package com.vulinh.data.event.payload;

import com.vulinh.data.base.UuidIdentifiable;
import java.util.Objects;
import java.util.UUID;

public record NewPostFollowingEvent(UUID postId, String title, String excerpt)
    implements WithPostData, UuidIdentifiable {

  public static NewPostFollowingEventBuilder builder() {
    return new NewPostFollowingEventBuilder();
  }

  public NewPostFollowingEvent withPostId(UUID postId) {
    return Objects.equals(this.postId, postId)
        ? this
        : new NewPostFollowingEvent(postId, title, excerpt);
  }

  public NewPostFollowingEvent withTitle(String title) {
    return Objects.equals(this.title, title)
        ? this
        : new NewPostFollowingEvent(postId, title, excerpt);
  }

  public NewPostFollowingEvent withExcerpt(String excerpt) {
    return Objects.equals(this.excerpt, excerpt)
        ? this
        : new NewPostFollowingEvent(postId, title, excerpt);
  }

  @Override
  public UUID getId() {
    return postId;
  }

  public static class NewPostFollowingEventBuilder {

    private UUID postId;
    private String title;
    private String excerpt;

    NewPostFollowingEventBuilder() {}

    public NewPostFollowingEventBuilder postId(UUID postId) {
      this.postId = postId;
      return this;
    }

    public NewPostFollowingEventBuilder title(String title) {
      this.title = title;
      return this;
    }

    public NewPostFollowingEventBuilder excerpt(String excerpt) {
      this.excerpt = excerpt;
      return this;
    }

    public NewPostFollowingEvent build() {
      return new NewPostFollowingEvent(postId, title, excerpt);
    }
  }
}
