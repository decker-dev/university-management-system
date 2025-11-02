package student.model;

import shared.model.User;
import shared.enums.UserType;

public class Student extends User {
    private int programId;

    public Student() {
        super();
        this.userType = UserType.STUDENT;
    }

    public Student(String fileNumber, String firstName, String lastName, String email, String password, int programId) {
        super(fileNumber, firstName, lastName, email, password, UserType.STUDENT);
        this.programId = programId;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }
}

