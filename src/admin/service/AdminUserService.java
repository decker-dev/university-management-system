package admin.service;

import admin.dao.AdminUserDAO;
import shared.model.User;
import shared.enums.UserType;
import shared.util.ValidationUtil;

import java.util.List;

public class AdminUserService {
    private AdminUserDAO userDAO;

    public AdminUserService() {
        this.userDAO = new AdminUserDAO();
    }

    public User authenticate(String fileNumber, String password) {
        User user = userDAO.findByFileNumber(fileNumber);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean createUser(User user) {
        if (!ValidationUtil.isValidFileNumber(user.getFileNumber())) {
            return false;
        }
        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            return false;
        }
        if (!ValidationUtil.isValidPassword(user.getPassword())) {
            return false;
        }
        if (userDAO.findByFileNumber(user.getFileNumber()) != null) {
            return false; // User already exists
        }

        userDAO.save(user);
        return true;
    }

    public boolean updateUser(User user) {
        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            return false;
        }
        userDAO.update(user);
        return true;
    }

    public boolean deleteUser(String fileNumber) {
        User user = userDAO.findByFileNumber(fileNumber);
        if (user != null) {
            userDAO.delete(user);
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userDAO.loadAll();
    }

    public User getUserByFileNumber(String fileNumber) {
        return userDAO.findByFileNumber(fileNumber);
    }

    public List<User> getUsersByType(UserType type) {
        return userDAO.loadAll().stream()
            .filter(u -> u.getUserType() == type)
            .collect(java.util.stream.Collectors.toList());
    }
}

