package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void update(Resume resume) {
        isNotExist(resume.getUuid());
        refresh(resume);
    }

    @Override
    public void save(Resume resume) {
        isOverflow(resume);
        isExist(resume);
        supplement(resume);
    }

    @Override
    public Resume get(String uuid) {
        isNotExist(uuid);
        return getFrom(uuid);
    }

    @Override
    public void delete(String uuid) {
        isNotExist(uuid);
        extract(uuid);
    }

    protected abstract void isExist(Resume resume);

    protected abstract void isNotExist(String uuid);

    protected abstract void supplement(Resume resume);

    protected abstract void extract(String uuid);

    protected abstract Resume getFrom(String uuid);

    protected abstract void refresh(Resume resume);

    protected abstract void isOverflow(Resume resume);
}
