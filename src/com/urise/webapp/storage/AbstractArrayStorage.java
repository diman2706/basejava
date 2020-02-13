package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    @Override
    public Resume getFromStorage(Object index) {
        return storage[(int) index];
    }

    public void deleteFromStorage(Object index) {
        deleteResume((int) index);
        storage[size - 1] = null;
        size--;
    }

    public void saveToStorage(Resume resume, Object index) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        } else {
            insertResume(resume, (int) index);
            size++;
        }
    }

    public void updateFromStorage(Resume resume, Object index) {
        storage[(int) index] = resume;
        System.out.println("Resume " + resume + " is updated");
    }

    @Override
    protected boolean isExist(Object index) {
        return (int) index >= 0;
    }

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void deleteResume(int index);
}