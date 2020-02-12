package com.urise.webapp.storage;

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
    public Resume getFromStorage(Object index) {
        return list.get((Integer) index);
    }

    @Override
    protected void deleteFromStorage(Object index) {
        list.remove(((Integer) index).intValue());
    }

    @Override
    protected void saveToStorage(Resume resume, Object index) {
        list.add(resume);
    }

    @Override
    protected void updateFromStorage(Resume resume, Object index) {
        resume = list.get((Integer) index);
    }

    @Override
    protected boolean isExist(Object index) {
        return index != null;
    }

    @Override
    protected void isOverflow(Resume resume) {
        if (list.size() >= AbstractArrayStorage.STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
    }

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid() == uuid) {
                return i;
            }
        }
        return null;
    }
}
