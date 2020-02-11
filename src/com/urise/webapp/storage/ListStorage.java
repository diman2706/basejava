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
    public void clear() {
        list.clear();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Resume[] getAll() {
        return list.toArray(new Resume[list.size()]);
    }

    @Override
    protected Resume getFrom(String uuid) {
        return list.get(getIndex(uuid));
    }

    @Override
    protected void refresh(Resume resume) {
        resume = list.get(getIndex(resume.getUuid()));
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid() == uuid) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void extract(String uuid) {
        list.remove(getIndex(uuid));
    }

    @Override
    protected void isExist(Resume resume) {
        for (Resume value : list) {
            if (value.equals(resume)) {
                throw new ExistStorageException(resume.getUuid());
            }
        }
    }

    @Override
    protected void isNotExist(String uuid) {
        int count = 0;
        if (list.size() == 0) {
            throw new NotExistStorageException(uuid);
        }
        for (Resume resume : list) {
            if (resume.getUuid() == uuid) {
                count++;
            }
        }
        if (count == 0) {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    protected void supplement(Resume resume) {
        list.add(resume);
    }

    @Override
    protected void isOverflow(Resume resume) {
        if (list.size() >= AbstractArrayStorage.STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
    }
}
