package com.github.felixgail.gplaymusic.model.shema;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ListResult<T> implements Serializable {

    @Expose
    private InnerData<T> data = new InnerData<>();


    public int size() {
        return data.items.size();
    }


    public boolean isEmpty() {
        return data.items.isEmpty();
    }

    //@Override
    public boolean contains(Object o) {
        return data.items.contains(0);
    }


    public Iterator<T> iterator() {
        return data.items.iterator();
    }


    public void forEach(Consumer<? super T> action) {
        data.items.forEach(action);
    }


    public Object[] toArray() {
        return data.items.toArray();
    }


    public <T1> T1[] toArray(T1[] a) {
        return data.items.toArray(a);
    }


    public boolean add(T t) {
        return data.items.add(t);
    }


    public boolean remove(Object o) {
        return data.items.remove(o);
    }


    public boolean containsAll(Collection<?> c) {
        return data.items.containsAll(c);
    }


    public boolean addAll(Collection<? extends T> c) {
        return data.items.addAll(c);
    }


    public boolean addAll(int index, Collection<? extends T> c) {
        return data.items.addAll(index, c);
    }


    public boolean removeAll(Collection<?> c) {
        return data.items.removeAll(c);
    }


    public boolean removeIf(Predicate<? super T> filter) {
        return data.items.removeIf(filter);
    }


    public boolean retainAll(Collection<?> c) {
        return data.items.retainAll(c);
    }


    public void replaceAll(UnaryOperator<T> operator) {
        data.items.replaceAll(operator);
    }


    public void sort(Comparator<? super T> c) {
        data.items.sort(c);
    }


    public void clear() {
        data.items.clear();
    }


    public boolean equals(Object o) {
        return data.items.equals(o);
    }


    public int hashCode() {
        return data.items.hashCode();
    }


    public T get(int index) {
        return data.items.get(index);
    }


    public T set(int index, T element) {
        return data.items.set(index, element);
    }


    public void add(int index, T element) {
        data.items.add(index, element);
    }


    public T remove(int index) {
        return data.items.remove(index);
    }


    public int indexOf(Object o) {
        return data.items.indexOf(o);
    }


    public int lastIndexOf(Object o) {
        return data.items.lastIndexOf(o);
    }


    public ListIterator<T> listIterator() {
        return data.items.listIterator();
    }


    public ListIterator<T> listIterator(int index) {
        return data.items.listIterator(index);
    }


    public List<T> subList(int fromIndex, int toIndex) {
        return data.items.subList(fromIndex, toIndex);
    }


    public Spliterator<T> spliterator() {
        return data.items.spliterator();
    }


    public Stream<T> stream() {
        return data.items.stream();
    }


    public Stream<T> parallelStream() {
        return data.items.parallelStream();
    }

    public List<T> toList() {
        return data.items;
    }


    private class InnerData<U> implements Serializable {
        @Expose
        private List<U> items = new LinkedList<>();
    }
}
