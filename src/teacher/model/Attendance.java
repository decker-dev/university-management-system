package teacher.model;

public class Attendance {
    private int id;
    private String studentFileNumber;
    private int classSessionId;
    private boolean present;

    public Attendance() {
    }

    public Attendance(int id, String studentFileNumber, int classSessionId, boolean present) {
        this.id = id;
        this.studentFileNumber = studentFileNumber;
        this.classSessionId = classSessionId;
        this.present = present;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentFileNumber() {
        return studentFileNumber;
    }

    public void setStudentFileNumber(String studentFileNumber) {
        this.studentFileNumber = studentFileNumber;
    }

    public int getClassSessionId() {
        return classSessionId;
    }

    public void setClassSessionId(int classSessionId) {
        this.classSessionId = classSessionId;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}

