package teacher.dao;

import shared.dao.BaseDAO;
import shared.util.FileUtil;
import shared.util.DateUtil;
import teacher.model.ClassSession;

public class TeacherClassSessionDAO extends BaseDAO<ClassSession> {
    private static final String FILE_PATH = FileUtil.TEACHER_DIR + "/class_sessions.txt";

    public TeacherClassSessionDAO() {
        super(FILE_PATH);
    }

    @Override
    protected ClassSession parseLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 6) return null;

        int id = Integer.parseInt(parts[0]);
        int courseId = Integer.parseInt(parts[1]);
        java.util.Date date = DateUtil.parseDate(parts[2]);
        String startTime = parts[3];
        String endTime = parts[4];
        int classroomId = Integer.parseInt(parts[5]);

        return new ClassSession(id, courseId, date, startTime, endTime, classroomId);
    }

    @Override
    protected String toLine(ClassSession session) {
        return session.getId() + "|" + session.getCourseId() + "|" + 
               DateUtil.formatDate(session.getDate()) + "|" + 
               session.getStartTime() + "|" + session.getEndTime() + "|" + 
               session.getClassroomId();
    }

    public int getNextId() {
        return loadAll().stream()
            .mapToInt(ClassSession::getId)
            .max()
            .orElse(0) + 1;
    }

    public ClassSession findById(int id) {
        return loadAll().stream()
            .filter(s -> s.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public java.util.List<ClassSession> findByCourseId(int courseId) {
        return loadAll().stream()
            .filter(s -> s.getCourseId() == courseId)
            .collect(java.util.stream.Collectors.toList());
    }

    public void update(ClassSession session) {
        java.util.List<ClassSession> sessions = loadAll();
        for (int i = 0; i < sessions.size(); i++) {
            if (sessions.get(i).getId() == session.getId()) {
                sessions.set(i, session);
                saveAll(sessions);
                return;
            }
        }
    }

    public void delete(ClassSession session) {
        java.util.List<ClassSession> sessions = loadAll();
        sessions.removeIf(s -> s.getId() == session.getId());
        saveAll(sessions);
    }
}

