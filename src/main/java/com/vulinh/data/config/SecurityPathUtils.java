package com.vulinh.data.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/** Utility methods for constructing stateless Spring Security API filter chains. */
public final class SecurityPathUtils {

  /** Default springdoc paths made public by {@link #publicApi(PublicSecurityPath)}. */
  private static final List<String> DEFAULT_OPENAPI_PATHS =
      List.of("/swagger-ui", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**");

  private SecurityPathUtils() {}

  /**
   * Creates a matcher for all configured public routes, default springdoc routes, and effective
   * Actuator endpoints.
   *
   * <p>Use this matcher only with the highest-priority {@code SecurityFilterChain}. A broader chain
   * registered first can intercept these requests before they reach the public chain.
   *
   * @param publicSecurityPath application-specific public route configuration
   * @return a matcher suitable for a public Spring Security filter chain
   */
  public static RequestMatcher publicApi(PublicSecurityPath publicSecurityPath) {
    var noAuthUrls = publicSecurityPath.getNoAuthUrls();
    var noAuthMethodUrls = publicSecurityPath.getNoAuthMethodUrls();

    var matchers =
        new ArrayList<RequestMatcher>(
            noAuthUrls.size() + noAuthMethodUrls.size() + DEFAULT_OPENAPI_PATHS.size() + 1);

    var matcherBuilder = PathPatternRequestMatcher.withDefaults();

    noAuthUrls.forEach(url -> matchers.add(matcherBuilder.matcher(url)));

    noAuthMethodUrls.forEach(url -> matchers.add(specificUrl(matcherBuilder, url)));

    DEFAULT_OPENAPI_PATHS.forEach(url -> matchers.add(matcherBuilder.matcher(url)));

    matchers.add(EndpointRequest.toAnyEndpoint());

    return new OrRequestMatcher(matchers);
  }

  /**
   * Applies a request matcher and stateless session management to a security chain.
   *
   * @param http the security builder to configure
   * @param matcher paths handled by the chain
   * @return the configured security builder
   * @throws Exception if Spring Security rejects the configuration
   */
  public static HttpSecurity baseStateless(HttpSecurity http, RequestMatcher matcher)
      throws Exception {
    return http.securityMatcher(matcher)
        .sessionManagement(
            sessionManagementConfigurer ->
                sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
  }

  private static RequestMatcher specificUrl(
      PathPatternRequestMatcher.Builder matcherBuilder, HttpMethodUrl httpMethodUrl) {
    return matcherBuilder.matcher(httpMethodUrl.method(), httpMethodUrl.url());
  }
}
