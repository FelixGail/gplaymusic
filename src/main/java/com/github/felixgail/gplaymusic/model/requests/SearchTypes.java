package com.github.felixgail.gplaymusic.model.requests;

import com.github.felixgail.gplaymusic.model.enums.ResultType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchTypes {

  private List<ResultType> types;

  public SearchTypes(List<ResultType> types) {
    this.types = types;
  }

  public SearchTypes(ResultType... types) {
    this.types = Arrays.asList(types);
  }

  public List<ResultType> getTypes() {
    return types;
  }

  public void setTypes(List<ResultType> types) {
    this.types = types;
  }

  @Override
  public String toString() {
    return types.stream().map(t -> Integer.toString(t.getValue())).collect(Collectors.joining(","));
  }
}
