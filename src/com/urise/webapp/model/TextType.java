package com.urise.webapp.model;

import java.util.Objects;

public class TextType extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private final String content;

    public TextType(String content) {
        Objects.requireNonNull(content, "content must not be null");
        this.content = content;
    }

    @Override
    public String toString() {
        return content + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextType textType = (TextType) o;

        return content.equals(textType.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
