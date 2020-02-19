package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {

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
    public Resume getFromStorage(Integer index) {
        return list.get(index);
    }

    @Override
    protected void deleteFromStorage(Integer index) {
        list.remove((index).intValue());
    }

    @Override
    protected void saveToStorage(Resume resume, Integer index) {
        list.add(resume);
    }

    @Override
    protected void updateFromStorage(Resume resume, Integer index) {
        resume = list.get(index);
    }

    @Override
    protected boolean isExist(Integer index) {
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
