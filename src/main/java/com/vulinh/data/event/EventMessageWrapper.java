package com.vulinh.data.event;

import com.vulinh.data.UuidIdentifiable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record EventMessageWrapper<T>(
    UUID eventId, Instant timestamp, EventType eventType, ActionUser actionUser, T data)
    implements UuidIdentifiable {

  public EventMessageWrapper {
    eventId = UUID.randomUUID();
    timestamp = Instant.now();
  }

  public static <T> EventMessageWrapperBuilder<T> builder() {
    return new EventMessageWrapperBuilder<>();
  }

  public EventMessageWrapper<T> withEventId(UUID eventId) {
    return Objects.equals(this.eventId, eventId)
        ? this
        : new EventMessageWrapper<>(eventId, timestamp, eventType, actionUser, data);
  }

  public EventMessageWrapper<T> withTimestamp(Instant timestamp) {
    return Objects.equals(this.timestamp, timestamp)
        ? this
        : new EventMessageWrapper<>(eventId, timestamp, eventType, actionUser, data);
  }

  public EventMessageWrapper<T> withEventType(EventType eventType) {
    return this.eventType == eventType
        ? this
        : new EventMessageWrapper<>(eventId, timestamp, eventType, actionUser, data);
  }

  public EventMessageWrapper<T> withActionUser(ActionUser actionUser) {
    return Objects.equals(this.actionUser, actionUser)
        ? this
        : new EventMessageWrapper<>(eventId, timestamp, eventType, actionUser, data);
  }

  public EventMessageWrapper<T> withData(T data) {
    return Objects.equals(this.data, data)
        ? this
        : new EventMessageWrapper<>(eventId, timestamp, eventType, actionUser, data);
  }

  @Override
  public UUID getId() {
    return eventId;
  }

  public static class EventMessageWrapperBuilder<T> {
    private EventType eventType;
    private ActionUser actionUser;
    private T data;

    EventMessageWrapperBuilder() {}

    public EventMessageWrapperBuilder<T> eventType(EventType eventType) {
      this.eventType = eventType;
      return this;
    }

    public EventMessageWrapperBuilder<T> actionUser(ActionUser actionUser) {
      this.actionUser = actionUser;
      return this;
    }

    public EventMessageWrapperBuilder<T> data(T data) {
      this.data = data;
      return this;
    }

    public EventMessageWrapper<T> build() {
      return new EventMessageWrapper<>(null, null, eventType, actionUser, data);
    }
  }
}
