package com.urise.webapp.model;

import java.util.List;
import java.util.Objects;

public class ListOfPositions extends AbstractSection {

    private final List<Position> positions;

    public ListOfPositions(List<Position> positions) {
        Objects.requireNonNull(positions, "positions must mot be null");
        this.positions = positions;
    }

    @Override
    public String toString() {
        return positions.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListOfPositions that = (ListOfPositions) o;

        return positions.equals(that.positions);
    }

    @Override
    public int hashCode() {
        return positions.hashCode();
    }
}
