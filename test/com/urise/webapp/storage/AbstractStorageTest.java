package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.urise.webapp.TestData.*;
import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected Storage storage;

    protected static final File DIRECTORY = Config.get().getStorageDir();

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