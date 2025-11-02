package student.service;

import admin.dao.AdminCourseDAO;
import student.dao.StudentEnrollmentDAO;
import teacher.dao.TeacherExamGradeDAO;
import teacher.dao.TeacherAttendanceDAO;
import teacher.model.ExamGrade;
import teacher.model.Attendance;
import shared.model.Course;
import student.model.Enrollment;

import java.util.List;

public class StudentQueryService {
    private AdminCourseDAO courseDAO;
    private StudentEnrollmentDAO enrollmentDAO;
    private TeacherExamGradeDAO gradeDAO;
    private TeacherAttendanceDAO attendanceDAO;

    public StudentQueryService() {
        this.courseDAO = new AdminCourseDAO();
        this.enrollmentDAO = new StudentEnrollmentDAO();
        this.gradeDAO = new TeacherExamGradeDAO();
        this.attendanceDAO = new TeacherAttendanceDAO();
    }

    public List<Course> getEnrolledCourses(String studentFileNumber) {
        List<Enrollment> enrollments = enrollmentDAO.findByStudentFileNumber(studentFileNumber);
        List<Course> courses = new java.util.ArrayList<>();
        
        for (Enrollment enrollment : enrollments) {
            Course course = courseDAO.findById(enrollment.getCourseId());
            if (course != null) {
                courses.add(course);
            }
        }
        
        return courses;
    }

    public List<ExamGrade> getGradesByStudent(String studentFileNumber) {
        return gradeDAO.findByStudent(studentFileNumber);
    }

    public List<Attendance> getAttendancesByStudent(String studentFileNumber) {
        return attendanceDAO.findByStudentFileNumber(studentFileNumber);
    }
}

