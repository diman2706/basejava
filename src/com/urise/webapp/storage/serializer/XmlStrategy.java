package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;
import com.urise.webapp.model.Resume;
import com.urise.webapp.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlStrategy implements SerializationStrategy {

    private XmlParser xmlParser;

    public XmlStrategy() {
        xmlParser = new XmlParser(
                Resume.class, Organization.class, Link.class,
                OrganizationSection.class, TextType.class, ListOfStrings.class, Organization.Position.class);
    }

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (Writer streamWriter = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            xmlParser.marshall(resume, streamWriter);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (Reader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return xmlParser.unmarshall(streamReader);
        }
    }
}