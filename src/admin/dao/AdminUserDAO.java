package admin.dao;

import shared.dao.BaseDAO;
import shared.model.User;
import shared.enums.UserType;
import shared.util.FileUtil;
import admin.model.Administrator;

public class AdminUserDAO extends BaseDAO<User> {
    private static final String FILE_PATH = FileUtil.ADMIN_DIR + "/users.txt";

    public AdminUserDAO() {
        super(FILE_PATH);
    }

    @Override
    protected User parseLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 6) return null;

        String fileNumber = parts[0];
        String firstName = parts[1];
        String lastName = parts[2];
        String email = parts[3];
        String password = parts[4];
        UserType type = UserType.valueOf(parts[5]);

        switch (type) {
            case ADMINISTRATOR:
                return new Administrator(fileNumber, firstName, lastName, email, password);
            case TEACHER:
                return new teacher.model.Teacher(fileNumber, firstName, lastName, email, password);
            case STUDENT:
                int programId = parts.length > 6 ? Integer.parseInt(parts[6]) : 0;
                return new student.model.Student(fileNumber, firstName, lastName, email, password, programId);
            default:
                return null;
        }
    }

    @Override
    protected String toLine(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getFileNumber()).append("|");
        sb.append(user.getFirstName()).append("|");
        sb.append(user.getLastName()).append("|");
        sb.append(user.getEmail()).append("|");
        sb.append(user.getPassword()).append("|");
        sb.append(user.getUserType().name());
        
        if (user instanceof student.model.Student) {
            sb.append("|").append(((student.model.Student) user).getProgramId());
        }
        
        return sb.toString();
    }

    public User findByFileNumber(String fileNumber) {
        return loadAll().stream()
            .filter(u -> u.getFileNumber().equals(fileNumber))
            .findFirst()
            .orElse(null);
    }

    public void update(User user) {
        java.util.List<User> users = loadAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getFileNumber().equals(user.getFileNumber())) {
                users.set(i, user);
                saveAll(users);
                return;
            }
        }
    }

    public void delete(User user) {
        java.util.List<User> users = loadAll();
        users.removeIf(u -> u.getFileNumber().equals(user.getFileNumber()));
        saveAll(users);
    }
}

