package student.ui;

import student.service.StudentQueryService;
import teacher.model.ExamGrade;
import teacher.dao.TeacherExamDAO;
import admin.dao.AdminCourseDAO;
import teacher.model.Exam;
import shared.model.Course;
import shared.util.SessionManager;
import shared.util.DateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MyGradesPanel extends JPanel {
    private StudentQueryService queryService;
    private TeacherExamDAO examDAO;
    private AdminCourseDAO courseDAO;
    private JTable gradeTable;
    private DefaultTableModel tableModel;

    public MyGradesPanel() {
        this.queryService = new StudentQueryService();
        this.examDAO = new TeacherExamDAO();
        this.courseDAO = new AdminCourseDAO();
        setLayout(new BorderLayout());
        initComponents();
        loadGrades();
    }

    private void initComponents() {
        String[] columnNames = {"Materia", "Fecha Examen", "Tipo", "Nota"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        gradeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gradeTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("Actualizar");
        refreshButton.addActionListener(e -> loadGrades());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadGrades() {
        tableModel.setRowCount(0);
        String studentFileNumber = SessionManager.getInstance().getCurrentUser().getFileNumber();
        List<ExamGrade> grades = queryService.getGradesByStudent(studentFileNumber);

        for (ExamGrade grade : grades) {
            Exam exam = examDAO.findById(grade.getExamId());
            if (exam != null) {
                Course course = courseDAO.findById(exam.getCourseId());
                Object[] row = {
                    course != null ? course.getName() : "N/A",
                    DateUtil.formatDate(exam.getDate()),
                    exam.getType().name(),
                    grade.getGrade()
                };
                tableModel.addRow(row);
            }
        }
    }
}

