package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Sections {

    private static SectionType type;

    public static void main(String[] args) {
        if (type == SectionType.PERSONAL && type == SectionType.OBJECTIVE) {  // пока набросал тут черновик, не думая что тут всегда фолз.
            TextType.addInfo();
        }
        if (type == SectionType.ACHIEVEMENT && type == SectionType.QUALIFICATIONS) {
            SplitTextType.addInfo();
        }
        if (type == SectionType.EXPERIENCE&& type == SectionType.EDUCATION) {
            TypeWithReference.addInfo();
        }
    }
    protected static void addInfo(){};
}
