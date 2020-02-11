package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected void isExist(Resume resume) {

    }

    @Override
    protected void isNotExist(String uuid) {

    }

    @Override
    protected void supplement(Resume resume) {

    }

    @Override
    protected void extract(String uuid) {

    }

    @Override
    protected Resume getFrom(String uuid) {
        return null;
    }

    @Override
    protected void refresh(Resume resume) {

    }

    @Override
    protected void isOverflow(Resume resume) {

    }

    @Override
    protected void insertResume(Resume resume, int index) {
        storage[size] = resume;
    }

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void deleteResume(int index) {
        storage[index] = storage[size - 1];
    }
}