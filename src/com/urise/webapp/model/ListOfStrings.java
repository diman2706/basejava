package com.urise.webapp.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListOfStrings extends AbstractSection {

    private final List<String> list;

    public ListOfStrings(String... list) {
        this(Arrays.asList(list));
    }

    public ListOfStrings(List<String> list) {
        Objects.requireNonNull(list, "list must not be null");
        this.list = list;
    }

    @Override
    public String toString() {
        return list.toString() + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListOfStrings that = (ListOfStrings) o;

        return list.equals(that.list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    public List<String> getList() {
        return list;
    }
}
