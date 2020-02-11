package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {

    private List<Resume> list = new ArrayList<>();

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Resume[] getAll() {
        return list.toArray(new Resume[list.size()]);
    }

    @Override
    public Resume getFromStorage(Resume resume) {
        return list.get(getIndex(resume.getUuid()));
    }

    @Override
    protected void deleteFromStorage(Resume resume) {
        list.remove(resume);
    }

    @Override
    protected void saveToStorage(Resume resume) {
        list.add(resume);
    }

    @Override
    protected void updateFromStorage(Resume resume) {
        resume = list.get(getIndex(resume.getUuid()));
    }

    @Override
    protected void isNotExist(Resume resume) {
        if (!list.contains(resume)) {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    @Override
    protected void isExist(Resume resume) {
        if (list.contains(resume)) {
            throw new ExistStorageException(resume.getUuid());
        }
    }

    @Override
    protected void isOverflow(Resume resume) {
        if (list.size() >= AbstractArrayStorage.STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid() == uuid) {
                return i;
            }
        }
        return -1;
    }
}
