package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {

    private Map<String, Resume> map = new HashMap<>();

    @Override
    protected boolean isExist(Object searchKey) {
        return searchKey != null;
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return map.get(uuid);
    }

    @Override
    protected Resume getFromStorage(Object searchKey) {
        return (Resume) searchKey;
    }

    @Override
    protected void deleteFromStorage(Object searchKey) {
        map.remove(((Resume) searchKey).getUuid());
    }

    @Override
    protected void saveToStorage(Resume resume, Object searchKey) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    protected void updateFromStorage(Resume resume, Object searchKey) {
        map.put(resume.getUuid(), resume);
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