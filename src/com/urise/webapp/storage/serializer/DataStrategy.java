package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStrategy implements SerializationStrategy {
    String info;
    List<String> listInfo;
    List<Organization> listOrganization;
    Link link;
    List<Organization.Position> positionList;
    List<String> InfoOrganization = new ArrayList<>();

    private void getInfo(AbstractSection section) {
        if (section instanceof TextType) {
            info = ((TextType) section).getContent();
        }
        if (section instanceof ListOfStrings) {
            listInfo = ((ListOfStrings) section).getList();
        }
        if (section instanceof OrganizationSection) {
            listOrganization = ((OrganizationSection) section).getOrganizations();
            getInfoFromOrganization(listOrganization);
        }
    }

    private void getInfoFromOrganization(List<Organization> listOrganization) {
        for (Organization organization : listOrganization) {
            link = organization.getLink();
            positionList = organization.getPositionList();
            getInfoLink(link);
            getInfoPosition(positionList);
        }
    }

    private void getInfoLink(Link link) {
        String linkName = link.getName();
        String linkUrl = link.getUrl();
        InfoOrganization.add(linkName);
        InfoOrganization.add(linkUrl);
    }

    private void getInfoPosition(List<Organization.Position> positionList) {
        for (Organization.Position position : positionList) {
            String title = position.getTitle();
            String description = position.getDescription();
            LocalDate startDate = position.getStartDate();
            LocalDate endDate = position.getEndDate();
            InfoOrganization.add(startDate.toString());
            InfoOrganization.add(endDate.toString());
            InfoOrganization.add(title);
            InfoOrganization.add(description);
        }
    }

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
            // TODO implements sections

            Map<SectionType, AbstractSection> sections = resume.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                if (entry.getKey() == SectionType.PERSONAL || entry.getKey() == SectionType.OBJECTIVE) {
                    getInfo(entry.getValue());
                    dos.writeUTF(info);
                }
                if (entry.getKey() == SectionType.ACHIEVEMENTS || entry.getKey() == SectionType.QUALIFICATIONS) {
                    getInfo(entry.getValue());
                    dos.writeInt(listInfo.size());
                    for (String info : listInfo) {
                        dos.writeUTF(info);
                    }
                }
                if (entry.getKey() == SectionType.EXPERIENCE || entry.getKey() == SectionType.EDUCATION) {
                    getInfo(entry.getValue());
                    dos.writeInt(listOrganization.size());
                    dos.writeInt(positionList.size());
                    for (String s : InfoOrganization) {
                        if (s != null) {
                            dos.writeUTF(s);
                        } else dos.writeUTF("");
                    }
                    InfoOrganization.clear();
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
            // TODO implements sections

            int sectionSize = dis.readInt();
            for (int i = 0; i < sectionSize - 1; i++) {
                SectionType type = SectionType.valueOf(dis.readUTF());
                AbstractSection section = null;
                if (type == SectionType.PERSONAL || type == SectionType.OBJECTIVE) {
                    section = new TextType(dis.readUTF());
                }
                if (type == SectionType.ACHIEVEMENTS || type == SectionType.QUALIFICATIONS) {
                    int listInfoSize = dis.readInt();
                    List<String> listOfStrings = new ArrayList<>();
                    for (int j = 0; j < listInfoSize; j++) {
                        listOfStrings.add(dis.readUTF());
                    }
                    section = new ListOfStrings(listOfStrings);
                }
                if (type == SectionType.EXPERIENCE || type == SectionType.EDUCATION) {

                    List<Organization> organizationList = new ArrayList<>();
                    int organizationListSize = dis.readInt();
                    int positionListSize = dis.readInt();

                    Link link = new Link(dis.readUTF(), dis.readUTF());
                    Organization.Position position = new Organization.Position(
                            LocalDate.parse(dis.readUTF()),
                            LocalDate.parse(dis.readUTF()),
                            dis.readUTF(),
                            dis.readUTF()
                    );

                    List<Organization.Position> positionList = new ArrayList<>();
                    for (int j = 0; j < positionListSize; j++) {
                        positionList.add(position);
                    }

                    for (int j = 0; j < organizationListSize; j++) {
                        organizationList.add(new Organization(link, positionList));
                    }
                    section = new OrganizationSection(organizationList);
                }
                resume.addSection(type, section);
            }
            return resume;
        }
    }
}