package teacher.model;

public class ExamGrade {
    private int id;
    private int examId;
    private String studentFileNumber;
    private double grade;

    public ExamGrade() {
    }

    public ExamGrade(int id, int examId, String studentFileNumber, double grade) {
        this.id = id;
        this.examId = examId;
        this.studentFileNumber = studentFileNumber;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getStudentFileNumber() {
        return studentFileNumber;
    }

    public void setStudentFileNumber(String studentFileNumber) {
        this.studentFileNumber = studentFileNumber;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}

