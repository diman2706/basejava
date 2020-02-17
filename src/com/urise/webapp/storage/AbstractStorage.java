package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public Resume get(String uuid) {
        Object searchKey = getExistedResume(uuid);
        return getFromStorage(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getExistedResume(uuid);
        deleteFromStorage(searchKey);
    }

    @Override
    public void save(Resume resume) {
        Object searchKey = getNotExistedResume(resume.getUuid());
        saveToStorage(resume, searchKey);
    }

    @Override
    public void update(Resume resume) {
        Object searchKey = getExistedResume(resume.getUuid());
        updateFromStorage(resume, searchKey);
    }

    private Object getExistedResume(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistedResume(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract boolean isExist(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract Resume getFromStorage(Object searchKey);

    protected abstract void deleteFromStorage(Object searchKey);

    protected abstract void saveToStorage(Resume resume, Object searchKey);

    protected abstract void updateFromStorage(Resume resume, Object searchKey);

}

