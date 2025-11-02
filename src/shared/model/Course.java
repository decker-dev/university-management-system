package shared.model;

public class Course {
    private int id;
    private String name;
    private String code;
    private int programId;
    private String teacherFileNumber;

    public Course() {
    }

    public Course(int id, String name, String code, int programId, String teacherFileNumber) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.programId = programId;
        this.teacherFileNumber = teacherFileNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getTeacherFileNumber() {
        return teacherFileNumber;
    }

    public void setTeacherFileNumber(String teacherFileNumber) {
        this.teacherFileNumber = teacherFileNumber;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}

