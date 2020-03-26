package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStrategy implements SerializationStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, String> contacts = resume.getContacts();
            writeWithException(dos, contacts.entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });

            Map<SectionType, AbstractSection> sections = resume.getSections();
            writeWithException(dos, sections.entrySet(), type -> {
                SectionType sectionType = type.getKey();
                AbstractSection abstractSection = type.getValue();
                dos.writeUTF(sectionType.name());
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(((TextType) abstractSection).getContent());
                        break;
                }
                switch (sectionType) {
                    case ACHIEVEMENTS:
                    case QUALIFICATIONS:
                        writeWithException(dos, ((ListOfStrings) abstractSection).getList(), dos::writeUTF);
                }
                switch (sectionType) {
                    case EXPERIENCE:
                    case EDUCATION:
                        writeWithException(dos, ((OrganizationSection) abstractSection).getOrganizations(), organizationType -> {
                            dos.writeUTF(organizationType.getLink().getName());
                            dos.writeUTF(organizationType.getLink().getUrl());
                            writeWithException(dos, organizationType.getPositionList(), positionType -> {
                                writeLocalData(dos, positionType.getStartDate());
                                writeLocalData(dos, positionType.getEndDate());
                                dos.writeUTF(positionType.getTitle());
                                dos.writeUTF(positionType.getDescription());
                            });
                        });
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readContacts(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));

            readPoints(dis, () -> {
                SectionType type = SectionType.valueOf(dis.readUTF());
                resume.addSection(type, readSections(dis, type));
                return null;
            });
            return resume;
        }
    }

    private AbstractSection readSections(DataInputStream dis, SectionType type) throws IOException {
        switch (type) {
            case PERSONAL:
            case OBJECTIVE:
                return new TextType(dis.readUTF());
        }
        switch (type) {
            case ACHIEVEMENTS:
            case QUALIFICATIONS:
                return new ListOfStrings(readPoints(dis, dis::readUTF));
        }
        switch (type) {
            case EXPERIENCE:
            case EDUCATION:
                return new OrganizationSection(
                        readPoints(dis, () -> new Organization(
                                new Link(dis.readUTF(), dis.readUTF()),
                                DataStrategy.this.readPoints(dis, () -> new Organization.Position(
                                        DataStrategy.this.readLocalDate(dis), DataStrategy.this.readLocalDate(dis), dis.readUTF(), dis.readUTF()
                                ))
                        )));
            default:
                throw new IllegalStateException();
        }
    }

    private void writeLocalData(DataOutputStream dos, LocalDate localDate) throws IOException {
        dos.writeInt(localDate.getYear());
        dos.writeInt(localDate.getMonth().getValue());
    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }

    private interface InfoWriter<T> {
        void write(T type) throws IOException;
    }

    private interface ContactReader {
        void readObject() throws IOException;
    }

    private interface PointReader<T> {
        T readPoint() throws IOException;
    }

    private <T> void writeWithException(DataOutputStream dos, Collection<T> collection, InfoWriter<T> infoWriter) throws IOException {
        dos.writeInt(collection.size());
        for (T element : collection) {
            infoWriter.write(element);
        }
    }

    private void readContacts(DataInputStream dis, ContactReader contactReader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            contactReader.readObject();
        }
    }

    private <T> List<T> readPoints(DataInputStream dis, PointReader<T> pointReader) throws IOException {
        int size = dis.readInt();
        List<T> pointsList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            pointsList.add(pointReader.readPoint());
        }
        return pointsList;
    }

}