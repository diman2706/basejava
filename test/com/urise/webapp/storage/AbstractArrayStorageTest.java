package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractArrayStorageTest {
    private Storage storage;
    private static final int STORAGE_LIMIT = 10000;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final Resume resume = new Resume();

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test()
    public void update() throws Exception {
        storage.save(resume);
        storage.update(resume);
        Assert.assertEquals(storage.get(resume.getUuid()), resume);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(resume);
    }

    @Test
    public void getAll() throws Exception {
        Resume[] resumes = new Resume[3];
        resumes[0] = storage.get(UUID_1);
        resumes[1] = storage.get(UUID_2);
        resumes[2] = storage.get(UUID_3);
        Assert.assertArrayEquals(resumes, storage.getAll());
    }

    @Test
    public void save() throws Exception {
        storage.save(resume);
        Assert.assertEquals(4, storage.size());
        storage.get(resume.getUuid());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(new Resume(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        storage.delete(UUID_1);
        Assert.assertEquals(2, storage.size());
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete("UUID_4");
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(UUID_1, storage.get(UUID_1).getUuid());
        Assert.assertEquals(UUID_2, storage.get(UUID_2).getUuid());
        Assert.assertEquals(UUID_3, storage.get(UUID_3).getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }

    @Test(expected = StorageException.class)
    public void saveOverflowStorage() throws Exception {
        try {
            for (int i = 4; i < STORAGE_LIMIT +1; i++) {
                storage.save(new Resume());
            }
        } catch (Exception StorageException) {
            Assert.fail("Storage overflow");
        }
        storage.save(new Resume());
    }
}