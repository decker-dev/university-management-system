package student.dao;

import shared.dao.BaseDAO;
import shared.util.FileUtil;
import shared.util.DateUtil;
import shared.enums.EnrollmentStatus;
import student.model.Enrollment;

public class StudentEnrollmentDAO extends BaseDAO<Enrollment> {
    private static final String FILE_PATH = FileUtil.STUDENT_DIR + "/enrollments.txt";

    public StudentEnrollmentDAO() {
        super(FILE_PATH);
    }

    @Override
    protected Enrollment parseLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 5) return null;

        int id = Integer.parseInt(parts[0]);
        String studentFileNumber = parts[1];
        int courseId = Integer.parseInt(parts[2]);
        EnrollmentStatus status = EnrollmentStatus.valueOf(parts[3]);
        java.util.Date enrollmentDate = DateUtil.parseDate(parts[4]);

        return new Enrollment(id, studentFileNumber, courseId, status, enrollmentDate);
    }

    @Override
    protected String toLine(Enrollment enrollment) {
        return enrollment.getId() + "|" + enrollment.getStudentFileNumber() + "|" + 
               enrollment.getCourseId() + "|" + enrollment.getStatus().name() + "|" + 
               DateUtil.formatDate(enrollment.getEnrollmentDate());
    }

    public int getNextId() {
        return loadAll().stream()
            .mapToInt(Enrollment::getId)
            .max()
            .orElse(0) + 1;
    }

    public Enrollment findById(int id) {
        return loadAll().stream()
            .filter(e -> e.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public java.util.List<Enrollment> findByStudentFileNumber(String studentFileNumber) {
        return loadAll().stream()
            .filter(e -> e.getStudentFileNumber().equals(studentFileNumber))
            .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<Enrollment> findByCourseId(int courseId) {
        return loadAll().stream()
            .filter(e -> e.getCourseId() == courseId)
            .collect(java.util.stream.Collectors.toList());
    }

    public Enrollment findByStudentAndCourse(String studentFileNumber, int courseId) {
        return loadAll().stream()
            .filter(e -> e.getStudentFileNumber().equals(studentFileNumber) && 
                         e.getCourseId() == courseId)
            .findFirst()
            .orElse(null);
    }

    public void update(Enrollment enrollment) {
        java.util.List<Enrollment> enrollments = loadAll();
        for (int i = 0; i < enrollments.size(); i++) {
            if (enrollments.get(i).getId() == enrollment.getId()) {
                enrollments.set(i, enrollment);
                saveAll(enrollments);
                return;
            }
        }
    }

    public void delete(Enrollment enrollment) {
        java.util.List<Enrollment> enrollments = loadAll();
        enrollments.removeIf(e -> e.getId() == enrollment.getId());
        saveAll(enrollments);
    }
}

