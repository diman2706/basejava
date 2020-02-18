package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorageUUID extends AbstractStorage {

    private Map<String, Resume> map = new HashMap<>();

    @Override
    protected boolean isExist(Object uuid) {
        return map.containsKey(uuid);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected Resume getFromStorage(Object uuid) {
        return map.get(uuid);
    }

    @Override
    protected void deleteFromStorage(Object uuid) {
        map.remove(uuid);
    }

    @Override
    protected void saveToStorage(Resume resume, Object uuid) {
        map.put((String) uuid, resume);
    }

    @Override
    protected void updateFromStorage(Resume resume, Object uuid) {
        map.put((String) uuid, resume);
    }

    @Override
    protected List<Resume> getAllResume() {
        return new ArrayList<>(map.values());
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }
}
