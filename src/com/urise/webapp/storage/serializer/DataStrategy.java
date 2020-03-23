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
                        for (int i = 0; i < listOrganization.size(); i++) {
                            dos.writeUTF(listOrganization.get(i).getLink().getName() == null ? "" : listOrganization.get(i).getLink().getName());
                            dos.writeUTF(listOrganization.get(i).getLink().getUrl() == null ? "" : listOrganization.get(i).getLink().getUrl());
                            List<Organization.Position> positionList = listOrganization.get(i).getPositionList();
                            dos.writeInt(positionList.size());
                            for (int j = 0; j < positionList.size(); j++) {
                            //   dos.writeUTF(positionList.get(j).getStartDate().toString());
                             //  dos.writeUTF(positionList.get(j).getEndDate().toString());

                               writeLocalData(dos,positionList.get(j).getStartDate());
                               writeLocalData(dos,positionList.get(j).getEndDate());


                              dos.writeUTF(positionList.get(j).getTitle() == null ? "" : positionList.get(j).getTitle());
                              dos.writeUTF(positionList.get(j).getDescription() == null ? "" : positionList.get(j).getDescription());
                            }
                        }

                        break;
                }
            }

        }

    }

    private void writeLocalData(DataOutputStream dos, LocalDate localDate) throws IOException{
        dos.writeInt(localDate.getMonth().getValue());
        dos.writeInt(localDate.getYear());

    }
    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }
  /*  private void getInfoFromOrganization(List<Organization> listOrganization) {
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
        infoOrganization.add(linkName);
        infoOrganization.add(linkUrl);
    }

    private void getInfoPosition(List<Organization.Position> positionList) {
        for (Organization.Position position : positionList) {
            String title = position.getTitle();
            String description = position.getDescription();
            LocalDate startDate = position.getStartDate();
            LocalDate endDate = position.getEndDate();
            infoOrganization.add(startDate.toString());
            infoOrganization.add(endDate.toString());
            infoOrganization.add(title);
            infoOrganization.add(description);
        }
    }*/




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
                    Link link = new Link(dis.readUTF(), dis.readUTF());
                    Organization.Position position = new Organization.Position(
                            readLocalDate(dis),
                            readLocalDate(dis),
                           // LocalDate.parse(dis.readUTF()),
                          //  LocalDate.parse(dis.readUTF()),
                            dis.readUTF(),
                            dis.readUTF()
                    );
                    int positionListSize = dis.readInt();

                    List<Organization.Position> positionList = new ArrayList<>();
                    for (int j = 0; j < positionListSize; j++) {
                        positionList.add(position);
                    }

                    for (int j = 0; j < organizationListSize; j++) {
                        organizationList.add(new Organization(link, positionList));
                    }
                    section = new OrganizationSection(organizationList);


                    /*List<Organization> organizationList = new ArrayList<>();
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
                    section = new OrganizationSection(organizationList);*/
                }
                resume.addSection(type, section);
            }
            return resume;
        }
    }
}