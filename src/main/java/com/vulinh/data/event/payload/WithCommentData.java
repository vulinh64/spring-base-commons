package com.vulinh.data.event.payload;

import java.util.UUID;

public interface WithCommentData {

  UUID commentId();

  String content();
}
