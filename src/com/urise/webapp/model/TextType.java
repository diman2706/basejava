package com.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Objects;

public class TextType extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private String content;

    public TextType(String content) {
        Objects.requireNonNull(content, "content must not be null");
        this.content = content;
    }

    public TextType() {
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
