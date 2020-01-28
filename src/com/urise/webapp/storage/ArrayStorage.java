package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size = 0;
    private Resume foundResume;
    private int numberResume;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume resume) {
        if (isResumeContainsOfStorage(resume)) {
            foundResume = resume;
        } else {
            System.out.println("Resume is not exist");
        }
    }

    public void save(Resume resume) {
        if (size > storage.length) {
            System.out.println("There is no space in the storage");
        } else {
            if (isResumeContainsOfStorage(resume)) {
                System.out.println("Resume has been already added");
                return;
            }
            storage[size] = resume;
            size++;
        }
    }

    public Resume get(String uuid) {
        if (isResumeContainsOfStorage(uuid)) {
            return foundResume;
        } else {
            System.out.println("Resume is not exist");
        }
        return null;
    }

    public void delete(String uuid) {
        int index;
        if (isResumeContainsOfStorage(uuid)) {
            index = numberResume;
            System.arraycopy(storage, index + 1, storage, index, size - index - 1);
            size--;
            storage[size] = null;
        } else {
            System.out.println("Resume is not exist");
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

    public boolean isResumeContainsOfStorage(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                foundResume = storage[i];
                numberResume = i;
                return true;
            }
        }
        return false;
    }

    public boolean isResumeContainsOfStorage(Resume resume) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(resume.getUuid())) {
                foundResume = storage[i];
                numberResume = i;
                return true;
            }
        }
        return false;
    }
}

