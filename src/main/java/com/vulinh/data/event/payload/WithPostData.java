package com.vulinh.data.event.payload;

import java.util.UUID;

public interface WithPostData {

  UUID postId();

  String title();

  String excerpt();
}
