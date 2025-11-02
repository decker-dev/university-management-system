package admin.dao;

import shared.dao.BaseDAO;
import shared.model.Course;
import shared.util.FileUtil;

public class AdminCourseDAO extends BaseDAO<Course> {
    private static final String FILE_PATH = FileUtil.ADMIN_DIR + "/courses.txt";

    public AdminCourseDAO() {
        super(FILE_PATH);
    }

    @Override
    protected Course parseLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 4) return null;

        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        String code = parts[2];
        int programId = Integer.parseInt(parts[3]);
        String teacherFileNumber = parts.length > 4 ? parts[4] : null;

        return new Course(id, name, code, programId, teacherFileNumber);
    }

    @Override
    protected String toLine(Course course) {
        String teacher = course.getTeacherFileNumber() != null ? course.getTeacherFileNumber() : "";
        return course.getId() + "|" + course.getName() + "|" + course.getCode() + "|" + 
               course.getProgramId() + "|" + teacher;
    }

    public int getNextId() {
        return loadAll().stream()
            .mapToInt(Course::getId)
            .max()
            .orElse(0) + 1;
    }

    public Course findById(int id) {
        return loadAll().stream()
            .filter(c -> c.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public java.util.List<Course> findByProgramId(int programId) {
        return loadAll().stream()
            .filter(c -> c.getProgramId() == programId)
            .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<Course> findByTeacher(String teacherFileNumber) {
        return loadAll().stream()
            .filter(c -> teacherFileNumber.equals(c.getTeacherFileNumber()))
            .collect(java.util.stream.Collectors.toList());
    }

    public void update(Course course) {
        java.util.List<Course> courses = loadAll();
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getId() == course.getId()) {
                courses.set(i, course);
                saveAll(courses);
                return;
            }
        }
    }

    public void delete(Course course) {
        java.util.List<Course> courses = loadAll();
        courses.removeIf(c -> c.getId() == course.getId());
        saveAll(courses);
    }
}

