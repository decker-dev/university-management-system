package teacher.ui;

import teacher.service.TeacherExamService;
import teacher.model.Exam;
import teacher.model.ExamGrade;
import student.dao.StudentEnrollmentDAO;
import student.model.Enrollment;
import admin.dao.AdminUserDAO;
import shared.model.User;
import shared.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GradeRecordPanel extends JPanel {
    private TeacherExamService examService;
    private StudentEnrollmentDAO enrollmentDAO;
    private AdminUserDAO userDAO;
    private JComboBox<Exam> examCombo;
    private JTable gradeTable;
    private DefaultTableModel tableModel;

    public GradeRecordPanel() {
        this.examService = new TeacherExamService();
        this.enrollmentDAO = new StudentEnrollmentDAO();
        this.userDAO = new AdminUserDAO();
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Examen:"));
        examCombo = new JComboBox<>();
        examCombo.addActionListener(e -> loadStudents());
        topPanel.add(examCombo);

        JButton loadButton = new JButton("Cargar Exámenes");
        loadButton.addActionListener(e -> loadExams());
        topPanel.add(loadButton);

        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Legajo", "Nombre", "Nota"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        gradeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gradeTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton saveButton = new JButton("Guardar Notas");
        saveButton.addActionListener(e -> saveGrades());
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadExams();
    }

    private void loadExams() {
        examCombo.removeAllItems();
        String teacherFileNumber = SessionManager.getInstance().getCurrentUser().getFileNumber();
        List<Exam> exams = examService.getExamsByTeacher(teacherFileNumber);

        for (Exam exam : exams) {
            examCombo.addItem(exam);
        }
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        Exam exam = (Exam) examCombo.getSelectedItem();
        if (exam == null) return;

        List<Enrollment> enrollments = enrollmentDAO.findByCourseId(exam.getCourseId());
        for (Enrollment enrollment : enrollments) {
            User student = userDAO.findByFileNumber(enrollment.getStudentFileNumber());
            if (student != null) {
                ExamGrade grade = examService.getGradeByExamAndStudent(
                    exam.getId(), enrollment.getStudentFileNumber());
                double gradeValue = grade != null ? grade.getGrade() : 0.0;
                
                Object[] row = {
                    student.getFileNumber(),
                    student.getFullName(),
                    gradeValue
                };
                tableModel.addRow(row);
            }
        }
    }

    private void saveGrades() {
        Exam exam = (Exam) examCombo.getSelectedItem();
        if (exam == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un examen");
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String studentFileNumber = (String) tableModel.getValueAt(i, 0);
            try {
                double grade = Double.parseDouble(tableModel.getValueAt(i, 2).toString());
                examService.recordGrade(exam.getId(), studentFileNumber, grade);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Nota inválida en fila " + (i+1));
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Notas guardadas exitosamente");
    }
}

