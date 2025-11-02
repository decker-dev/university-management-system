package shared.model;

import shared.enums.UserType;

public abstract class User {
    protected String fileNumber;  // legajo
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected UserType userType;

    public User() {
    }

    public User(String fileNumber, String firstName, String lastName, String email, String password, UserType userType) {
        this.fileNumber = fileNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

