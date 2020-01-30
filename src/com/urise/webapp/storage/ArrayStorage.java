package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index == -1) {
            System.out.println("Resume " + resume + " is not exist");
        } else {
            storage[index] = resume;
            System.out.println("Resume " + resume + " is updated");
        }
    }

    @Override
    protected void InsertResume(Resume resume) {
        storage[size] = resume;
        size++;
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            System.arraycopy(storage, index + 1, storage, index, size - index - 1);
            size--;
            storage[size] = null;
        } else {
            System.out.println("Resume " + uuid + " is not exist");
        }
    }

    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}