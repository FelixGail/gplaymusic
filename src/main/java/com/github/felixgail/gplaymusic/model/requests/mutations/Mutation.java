package com.github.felixgail.gplaymusic.model.requests.mutations;

public interface Mutation<T> {

  T getMutation();

  String getSerializedAttributeName();
}
