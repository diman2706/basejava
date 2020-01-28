package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume resume) {
        int index = isResumeContainsOfStorage(resume.getUuid());
        if (isResumeContainsOfStorage(resume.getUuid()) == -1) {
            System.out.println("Resume " + resume + " is not exist");
        } else {
            storage[index] = resume;
            System.out.println("Resume " + resume + " is updated");
        }
    }

    public void save(Resume resume) {
        if (size > storage.length) {
            System.out.println("There is no space in the storage");
        } else if (isResumeContainsOfStorage(resume.getUuid()) != -1) {
            System.out.println("Resume " + resume + " has been already added");
            return;
        }
        storage[size] = resume;
        size++;
    }

    public Resume get(String uuid) {
        int index = isResumeContainsOfStorage(uuid);
        if (isResumeContainsOfStorage(uuid) != -1) {
            return storage[index];
        }
        System.out.println("Resume " + uuid + " is not exist");
        return null;
    }

    public void delete(String uuid) {
        if (isResumeContainsOfStorage(uuid) != -1) {
            int index = isResumeContainsOfStorage(uuid);
            System.arraycopy(storage, index + 1, storage, index, size - index - 1);
            size--;
            storage[size] = null;
        } else {
            System.out.println("Resume " + uuid + " is not exist");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    public int isResumeContainsOfStorage(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
