package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlExecutor;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
            Resume resume;
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume WHERE uuid =?")) {
                ps.setString(1, uuid);
                ResultSet resultSet = ps.executeQuery();
                if (!resultSet.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, resultSet.getString("full_name"));
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact WHERE resume_uuid =?")) {
                ps.setString(1, uuid);
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    addContact(resultSet, resume);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section WHERE resume_uuid =?")) {
                ps.setString(1, uuid);
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    addSection(resultSet, resume);
                }
            }
            return resume;
        });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name =? WHERE uuid = ?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                ps.execute();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid=?")) {
                ps.setString(1, resume.getUuid());
                ps.execute();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid=?")) {
                ps.setString(1, resume.getUuid());
                ps.execute();
            }
            insertContactsAndSections(resume, conn);
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, resume.getFullName());
                        ps.execute();
                    }
                    insertContactsAndSections(resume, conn);
                    return null;
                }
        );
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid=?", (SqlExecutor<Resume>) st -> {
            st.setString(1, uuid);
            if (st.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(conn -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    String uuid = resultSet.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, resultSet.getString("full_name")));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    Resume resume = resumes.get(resultSet.getString("resume_uuid"));
                    addContact(resultSet, resume);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")) {
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    Resume resume = resumes.get(resultSet.getString("resume_uuid"));
                    addSection(resultSet, resume);
                }
            }
            return new ArrayList<>(resumes.values());
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", st -> {
            ResultSet resultSet = st.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        });
    }

    private void addContact(ResultSet resultSet, Resume resume) throws SQLException {
        String value = resultSet.getString("value");
        if (value != null) {
            resume.addContact(ContactType.valueOf(resultSet.getString("type")), value);
        }
    }

    private void addSection(ResultSet resultSet, Resume resume) throws SQLException {
        String content = resultSet.getString("content");
        if (content != null) {
            SectionType type = SectionType.valueOf(resultSet.getString("type"));

            switch (type) {
                case PERSONAL:
                case OBJECTIVE:
                    resume.addSection(type, new TextType(content));
                    break;
            }
            switch (type) {
                case ACHIEVEMENTS:
                case QUALIFICATIONS:
                    String[] splits = content.replace("{", "").replace("}", "").split("\\n");
                    resume.addSection(type, new ListOfStrings(splits));
                    break;
            }
        }
    }

    private void insertContactsAndSections(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, content) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : resume.getSections().entrySet()) {
                SectionType type = e.getKey();
                AbstractSection section = e.getValue();
                ps.setString(1, resume.getUuid());
                ps.setString(2, type.name());
                getTypeOfSection(type, ps, section);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void getTypeOfSection(SectionType type, PreparedStatement ps, AbstractSection section) throws SQLException {

        switch (type) {
            case PERSONAL:
            case OBJECTIVE:
                String content = ((TextType) section).getContent();
                ps.setString(3, content);
                break;
        }
        switch (type) {
            case ACHIEVEMENTS:
            case QUALIFICATIONS:
                List<String> list = ((ListOfStrings) section).getList();
                String stringBuilder = list.stream().map(s -> s + "\n").collect(Collectors.joining());
                ps.setString(3, stringBuilder);
                break;
        }
    }
}
