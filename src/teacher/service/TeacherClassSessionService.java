package teacher.service;

import admin.dao.AdminCourseDAO;
import admin.dao.AdminClassroomDAO;
import teacher.dao.TeacherClassSessionDAO;
import teacher.model.ClassSession;
import shared.model.Course;
import shared.model.Classroom;

import java.util.Date;
import java.util.List;

public class TeacherClassSessionService {
    private TeacherClassSessionDAO classSessionDAO;
    private AdminCourseDAO courseDAO;
    private AdminClassroomDAO classroomDAO;

    public TeacherClassSessionService() {
        this.classSessionDAO = new TeacherClassSessionDAO();
        this.courseDAO = new AdminCourseDAO();
        this.classroomDAO = new AdminClassroomDAO();
    }

    public boolean createClassSession(int courseId, Date date, String startTime, String endTime, int classroomId) {
        // Validate course exists
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            return false;
        }

        // Validate classroom exists
        Classroom classroom = classroomDAO.findById(classroomId);
        if (classroom == null) {
            return false;
        }

        int id = classSessionDAO.getNextId();
        ClassSession session = new ClassSession(id, courseId, date, startTime, endTime, classroomId);
        classSessionDAO.save(session);
        return true;
    }

    public boolean updateClassSession(ClassSession session) {
        classSessionDAO.update(session);
        return true;
    }

    public boolean deleteClassSession(int id) {
        ClassSession session = classSessionDAO.findById(id);
        if (session != null) {
            classSessionDAO.delete(session);
            return true;
        }
        return false;
    }

    public List<ClassSession> getAllClassSessions() {
        return classSessionDAO.loadAll();
    }

    public List<ClassSession> getClassSessionsByCourse(int courseId) {
        return classSessionDAO.findByCourseId(courseId);
    }

    public ClassSession getClassSessionById(int id) {
        return classSessionDAO.findById(id);
    }
}

