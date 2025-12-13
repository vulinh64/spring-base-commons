package com.vulinh.data;

import java.io.Serializable;

public interface Identifiable<I extends Serializable> {

  I getId();
}
