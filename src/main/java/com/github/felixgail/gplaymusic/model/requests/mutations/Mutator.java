package com.github.felixgail.gplaymusic.model.requests.mutations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Mutator implements Serializable {

  private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Expose
  private List<Mutation> mutations;

  public Mutator(Mutation mutation) {
    mutations = new LinkedList<>();
    addMutation(mutation);
  }

  public Mutator() {
    mutations = new LinkedList<>();
  }

  public Mutator(List<Mutation> list) {
    mutations = list;
  }

  public void addMutation(Mutation mutation) {
    mutations.add(mutation);
  }

  public List<Mutation> getMutations() {
    return mutations;
  }

  public void setMutations(List<Mutation> mutations) {
    this.mutations = mutations;
  }

  public String string() {
    return gson.toJson(this) + System.lineSeparator();
  }
}