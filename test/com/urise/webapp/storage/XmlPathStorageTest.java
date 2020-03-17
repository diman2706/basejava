package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.XmlStrategy;

public class XmlPathStorageTest extends AbstractStorageTest {

    public XmlPathStorageTest() {
        super(new PathStorage(DIRECTORY.getAbsolutePath(), new XmlStrategy()));
    }
}