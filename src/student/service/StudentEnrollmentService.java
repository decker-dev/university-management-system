package student.service;

import admin.dao.AdminCourseDAO;
import student.dao.StudentEnrollmentDAO;
import student.model.Enrollment;
import student.model.Student;
import shared.model.Course;
import shared.enums.EnrollmentStatus;

import java.util.List;
import java.util.Date;

public class StudentEnrollmentService {
    private StudentEnrollmentDAO enrollmentDAO;
    private AdminCourseDAO courseDAO;

    public StudentEnrollmentService() {
        this.enrollmentDAO = new StudentEnrollmentDAO();
        this.courseDAO = new AdminCourseDAO();
    }

    public boolean enrollStudent(Student student, int courseId) {
        // Validate course exists
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            return false;
        }

        // Validate course belongs to student's program
        if (course.getProgramId() != student.getProgramId()) {
            return false;
        }

        // Check if already enrolled
        Enrollment existing = enrollmentDAO.findByStudentAndCourse(student.getFileNumber(), courseId);
        if (existing != null) {
            return false; // Already enrolled
        }

        int id = enrollmentDAO.getNextId();
        Enrollment enrollment = new Enrollment(id, student.getFileNumber(), courseId, 
                                               EnrollmentStatus.ACTIVE, new Date());
        enrollmentDAO.save(enrollment);
        return true;
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentFileNumber) {
        return enrollmentDAO.findByStudentFileNumber(studentFileNumber);
    }

    public List<Enrollment> getEnrollmentsByCourse(int courseId) {
        return enrollmentDAO.findByCourseId(courseId);
    }

    public List<Course> getAvailableCoursesForStudent(Student student) {
        List<Course> allCourses = courseDAO.findByProgramId(student.getProgramId());
        List<Enrollment> studentEnrollments = enrollmentDAO.findByStudentFileNumber(student.getFileNumber());
        
        // Filter out courses already enrolled
        allCourses.removeIf(course -> 
            studentEnrollments.stream().anyMatch(e -> e.getCourseId() == course.getId())
        );
        
        return allCourses;
    }
}

