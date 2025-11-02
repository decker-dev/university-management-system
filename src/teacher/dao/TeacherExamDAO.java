package teacher.dao;

import shared.dao.BaseDAO;
import shared.util.FileUtil;
import shared.util.DateUtil;
import shared.enums.ExamType;
import teacher.model.Exam;

public class TeacherExamDAO extends BaseDAO<Exam> {
    private static final String FILE_PATH = FileUtil.TEACHER_DIR + "/exams.txt";

    public TeacherExamDAO() {
        super(FILE_PATH);
    }

    @Override
    protected Exam parseLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 6) return null;

        int id = Integer.parseInt(parts[0]);
        java.util.Date date = DateUtil.parseDate(parts[1]);
        int courseId = Integer.parseInt(parts[2]);
        String teacherFileNumber = parts[3];
        ExamType type = ExamType.valueOf(parts[4]);
        int questionCount = Integer.parseInt(parts[5]);

        return new Exam(id, date, courseId, teacherFileNumber, type, questionCount);
    }

    @Override
    protected String toLine(Exam exam) {
        return exam.getId() + "|" + DateUtil.formatDate(exam.getDate()) + "|" + 
               exam.getCourseId() + "|" + exam.getTeacherFileNumber() + "|" + 
               exam.getType().name() + "|" + exam.getQuestionCount();
    }

    public int getNextId() {
        return loadAll().stream()
            .mapToInt(Exam::getId)
            .max()
            .orElse(0) + 1;
    }

    public Exam findById(int id) {
        return loadAll().stream()
            .filter(e -> e.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public java.util.List<Exam> findByCourseId(int courseId) {
        return loadAll().stream()
            .filter(e -> e.getCourseId() == courseId)
            .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<Exam> findByTeacher(String teacherFileNumber) {
        return loadAll().stream()
            .filter(e -> e.getTeacherFileNumber().equals(teacherFileNumber))
            .collect(java.util.stream.Collectors.toList());
    }

    public void update(Exam exam) {
        java.util.List<Exam> exams = loadAll();
        for (int i = 0; i < exams.size(); i++) {
            if (exams.get(i).getId() == exam.getId()) {
                exams.set(i, exam);
                saveAll(exams);
                return;
            }
        }
    }

    public void delete(Exam exam) {
        java.util.List<Exam> exams = loadAll();
        exams.removeIf(e -> e.getId() == exam.getId());
        saveAll(exams);
    }
}

