package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.ObjectInputOutputStrategy;

public class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {
        super(new FileStorage(DIRECTORY,new ObjectInputOutputStrategy()));
    }
}
