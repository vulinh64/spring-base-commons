package com.vulinh.data.config;

import java.util.List;

/**
 * Bridges record-style component accessors to the JavaBean-style methods required by {@link
 * PublicSecurityPath}.
 */
public interface RecordPublicSecurityPath extends PublicSecurityPath {

  /** Returns public URL patterns that apply to every HTTP method. */
  List<String> noAuthUrls();

  /** Returns public URL patterns restricted to their configured HTTP methods. */
  List<HttpMethodUrl> noAuthMethodUrls();

  /** {@inheritDoc} */
  @Override
  default List<String> getNoAuthUrls() {
    return noAuthUrls();
  }

  /** {@inheritDoc} */
  @Override
  default List<HttpMethodUrl> getNoAuthMethodUrls() {
    return noAuthMethodUrls();
  }
}
