package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writeable");
        }
        this.directory = directory;
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected Resume getFromStorage(File file) {
        return doGet(file);
    }

    @Override
    protected void deleteFromStorage(File file) {
        if (!file.delete()) {
            throw new StorageException("Delete error", file.getName());
        }
    }

    @Override
    protected void saveToStorage(Resume resume, File file) {
        try {
            if (file.createNewFile()) {
                doWrite(resume, file);
            }
        } catch (IOException e) {
            throw new StorageException("Save error", file.getName(), e);
        }
    }

    @Override
    protected void updateFromStorage(Resume resume, File file) {
        try {
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException("Update error", file.getName(), e);
        }
    }

    @Override
    protected List<Resume> getAllResume() {
        List<Resume> list = new ArrayList<>();
        File[] files = directory.listFiles();
        if (!(files == null)) {
            for (File file : files) {
                list.add(getFromStorage(file));
            }
        }
        return list;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (!(files == null)) {
            for (File file : files) {
                deleteFromStorage(file);
            }
        }
    }

    @Override
    public int size() {
        File[] files = directory.listFiles();
        if (!(files == null)) {
            return files.length;
        }
        return 0;
    }

    protected abstract void doWrite(Resume resume, File file) throws IOException;

    protected abstract Resume doGet(File file);
}