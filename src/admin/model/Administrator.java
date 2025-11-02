package admin.model;

import shared.model.User;
import shared.enums.UserType;

public class Administrator extends User {
    public Administrator() {
        super();
        this.userType = UserType.ADMINISTRATOR;
    }

    public Administrator(String fileNumber, String firstName, String lastName, String email, String password) {
        super(fileNumber, firstName, lastName, email, password, UserType.ADMINISTRATOR);
    }
}

