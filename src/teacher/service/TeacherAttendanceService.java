package teacher.service;

import teacher.dao.TeacherAttendanceDAO;
import teacher.dao.TeacherClassSessionDAO;
import teacher.model.Attendance;
import teacher.model.ClassSession;

import java.util.List;

public class TeacherAttendanceService {
    private TeacherAttendanceDAO attendanceDAO;
    private TeacherClassSessionDAO classSessionDAO;

    public TeacherAttendanceService() {
        this.attendanceDAO = new TeacherAttendanceDAO();
        this.classSessionDAO = new TeacherClassSessionDAO();
    }

    public boolean recordAttendance(String studentFileNumber, int classSessionId, boolean present) {
        // Validate class session exists
        ClassSession session = classSessionDAO.findById(classSessionId);
        if (session == null) {
            return false;
        }

        // Check if attendance already exists
        Attendance existing = attendanceDAO.findByStudentAndSession(studentFileNumber, classSessionId);
        if (existing != null) {
            existing.setPresent(present);
            attendanceDAO.update(existing);
        } else {
            int id = attendanceDAO.getNextId();
            Attendance attendance = new Attendance(id, studentFileNumber, classSessionId, present);
            attendanceDAO.save(attendance);
        }
        return true;
    }

    public List<Attendance> getAttendanceByClassSession(int classSessionId) {
        return attendanceDAO.findByClassSessionId(classSessionId);
    }

    public List<Attendance> getAttendanceByStudent(String studentFileNumber) {
        return attendanceDAO.findByStudentFileNumber(studentFileNumber);
    }

    public Attendance getAttendanceByStudentAndSession(String studentFileNumber, int classSessionId) {
        return attendanceDAO.findByStudentAndSession(studentFileNumber, classSessionId);
    }
}

