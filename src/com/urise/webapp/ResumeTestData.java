package com.urise.webapp;

import com.urise.webapp.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume resume = new Resume("uuid");

        Link link = new Link("Java Online Projects", "http://javaops.ru/");
        Link link1 = new Link("Coursera", "https://www.coursera.org/course/progfun");

        LocalDate date0 = LocalDate.of(2014, 10, 1);
        LocalDate date1 = LocalDate.of(2013, 3, 1);
        LocalDate date2 = LocalDate.of(2013, 5, 1);

        List<Position.Activity> activityListExperience = new ArrayList<>();
        activityListExperience.add(new Position.Activity("Автор проекта.", date0, LocalDate.now(), "Создание, организация и проведение Java онлайн проектов и стажировок."));
        activityListExperience.add(new Position.Activity("Специальность", date1, date2, "Описание Специальности"));

        List<Position.Activity> activityListEducation = new ArrayList<>();
        activityListEducation.add(new Position.Activity("\"Functional Programming Principles in Scala\" by Martin Odersky", date1, date2, null));

        Position position = new Position(link, activityListExperience);
        Position position1 = new Position(link1, activityListEducation);

        List<Position> listExperiencePositions = new ArrayList<>();
        listExperiencePositions.add(position);

        List<Position> listEducatePositions = new ArrayList<>();
        listEducatePositions.add(position1);

        List<String> listOfAchievements = new ArrayList<>();
        listOfAchievements.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise");
        listOfAchievements.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike.");
        listOfAchievements.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. ");

        List<String> listOfQualifications = new ArrayList<>();
        listOfQualifications.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2 ");
        listOfQualifications.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle ");
        listOfQualifications.add("MySQL, SQLite, MS SQL, HSQLDB ");

        String uuid = resume.getUuid();
        System.out.println("uuid : " + uuid);
        System.out.println();

        Map<ContactType, String> contacts = resume.getContacts();
        contacts.put(ContactType.PHONENUMBER, "+7(921) 855-0482");
        contacts.put(ContactType.SKYPE, "grigory.kislin");
        contacts.put(ContactType.MAIL, "gkislin@yandex.ru");
        contacts.put(ContactType.LINCKEDLN, "Профиль LinkedIn");
        contacts.put(ContactType.GITHUB, "Профиль GitHub");
        contacts.put(ContactType.STACKOVERFLOW, "Профиль StackOverflow");

        Map<SectionType, AbstractSection> sections = resume.getSections();
        AbstractSection personal = new TextType("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");
        AbstractSection objective = new TextType("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");

        AbstractSection achievements = new ListOfStrings(listOfAchievements);
        AbstractSection qualifications = new ListOfStrings(listOfQualifications);

        AbstractSection experience = new ListOfPositions(listExperiencePositions);
        AbstractSection education = new ListOfPositions(listEducatePositions);

        sections.put(SectionType.PERSONAL, personal);
        sections.put(SectionType.OBJECTIVE, objective);
        sections.put(SectionType.ACHIEVEMENTS, achievements);
        sections.put(SectionType.QUALIFICATIONS, qualifications);
        sections.put(SectionType.EXPERIENCE, experience);
        sections.put(SectionType.EDUCATION, education);

      //  System.out.println(resume.getContacts());
      //  System.out.println();
      //  System.out.println(resume.getSections());
        System.out.println(resume.getSections().get(SectionType.EDUCATION));
    }
}
