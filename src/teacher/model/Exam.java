package teacher.model;

import shared.enums.ExamType;

import java.util.Date;

public class Exam {
    private int id;
    private Date date;
    private int courseId;
    private String teacherFileNumber;
    private ExamType type;
    private int questionCount;

    public Exam() {
    }

    public Exam(int id, Date date, int courseId, String teacherFileNumber, ExamType type, int questionCount) {
        this.id = id;
        this.date = date;
        this.courseId = courseId;
        this.teacherFileNumber = teacherFileNumber;
        this.type = type;
        this.questionCount = questionCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTeacherFileNumber() {
        return teacherFileNumber;
    }

    public void setTeacherFileNumber(String teacherFileNumber) {
        this.teacherFileNumber = teacherFileNumber;
    }

    public ExamType getType() {
        return type;
    }

    public void setType(ExamType type) {
        this.type = type;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}

