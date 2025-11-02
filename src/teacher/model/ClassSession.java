package teacher.model;

import java.util.Date;

public class ClassSession {
    private int id;
    private int courseId;
    private Date date;
    private String startTime;
    private String endTime;
    private int classroomId;

    public ClassSession() {
    }

    public ClassSession(int id, int courseId, Date date, String startTime, String endTime, int classroomId) {
        this.id = id;
        this.courseId = courseId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classroomId = classroomId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }
}

