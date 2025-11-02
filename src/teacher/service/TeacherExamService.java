package teacher.service;

import admin.dao.AdminCourseDAO;
import teacher.dao.TeacherExamDAO;
import teacher.dao.TeacherExamGradeDAO;
import teacher.model.Exam;
import teacher.model.ExamGrade;
import shared.model.Course;
import shared.enums.ExamType;

import java.util.Date;
import java.util.List;

public class TeacherExamService {
    private TeacherExamDAO examDAO;
    private TeacherExamGradeDAO gradeDAO;
    private AdminCourseDAO courseDAO;

    public TeacherExamService() {
        this.examDAO = new TeacherExamDAO();
        this.gradeDAO = new TeacherExamGradeDAO();
        this.courseDAO = new AdminCourseDAO();
    }

    public boolean createExam(Date date, int courseId, String teacherFileNumber, ExamType type, int questionCount) {
        // Validate course exists
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            return false;
        }

        // Validate teacher is assigned to course
        if (!teacherFileNumber.equals(course.getTeacherFileNumber())) {
            return false;
        }

        int id = examDAO.getNextId();
        Exam exam = new Exam(id, date, courseId, teacherFileNumber, type, questionCount);
        examDAO.save(exam);
        return true;
    }

    public boolean updateExam(Exam exam) {
        examDAO.update(exam);
        return true;
    }

    public boolean deleteExam(int id) {
        Exam exam = examDAO.findById(id);
        if (exam != null) {
            // Delete associated grades
            List<ExamGrade> grades = gradeDAO.findByExamId(id);
            for (ExamGrade grade : grades) {
                gradeDAO.delete(grade);
            }
            examDAO.delete(exam);
            return true;
        }
        return false;
    }

    public boolean recordGrade(int examId, String studentFileNumber, double grade) {
        // Validate exam exists
        Exam exam = examDAO.findById(examId);
        if (exam == null) {
            return false;
        }

        // Check if grade already exists
        ExamGrade existing = gradeDAO.findByExamAndStudent(examId, studentFileNumber);
        if (existing != null) {
            existing.setGrade(grade);
            gradeDAO.update(existing);
        } else {
            int id = gradeDAO.getNextId();
            ExamGrade examGrade = new ExamGrade(id, examId, studentFileNumber, grade);
            gradeDAO.save(examGrade);
        }
        return true;
    }

    public List<Exam> getAllExams() {
        return examDAO.loadAll();
    }

    public List<Exam> getExamsByCourse(int courseId) {
        return examDAO.findByCourseId(courseId);
    }

    public List<Exam> getExamsByTeacher(String teacherFileNumber) {
        return examDAO.findByTeacher(teacherFileNumber);
    }

    public Exam getExamById(int id) {
        return examDAO.findById(id);
    }

    public List<ExamGrade> getGradesByExam(int examId) {
        return gradeDAO.findByExamId(examId);
    }

    public List<ExamGrade> getGradesByStudent(String studentFileNumber) {
        return gradeDAO.findByStudent(studentFileNumber);
    }

    public ExamGrade getGradeByExamAndStudent(int examId, String studentFileNumber) {
        return gradeDAO.findByExamAndStudent(examId, studentFileNumber);
    }
}

