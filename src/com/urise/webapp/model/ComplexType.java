package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class ComplexType extends Section {

    private final Link link;
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String content;

    public ComplexType(Link link, String title, LocalDate startDate, LocalDate endDate, String content) {
        Objects.requireNonNull(title, "title must mot be null");
        Objects.requireNonNull(startDate, "startDate must mot be null");
        Objects.requireNonNull(endDate, "endDate must mot be null");
        this.link = link;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
    }

    @Override
    public String toString() {
        return "ComplexType{" +
                "link=" + link +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplexType that = (ComplexType) o;

        if (!link.equals(that.link)) return false;
        if (!title.equals(that.title)) return false;
        if (!startDate.equals(that.startDate)) return false;
        if (!endDate.equals(that.endDate)) return false;
        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        int result = link.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + content.hashCode();
        return result;
    }
}
