package com.github.felixgail.gplaymusic.model.requestbodies.mutations;

public interface Mutation<T> {

  T getMutation();

  String getSerializedAttributeName();
}
