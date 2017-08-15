package com.github.felixgail.gplaymusic.model.requestbodies.mutations;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Mutator implements Serializable {
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
}