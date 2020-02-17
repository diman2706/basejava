package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    protected List<Resume> getAllResume() {
        List<Resume> list = new ArrayList<>();
        for (Map.Entry<String, Resume> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    @Override
    public int size() {
        return map.size();
    }
}