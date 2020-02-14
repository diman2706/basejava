package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {

    private Map<String, Resume> map = new HashMap<>();

    @Override
    protected boolean isExist(Object index) {
        return index != null;
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return map.get(uuid);
    }

    @Override
    protected Resume getFromStorage(Object index) {
        return (Resume) index;
    }

    @Override
    protected void deleteFromStorage(Object index) {
        map.remove(((Resume) index).getUuid());
    }

    @Override
    protected void saveToStorage(Resume resume, Object index) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    protected void updateFromStorage(Resume resume, Object index) {
        resume = map.get(index);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Resume[] getAll() {
        Resume[] resume = map.values().toArray(new Resume[map.size()]);
        Arrays.sort(resume);
        return resume;
    }

    @Override
    public int size() {
        return map.size();
    }
}