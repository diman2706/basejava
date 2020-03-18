package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.JsonStrategy;

public class JsonPathStorageTest extends AbstractStorageTest {

    public JsonPathStorageTest() {
        super(new PathStorage(DIRECTORY.getAbsolutePath(), new JsonStrategy()));
    }
}
