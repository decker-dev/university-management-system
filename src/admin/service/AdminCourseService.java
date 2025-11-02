package admin.service;

import admin.dao.AdminCourseDAO;
import admin.dao.AdminProgramDAO;
import admin.dao.AdminUserDAO;
import shared.model.Course;
import shared.model.User;
import shared.enums.UserType;
import shared.util.ValidationUtil;

import java.util.List;

public class AdminCourseService {
    private AdminCourseDAO courseDAO;
    private AdminProgramDAO programDAO;
    private AdminUserDAO userDAO;

    public AdminCourseService() {
        this.courseDAO = new AdminCourseDAO();
        this.programDAO = new AdminProgramDAO();
        this.userDAO = new AdminUserDAO();
    }

    public boolean createCourse(String name, String code, int programId) {
        if (!ValidationUtil.isNotEmpty(name) || !ValidationUtil.isNotEmpty(code)) {
            return false;
        }
        if (programDAO.findById(programId) == null) {
            return false; // Program doesn't exist
        }

        int id = courseDAO.getNextId();
        Course course = new Course(id, name, code, programId, null);
        courseDAO.save(course);
        return true;
    }

    public boolean updateCourse(Course course) {
        if (!ValidationUtil.isNotEmpty(course.getName()) || 
            !ValidationUtil.isNotEmpty(course.getCode())) {
            return false;
        }
        courseDAO.update(course);
        return true;
    }

    public boolean deleteCourse(int id) {
        Course course = courseDAO.findById(id);
        if (course != null) {
            courseDAO.delete(course);
            return true;
        }
        return false;
    }

    public boolean assignTeacher(int courseId, String teacherFileNumber) {
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            return false;
        }

        User teacher = userDAO.findByFileNumber(teacherFileNumber);
        if (teacher == null || teacher.getUserType() != UserType.TEACHER) {
            return false;
        }

        course.setTeacherFileNumber(teacherFileNumber);
        courseDAO.update(course);
        return true;
    }

    public List<Course> getAllCourses() {
        return courseDAO.loadAll();
    }

    public Course getCourseById(int id) {
        return courseDAO.findById(id);
    }

    public List<Course> getCoursesByProgram(int programId) {
        return courseDAO.findByProgramId(programId);
    }
}

