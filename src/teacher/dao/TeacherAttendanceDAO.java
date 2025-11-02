package teacher.dao;

import shared.dao.BaseDAO;
import shared.util.FileUtil;
import teacher.model.Attendance;

public class TeacherAttendanceDAO extends BaseDAO<Attendance> {
    private static final String FILE_PATH = FileUtil.TEACHER_DIR + "/attendances.txt";

    public TeacherAttendanceDAO() {
        super(FILE_PATH);
    }

    @Override
    protected Attendance parseLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 4) return null;

        int id = Integer.parseInt(parts[0]);
        String studentFileNumber = parts[1];
        int classSessionId = Integer.parseInt(parts[2]);
        boolean present = Boolean.parseBoolean(parts[3]);

        return new Attendance(id, studentFileNumber, classSessionId, present);
    }

    @Override
    protected String toLine(Attendance attendance) {
        return attendance.getId() + "|" + attendance.getStudentFileNumber() + "|" + 
               attendance.getClassSessionId() + "|" + attendance.isPresent();
    }

    public int getNextId() {
        return loadAll().stream()
            .mapToInt(Attendance::getId)
            .max()
            .orElse(0) + 1;
    }

    public Attendance findById(int id) {
        return loadAll().stream()
            .filter(a -> a.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public java.util.List<Attendance> findByClassSessionId(int classSessionId) {
        return loadAll().stream()
            .filter(a -> a.getClassSessionId() == classSessionId)
            .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<Attendance> findByStudentFileNumber(String studentFileNumber) {
        return loadAll().stream()
            .filter(a -> a.getStudentFileNumber().equals(studentFileNumber))
            .collect(java.util.stream.Collectors.toList());
    }

    public Attendance findByStudentAndSession(String studentFileNumber, int classSessionId) {
        return loadAll().stream()
            .filter(a -> a.getStudentFileNumber().equals(studentFileNumber) && 
                         a.getClassSessionId() == classSessionId)
            .findFirst()
            .orElse(null);
    }

    public void update(Attendance attendance) {
        java.util.List<Attendance> attendances = loadAll();
        for (int i = 0; i < attendances.size(); i++) {
            if (attendances.get(i).getId() == attendance.getId()) {
                attendances.set(i, attendance);
                saveAll(attendances);
                return;
            }
        }
    }

    public void delete(Attendance attendance) {
        java.util.List<Attendance> attendances = loadAll();
        attendances.removeIf(a -> a.getId() == attendance.getId());
        saveAll(attendances);
    }
}

