package com.vulinh.data.event.payload;

import com.vulinh.data.UuidIdentifiable;
import java.util.Objects;
import java.util.UUID;

public record NewCommentEvent(
    UUID postId, String title, String excerpt, UUID commentId, String content)
    implements WithPostData, WithCommentData, UuidIdentifiable {

  public static NewCommentEventBuilder builder() {
    return new NewCommentEventBuilder();
  }

  public NewCommentEvent withPostId(UUID postId) {
    return Objects.equals(this.postId, postId)
        ? this
        : new NewCommentEvent(postId, title, excerpt, commentId, content);
  }

  public NewCommentEvent withTitle(String title) {
    return Objects.equals(this.title, title)
        ? this
        : new NewCommentEvent(postId, title, excerpt, commentId, content);
  }

  public NewCommentEvent withExcerpt(String excerpt) {
    return Objects.equals(this.excerpt, excerpt)
        ? this
        : new NewCommentEvent(postId, title, excerpt, commentId, content);
  }

  public NewCommentEvent withCommentId(UUID commentId) {
    return Objects.equals(this.commentId, commentId)
        ? this
        : new NewCommentEvent(postId, title, excerpt, commentId, content);
  }

  public NewCommentEvent withContent(String content) {
    return Objects.equals(this.content, content)
        ? this
        : new NewCommentEvent(postId, title, excerpt, commentId, content);
  }

  @Override
  public UUID getId() {
    return commentId;
  }

  public static class NewCommentEventBuilder {

    private UUID postId;
    private String title;
    private String excerpt;
    private UUID commentId;
    private String content;

    NewCommentEventBuilder() {}

    public NewCommentEventBuilder postId(UUID postId) {
      this.postId = postId;
      return this;
    }

    public NewCommentEventBuilder title(String title) {
      this.title = title;
      return this;
    }

    public NewCommentEventBuilder excerpt(String excerpt) {
      this.excerpt = excerpt;
      return this;
    }

    public NewCommentEventBuilder commentId(UUID commentId) {
      this.commentId = commentId;
      return this;
    }

    public NewCommentEventBuilder content(String content) {
      this.content = content;
      return this;
    }

    public NewCommentEvent build() {
      return new NewCommentEvent(postId, title, excerpt, commentId, content);
    }
  }
}
