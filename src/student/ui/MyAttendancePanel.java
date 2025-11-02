package student.ui;

import student.service.StudentQueryService;
import teacher.model.Attendance;
import teacher.dao.TeacherClassSessionDAO;
import admin.dao.AdminCourseDAO;
import teacher.model.ClassSession;
import shared.model.Course;
import shared.util.SessionManager;
import shared.util.DateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MyAttendancePanel extends JPanel {
    private StudentQueryService queryService;
    private TeacherClassSessionDAO sessionDAO;
    private AdminCourseDAO courseDAO;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;

    public MyAttendancePanel() {
        this.queryService = new StudentQueryService();
        this.sessionDAO = new TeacherClassSessionDAO();
        this.courseDAO = new AdminCourseDAO();
        setLayout(new BorderLayout());
        initComponents();
        loadAttendances();
    }

    private void initComponents() {
        String[] columnNames = {"Materia", "Fecha Clase", "Hora", "Presente"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        attendanceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("Actualizar");
        refreshButton.addActionListener(e -> loadAttendances());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadAttendances() {
        tableModel.setRowCount(0);
        String studentFileNumber = SessionManager.getInstance().getCurrentUser().getFileNumber();
        List<Attendance> attendances = queryService.getAttendancesByStudent(studentFileNumber);

        for (Attendance attendance : attendances) {
            ClassSession session = sessionDAO.findById(attendance.getClassSessionId());
            if (session != null) {
                Course course = courseDAO.findById(session.getCourseId());
                Object[] row = {
                    course != null ? course.getName() : "N/A",
                    DateUtil.formatDate(session.getDate()),
                    session.getStartTime() + " - " + session.getEndTime(),
                    attendance.isPresent() ? "Sí" : "No"
                };
                tableModel.addRow(row);
            }
        }
    }
}

