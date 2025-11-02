package student.model;

import shared.enums.EnrollmentStatus;
import java.util.Date;

public class Enrollment {
    private int id;
    private String studentFileNumber;
    private int courseId;
    private EnrollmentStatus status;
    private Date enrollmentDate;

    public Enrollment() {
    }

    public Enrollment(int id, String studentFileNumber, int courseId, EnrollmentStatus status, Date enrollmentDate) {
        this.id = id;
        this.studentFileNumber = studentFileNumber;
        this.courseId = courseId;
        this.status = status;
        this.enrollmentDate = enrollmentDate;
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

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}

