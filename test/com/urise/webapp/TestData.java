package com.urise.webapp;

import com.urise.webapp.model.*;

import java.time.Month;
import java.util.UUID;

public class TestData {
    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();
    public static final String UUID_4 = UUID.randomUUID().toString();

    public static final Resume R1;
    public static final Resume R2;
    public static final Resume R3;
    public static final Resume R4;

    static {
        R1 = new Resume(UUID_1, "Name1");
        R2 = new Resume(UUID_2, "Name2");
        R3 = new Resume(UUID_3, "Name3");
        R4 = new Resume(UUID_4, "Name4");

        R1.addContact(ContactType.PHONENUMBER, "11111");
        R1.addContact(ContactType.MAIL, "mail111@ya.ru");

        R2.addContact(ContactType.PHONENUMBER, "22222");
        R2.addContact(ContactType.MAIL, "mail222@ya.ru");

        R3.addContact(ContactType.PHONENUMBER, "3333");
        R3.addContact(ContactType.MAIL, "mail333@ya.ru");

        R4.addContact(ContactType.PHONENUMBER, "44444");
        R4.addContact(ContactType.MAIL, "mail444@ya.ru");
        R1.addSection(SectionType.PERSONAL, new TextType("Personal data"));
        R1.addSection(SectionType.OBJECTIVE, new TextType("Objective1"));
        R1.addSection(SectionType.ACHIEVEMENTS, new ListOfStrings("Achivment11", "Achivment12", "Achivment13", "Oppa"));
        R1.addSection(SectionType.QUALIFICATIONS, new ListOfStrings("Java", "SQL", "JavaScript"));
        R1.addSection(SectionType.EDUCATION, new ListOfStrings("Java", "SQL", "JavaScript"));
        R1.addSection(SectionType.EXPERIENCE,
                new OrganizationSection(
                        new Organization("Organization2", "http://Organization2.ru",
                                new Organization.Position(2015, Month.JANUARY, "position1", "content1"))));
        R1.addSection(SectionType.EDUCATION,
                new OrganizationSection(
                        new Organization("Institute", null,
                                new Organization.Position(1996, Month.JANUARY, 2000, Month.DECEMBER, "aspirant", null),
                                new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "student", "IT facultet")),
                        new Organization("Organization12", "http://Organization12.ru")));
    }
}
