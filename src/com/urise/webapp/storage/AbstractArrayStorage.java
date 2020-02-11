package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;
    private int index;

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
    public Resume getFromStorage(Resume resume) {
        return storage[index];
    }

    public void deleteFromStorage(Resume resume) {
        deleteResume(index);
        storage[size - 1] = null;
        size--;
    }

    public void saveToStorage(Resume resume) {
        insertResume(resume, index);
        size++;
    }

    public void updateFromStorage(Resume resume) {
        storage[index] = resume;
        System.out.println("Resume " + resume + " is updated");
    }

    @Override
    protected void isNotExist(Resume resume) {
        index = getIndex(resume.getUuid());
        if (index < 0) {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    @Override
    protected void isExist(Resume resume) {
        index = getIndex(resume.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(resume.getUuid());
        }
    }

    @Override
    protected void isOverflow(Resume resume) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
    }

    protected abstract int getIndex(String uuid);

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void deleteResume(int index);
}