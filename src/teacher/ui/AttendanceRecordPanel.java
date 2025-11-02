package teacher.ui;

import teacher.service.TeacherClassSessionService;
import teacher.service.TeacherAttendanceService;
import teacher.model.ClassSession;
import student.dao.StudentEnrollmentDAO;
import student.model.Enrollment;
import admin.dao.AdminCourseDAO;
import admin.dao.AdminUserDAO;
import shared.model.Course;
import shared.model.User;
import shared.util.SessionManager;
import shared.util.DateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AttendanceRecordPanel extends JPanel {
    private TeacherClassSessionService sessionService;
    private TeacherAttendanceService attendanceService;
    private AdminCourseDAO courseDAO;
    private StudentEnrollmentDAO enrollmentDAO;
    private AdminUserDAO userDAO;
    private JComboBox<ClassSession> sessionCombo;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;

    public AttendanceRecordPanel() {
        this.sessionService = new TeacherClassSessionService();
        this.attendanceService = new TeacherAttendanceService();
        this.courseDAO = new AdminCourseDAO();
        this.enrollmentDAO = new StudentEnrollmentDAO();
        this.userDAO = new AdminUserDAO();
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Clase:"));
        sessionCombo = new JComboBox<>();
        sessionCombo.addActionListener(e -> loadStudents());
        topPanel.add(sessionCombo);

        JButton loadButton = new JButton("Cargar Clases");
        loadButton.addActionListener(e -> loadClassSessions());
        topPanel.add(loadButton);

        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Legajo", "Nombre", "Presente"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 2 ? Boolean.class : String.class;
            }
        };
        attendanceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton saveButton = new JButton("Guardar Asistencias");
        saveButton.addActionListener(e -> saveAttendances());
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadClassSessions();
    }

    private void loadClassSessions() {
        sessionCombo.removeAllItems();
        String teacherFileNumber = SessionManager.getInstance().getCurrentUser().getFileNumber();
        List<ClassSession> sessions = sessionService.getAllClassSessions();

        for (ClassSession session : sessions) {
            Course course = courseDAO.findById(session.getCourseId());
            if (course != null && teacherFileNumber.equals(course.getTeacherFileNumber())) {
                sessionCombo.addItem(session);
            }
        }
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        ClassSession session = (ClassSession) sessionCombo.getSelectedItem();
        if (session == null) return;

        List<Enrollment> enrollments = enrollmentDAO.findByCourseId(session.getCourseId());
        for (Enrollment enrollment : enrollments) {
            User student = userDAO.findByFileNumber(enrollment.getStudentFileNumber());
            if (student != null) {
                var attendance = attendanceService.getAttendanceByStudentAndSession(
                    enrollment.getStudentFileNumber(), session.getId());
                boolean present = attendance != null && attendance.isPresent();
                
                Object[] row = {
                    student.getFileNumber(),
                    student.getFullName(),
                    present
                };
                tableModel.addRow(row);
            }
        }
    }

    private void saveAttendances() {
        ClassSession session = (ClassSession) sessionCombo.getSelectedItem();
        if (session == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una clase");
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String studentFileNumber = (String) tableModel.getValueAt(i, 0);
            boolean present = (boolean) tableModel.getValueAt(i, 2);
            attendanceService.recordAttendance(studentFileNumber, session.getId(), present);
        }

        JOptionPane.showMessageDialog(this, "Asistencias guardadas exitosamente");
    }
}

