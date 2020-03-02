package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Position {

    private final Link position;
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String description;

    public Position(Link position, String title, LocalDate startDate, LocalDate endDate, String description) {
        Objects.requireNonNull(title, "title must mot be null");
        Objects.requireNonNull(startDate, "startDate must mot be null");
        Objects.requireNonNull(endDate, "endDate must mot be null");
        this.position = position;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Position{" +
                "position=" + position + "\n"+
                " title='" + title + "\n"+'\'' +
                " startDate=" + startDate +"\n"+
                " endDate=" + endDate +"\n"+
                " description='" + description + '\'' +"\n"+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position that = (Position) o;

        if (!position.equals(that.position)) return false;
        if (!title.equals(that.title)) return false;
        if (!startDate.equals(that.startDate)) return false;
        if (!endDate.equals(that.endDate)) return false;
        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}
