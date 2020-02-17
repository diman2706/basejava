package com.urise.webapp.storage;

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
    protected List<Resume> getAllResume() {
        return list;
    }

    @Override
    public Resume getFromStorage(Object index) {
        return list.get((int) index);
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
        resume = list.get((int) index);
    }

    @Override
    protected boolean isExist(Object index) {
        return index != null;
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid() == uuid) {
                return i;
            }
        }
        return null;
    }
}
