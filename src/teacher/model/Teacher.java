package teacher.model;

import shared.model.User;
import shared.enums.UserType;

public class Teacher extends User {
    public Teacher() {
        super();
        this.userType = UserType.TEACHER;
    }

    public Teacher(String fileNumber, String firstName, String lastName, String email, String password) {
        super(fileNumber, firstName, lastName, email, password, UserType.TEACHER);
    }
}

