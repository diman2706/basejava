package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.ObjectInputOutputStrategy;

public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(DIRECTORY.getAbsolutePath(), new ObjectInputOutputStrategy()));
    }
}
