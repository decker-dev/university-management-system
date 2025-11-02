package teacher.dao;

import shared.dao.BaseDAO;
import shared.util.FileUtil;
import teacher.model.ExamGrade;

public class TeacherExamGradeDAO extends BaseDAO<ExamGrade> {
    private static final String FILE_PATH = FileUtil.TEACHER_DIR + "/exam_grades.txt";

    public TeacherExamGradeDAO() {
        super(FILE_PATH);
    }

    @Override
    protected ExamGrade parseLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 4) return null;

        int id = Integer.parseInt(parts[0]);
        int examId = Integer.parseInt(parts[1]);
        String studentFileNumber = parts[2];
        double grade = Double.parseDouble(parts[3]);

        return new ExamGrade(id, examId, studentFileNumber, grade);
    }

    @Override
    protected String toLine(ExamGrade grade) {
        return grade.getId() + "|" + grade.getExamId() + "|" + 
               grade.getStudentFileNumber() + "|" + grade.getGrade();
    }

    public int getNextId() {
        return loadAll().stream()
            .mapToInt(ExamGrade::getId)
            .max()
            .orElse(0) + 1;
    }

    public ExamGrade findById(int id) {
        return loadAll().stream()
            .filter(g -> g.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public java.util.List<ExamGrade> findByExamId(int examId) {
        return loadAll().stream()
            .filter(g -> g.getExamId() == examId)
            .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<ExamGrade> findByStudent(String studentFileNumber) {
        return loadAll().stream()
            .filter(g -> g.getStudentFileNumber().equals(studentFileNumber))
            .collect(java.util.stream.Collectors.toList());
    }

    public ExamGrade findByExamAndStudent(int examId, String studentFileNumber) {
        return loadAll().stream()
            .filter(g -> g.getExamId() == examId && 
                         g.getStudentFileNumber().equals(studentFileNumber))
            .findFirst()
            .orElse(null);
    }

    public void update(ExamGrade grade) {
        java.util.List<ExamGrade> grades = loadAll();
        for (int i = 0; i < grades.size(); i++) {
            if (grades.get(i).getId() == grade.getId()) {
                grades.set(i, grade);
                saveAll(grades);
                return;
            }
        }
    }

    public void delete(ExamGrade grade) {
        java.util.List<ExamGrade> grades = loadAll();
        grades.removeIf(g -> g.getId() == grade.getId());
        saveAll(grades);
    }
}

