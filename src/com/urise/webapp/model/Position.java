package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Position {

    private final Link position;
    private List<Activity> activityList;

    public Position(Link position, List<Activity> activityList) {
        this.position = position;
        this.activityList = activityList;
    }

    @Override
    public String toString() {
        return "Position{" +
                "position=" + position +
                ", activityList=" + activityList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position1 = (Position) o;

        if (!position.equals(position1.position)) return false;
        return activityList.equals(position1.activityList);
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + activityList.hashCode();
        return result;
    }

    public static class Activity {
        private final String title;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String description;

        public Activity(String title, LocalDate startDate, LocalDate endDate, String description) {
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
            return "Activity{" +
                    "title='" + title + '\'' +
                    ", startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", description='" + description + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Activity activity = (Activity) o;

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
