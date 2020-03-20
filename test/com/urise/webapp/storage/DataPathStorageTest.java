package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.DataStrategy;

public class DataPathStorageTest extends AbstractStorageTest {

    public DataPathStorageTest() {
        super(new PathStorage(DIRECTORY.getAbsolutePath(), new DataStrategy()));
    }
}