package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStrategy implements SerializationStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, String> contacts = resume.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

            Map<SectionType, AbstractSection> sections = resume.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                switch (entry.getKey()) {
                    case PERSONAL:
                    case OBJECTIVE:
                        AbstractSection section = entry.getValue();
                        String info = ((TextType) section).getContent();
                        dos.writeUTF(info);
                        break;
                }
                switch (entry.getKey()) {
                    case ACHIEVEMENTS:
                    case QUALIFICATIONS:
                        AbstractSection section = entry.getValue();
                        List<String> listInfo = ((ListOfStrings) section).getList();
                        dos.writeInt(listInfo.size());
                        for (String info : listInfo) {
                            dos.writeUTF(info);
                        }
                        break;
                }
                switch (entry.getKey()) {
                    case EXPERIENCE:
                    case EDUCATION:
                        AbstractSection section = entry.getValue();
                        List<Organization> listOrganization = ((OrganizationSection) section).getOrganizations();
                        dos.writeInt(listOrganization.size());
                        for (Organization organization : listOrganization) {
                            dos.writeUTF(organization.getLink().getName() == null ? "" : organization.getLink().getName());
                            dos.writeUTF(organization.getLink().getUrl() == null ? "" : organization.getLink().getUrl());
                            List<Organization.Position> positionList = organization.getPositionList();
                            dos.writeInt(positionList.size());
                            for (Organization.Position position : positionList) {
                                writeLocalData(dos, position.getStartDate());
                                writeLocalData(dos, position.getEndDate());
                                dos.writeUTF(position.getTitle() == null ? "" : position.getTitle());
                                dos.writeUTF(position.getDescription() == null ? "" : position.getDescription());
                            }
                        }
                        break;
                }
            }
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
            for (int i = 0; i < sectionSize - 1; i++) {
                SectionType type = SectionType.valueOf(dis.readUTF());
                AbstractSection section = null;
                if (type == SectionType.PERSONAL || type == SectionType.OBJECTIVE) {
                    section = new TextType(dis.readUTF());
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
}