package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected Storage storage;

    protected static final File DIRECTORY = Config.get().getStorageDir();

    private static final String UUID_1 = UUID.randomUUID().toString();
    private static final String UUID_2 = UUID.randomUUID().toString();
    private static final String UUID_3 = UUID.randomUUID().toString();
    private static final String UUID_4 = UUID.randomUUID().toString();

    private static final Resume R1;
    private static final Resume R2;
    private static final Resume R3;
    private static final Resume R4;

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
        R1.addSection(SectionType.ACHIEVEMENTS, new ListOfStrings("Achivment11", "Achivment12", "Achivment13","Oppa"));
        R1.addSection(SectionType.QUALIFICATIONS, new ListOfStrings("Java", "SQL", "JavaScript"));
        /*R1.addSection(SectionType.EXPERIENCE,
            new OrganizationSection(
                        new Organization("Organization1", "http://Organization1.ru",
            new Organization.Position(2005, Month.JANUARY, "position1", "content1"),
                                new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "position2", "content2"))));
        R1.addSection(SectionType.EDUCATION,
            new OrganizationSection(
                        new Organization("Institute", null,
            new Organization.Position(1996, Month.JANUARY, 2000, Month.DECEMBER, "aspirant", null),
                                new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "student", "IT facultet")),
            new Organization("Organization2", "http://Organization2.ru")));
        R2.addContact(ContactType.SKYPE, "skype2");
        R2.addContact(ContactType.PHONENUMBER, "22222");
        R1.addSection(SectionType.EXPERIENCE,
            new OrganizationSection(
                        new Organization("Organization3", "http://Organization3.ru",
            new Organization.Position(2015, Month.JANUARY, "position3", "content3"))));*/
    }

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
    }

    @Test
    public void size() throws Exception {
        assertEquals(3, storage.size());
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test()
    public void update() throws Exception {
        Resume newResume = new Resume(UUID_1, "New Name");
        newResume.addContact(ContactType.PHONENUMBER, "NEW");
        newResume.addContact(ContactType.MAIL, "NEW MAIL");
        storage.update(newResume);
        assertEquals(newResume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.get("dummy");
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> list = storage.getAllSorted();
        assertEquals(3, list.size());
        List<Resume> sortedResumes = new ArrayList<>();
        sortedResumes.add(R1);
        sortedResumes.add(R2);
        sortedResumes.add(R3);
        Collections.sort(sortedResumes);
        assertEquals(list, sortedResumes);
    }

    @Test
    public void save() throws Exception {
        storage.save(R4);
        assertEquals(4, storage.size());
        assertEquals(R4, storage.get(R4.getUuid()));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(R1);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        storage.delete(UUID_1);
        assertEquals(2, storage.size());
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete("dummy");
    }

    @Test
    public void get() throws Exception {
        assertEquals(R1, storage.get(R1.getUuid()));
        assertEquals(R2, storage.get(R2.getUuid()));
        assertEquals(R3, storage.get(R3.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }
}