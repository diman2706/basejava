package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Organization {

    private final Link link;
    private List<Position> positionList;

    public Organization(Link link, List<Position> positionList) {
        this.link = link;
        this.positionList = positionList;
    }

    @Override
    public String toString() {
        return "Organization " + "(" + link + "\n" + positionList + "\n" + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization organization1 = (Organization) o;

        if (!link.equals(organization1.link)) return false;
        return positionList.equals(organization1.positionList);
    }

    @Override
    public int hashCode() {
        int result = link.hashCode();
        result = 31 * result + positionList.hashCode();
        return result;
    }

    public static class Position {
        private final String title;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String description;

        public Position(String title, LocalDate startDate, LocalDate endDate, String description) {
            Objects.requireNonNull(title, "title must mot be null");
            Objects.requireNonNull(startDate, "startDate must mot be null");
            Objects.requireNonNull(endDate, "endDate must mot be null");
            this.title = title;
            this.startDate = startDate;
            this.endDate = endDate;
            this.description = description;
        }

        @Override
        public String toString() {
            return "Position" + "(" + title + " " + startDate + " " + endDate + " " + description + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position activity = (Position) o;

            if (!title.equals(activity.title)) return false;
            if (!startDate.equals(activity.startDate)) return false;
            if (!endDate.equals(activity.endDate)) return false;
            return Objects.equals(description, activity.description);
        }

        @Override
        public int hashCode() {
            int result = title.hashCode();
            result = 31 * result + startDate.hashCode();
            result = 31 * result + endDate.hashCode();
            result = 31 * result + (description != null ? description.hashCode() : 0);
            return result;
        }
    }

}
