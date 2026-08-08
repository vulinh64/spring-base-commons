package com.vulinh.data.config;

import java.util.List;

/**
 * Configuration contract for API routes that do not require authentication.
 *
 * <p>Use {@link SecurityPathUtils#publicApi(PublicSecurityPath)} to turn this configuration into a
 * Spring Security request matcher.
 */
public interface PublicSecurityPath {

  /** Returns public URL patterns that apply to every HTTP method. */
  List<String> getNoAuthUrls();

  /** Returns public URL patterns restricted to their configured HTTP methods. */
  List<HttpMethodUrl> getNoAuthMethodUrls();
}
