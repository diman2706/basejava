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
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            int sectionSize = dis.readInt();
            for (int i = 0; i < sectionSize; i++) {
                SectionType type = SectionType.valueOf(dis.readUTF());
                AbstractSection section = null;
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        section = new TextType(dis.readUTF());
                        break;
                }
                switch (type) {
                    case ACHIEVEMENTS:
                    case QUALIFICATIONS:
                        int listInfoSize = dis.readInt();
                        List<String> listOfStrings = new ArrayList<>();
                        for (int j = 0; j < listInfoSize; j++) {
                            listOfStrings.add(dis.readUTF());
                        }
                        section = new ListOfStrings(listOfStrings);
                        break;
                }
                switch (type) {
                    case EXPERIENCE:
                    case EDUCATION:
                        section = new OrganizationSection(readList(dis));
                        break;
                }
                resume.addSection(type, section);
            }
            return resume;
        }
    }

    private void writeLocalData(DataOutputStream dos, LocalDate localDate) throws IOException {
        dos.writeInt(localDate.getYear());
        dos.writeInt(localDate.getMonth().getValue());
    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }

    private List readList(DataInputStream dis) throws IOException {
        List<Organization> organizationList = new ArrayList<>();
        int organizationListSize = dis.readInt();
        for (int j = 0; j < organizationListSize; j++) {
            organizationList.add(new Organization(readLink(dis), readPositionList(dis)));
        }
        return organizationList;
    }

    private Link readLink(DataInputStream dis) throws IOException {
        return new Link(dis.readUTF(), dis.readUTF());
    }

    private List<Organization.Position> readPositionList(DataInputStream dis) throws IOException {
        int positionListSize = dis.readInt();
        List<Organization.Position> positionList = new ArrayList<>();
        for (int j = 0; j < positionListSize; j++) {
            positionList.add(readPosition(dis));
        }
        return positionList;
    }

    private Organization.Position readPosition(DataInputStream dis) throws IOException {
        return new Organization.Position(
                readLocalDate(dis),
                readLocalDate(dis),
                dis.readUTF(),
                dis.readUTF()
        );
    }

    private interface InfoWriter<T> {
        void write(T type) throws IOException;
    }

    private <T> void writeWithException(DataOutputStream dos, Collection<T> collection, InfoWriter<T> infoWriter) throws IOException {
        dos.writeInt(collection.size());
        for (T element : collection) {
            infoWriter.write(element);
        }
    }
}