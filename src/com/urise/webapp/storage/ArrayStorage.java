package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size = 0;

    public void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }
    public void update(Resume r){

    }

    public void save(Resume r) {
        // если такого резюме нет в сторадже то добавить его
        storage[size] = r;
        size++;
    }

    public Resume get(String uuid) {
            int quantity = 0;
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) return storage[i];
            else quantity ++;
            if (quantity == size) System.out.println("Resume is not exist");
        }
        return null;
    }

    public void delete(String uuid) {
        int quantity = 0;
        int index;
        for (int j = 0; j < size; j++) {
            if (storage[j].getUuid().equals(uuid)) {
                index = j;
                System.arraycopy(storage, index + 1, storage, index, size - index - 1);
                size--;
                storage[size] = null;
                break;
            }
            else quantity ++;
            if (quantity == size) System.out.println("Resume is not exist");
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
}

