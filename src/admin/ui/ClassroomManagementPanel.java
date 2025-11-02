package admin.ui;

import admin.service.AdminClassroomService;
import shared.model.Classroom;
import shared.enums.ClassroomType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClassroomManagementPanel extends JPanel {
    private AdminClassroomService classroomService;
    private JTable classroomTable;
    private DefaultTableModel tableModel;

    public ClassroomManagementPanel() {
        this.classroomService = new AdminClassroomService();
        setLayout(new BorderLayout());
        initComponents();
        loadClassrooms();
    }

    private void initComponents() {
        String[] columnNames = {"ID", "Tipo", "Número", "Piso", "Capacidad", "Sede"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        classroomTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(classroomTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Agregar Aula");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Eliminar");
        JButton refreshButton = new JButton("Actualizar");

        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteClassroom());
        refreshButton.addActionListener(e -> loadClassrooms());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadClassrooms() {
        tableModel.setRowCount(0);
        List<Classroom> classrooms = classroomService.getAllClassrooms();
        for (Classroom classroom : classrooms) {
            Object[] row = {
                classroom.getId(),
                classroom.getType().name(),
                classroom.getNumber(),
                classroom.getFloor(),
                classroom.getCapacity(),
                classroom.getLocation()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Agregar Aula", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<ClassroomType> typeCombo = new JComboBox<>(ClassroomType.values());
        JSpinner numberSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        JSpinner floorSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 20, 1));
        JSpinner capacitySpinner = new JSpinner(new SpinnerNumberModel(30, 1, 1000, 1));
        JTextField locationField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; dialog.add(typeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Número:"), gbc);
        gbc.gridx = 1; dialog.add(numberSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2; dialog.add(new JLabel("Piso:"), gbc);
        gbc.gridx = 1; dialog.add(floorSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3; dialog.add(new JLabel("Capacidad:"), gbc);
        gbc.gridx = 1; dialog.add(capacitySpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 4; dialog.add(new JLabel("Sede:"), gbc);
        gbc.gridx = 1; dialog.add(locationField, gbc);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");

        saveBtn.addActionListener(e -> {
            ClassroomType type = (ClassroomType) typeCombo.getSelectedItem();
            int number = (int) numberSpinner.getValue();
            int floor = (int) floorSpinner.getValue();
            int capacity = (int) capacitySpinner.getValue();
            String location = locationField.getText().trim();

            if (classroomService.createClassroom(type, number, floor, capacity, location)) {
                JOptionPane.showMessageDialog(dialog, "Aula creada exitosamente");
                loadClassrooms();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al crear aula", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        dialog.add(btnPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = classroomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un aula para editar");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Classroom classroom = classroomService.getClassroomById(id);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Aula", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<ClassroomType> typeCombo = new JComboBox<>(ClassroomType.values());
        typeCombo.setSelectedItem(classroom.getType());
        JSpinner numberSpinner = new JSpinner(new SpinnerNumberModel(classroom.getNumber(), 1, 999, 1));
        JSpinner floorSpinner = new JSpinner(new SpinnerNumberModel(classroom.getFloor(), 0, 20, 1));
        JSpinner capacitySpinner = new JSpinner(new SpinnerNumberModel(classroom.getCapacity(), 1, 1000, 1));
        JTextField locationField = new JTextField(classroom.getLocation(), 20);

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; dialog.add(typeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Número:"), gbc);
        gbc.gridx = 1; dialog.add(numberSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2; dialog.add(new JLabel("Piso:"), gbc);
        gbc.gridx = 1; dialog.add(floorSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3; dialog.add(new JLabel("Capacidad:"), gbc);
        gbc.gridx = 1; dialog.add(capacitySpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 4; dialog.add(new JLabel("Sede:"), gbc);
        gbc.gridx = 1; dialog.add(locationField, gbc);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");

        saveBtn.addActionListener(e -> {
            classroom.setType((ClassroomType) typeCombo.getSelectedItem());
            classroom.setNumber((int) numberSpinner.getValue());
            classroom.setFloor((int) floorSpinner.getValue());
            classroom.setCapacity((int) capacitySpinner.getValue());
            classroom.setLocation(locationField.getText().trim());

            if (classroomService.updateClassroom(classroom)) {
                JOptionPane.showMessageDialog(dialog, "Aula actualizada exitosamente");
                loadClassrooms();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al actualizar aula", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        dialog.add(btnPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteClassroom() {
        int selectedRow = classroomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un aula para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar esta aula?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            if (classroomService.deleteClassroom(id)) {
                JOptionPane.showMessageDialog(this, "Aula eliminada exitosamente");
                loadClassrooms();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar aula", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

