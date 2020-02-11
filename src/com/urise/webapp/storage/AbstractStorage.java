package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    private Resume resume;

    @Override
    public Resume get(String uuid) {
        conditions(uuid);
        return getFromStorage(resume);
    }

    @Override
    public void delete(String uuid) {
        conditions(uuid);
        deleteFromStorage(resume);
    }

    @Override
    public void save(Resume resume) {
        isOverflow(resume);
        isExist(resume);
        saveToStorage(resume);
    }

    @Override
    public void update(Resume resume) {
        isNotExist(resume);
        updateFromStorage(resume);
    }

    protected abstract void isNotExist(Resume resume);

    protected abstract void isExist(Resume resume);

    protected abstract void isOverflow(Resume resume);

    protected abstract Resume getFromStorage(Resume resume);

    protected abstract void deleteFromStorage(Resume resume);

    protected abstract void saveToStorage(Resume resume);

    protected abstract void updateFromStorage(Resume resume);

    private void conditions(String uuid) {
        resume = new Resume(uuid);
        isNotExist(resume);
    }
}

