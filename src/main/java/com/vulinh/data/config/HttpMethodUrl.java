package com.vulinh.data.config;

import java.util.Objects;
import org.springframework.http.HttpMethod;

/** An HTTP method and URL pattern pair used to configure a method-specific public route. */
public record HttpMethodUrl(HttpMethod method, String url) {

  /** Creates a builder for an {@link HttpMethodUrl}. */
  public static HttpMethodUrlBuilder builder() {
    return new HttpMethodUrlBuilder();
  }

  /** Returns this instance or a copy with the supplied HTTP method. */
  public HttpMethodUrl withMethod(HttpMethod method) {
    return Objects.equals(this.method, method) ? this : new HttpMethodUrl(method, url);
  }

  /** Returns this instance or a copy with the supplied URL pattern. */
  public HttpMethodUrl withUrl(String url) {
    return Objects.equals(this.url, url) ? this : new HttpMethodUrl(method, url);
  }

  /** Creates a builder pre-populated with this instance's values. */
  public HttpMethodUrlBuilder toBuilder() {
    return new HttpMethodUrlBuilder().method(method).url(url);
  }

  /** Mutable builder for {@link HttpMethodUrl}. */
  public static class HttpMethodUrlBuilder {

    private HttpMethod method;
    private String url;

    HttpMethodUrlBuilder() {}

    /** Sets the HTTP method. */
    public HttpMethodUrlBuilder method(HttpMethod method) {
      this.method = method;
      return this;
    }

    /** Sets the URL pattern. */
    public HttpMethodUrlBuilder url(String url) {
      this.url = url;
      return this;
    }

    /** Builds an immutable {@link HttpMethodUrl}. */
    public HttpMethodUrl build() {
      return new HttpMethodUrl(method, url);
    }
  }
}
