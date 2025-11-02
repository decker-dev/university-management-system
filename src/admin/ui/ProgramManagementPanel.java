package admin.ui;

import admin.service.AdminProgramService;
import shared.model.Program;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProgramManagementPanel extends JPanel {
    private AdminProgramService programService;
    private JTable programTable;
    private DefaultTableModel tableModel;

    public ProgramManagementPanel() {
        this.programService = new AdminProgramService();
        setLayout(new BorderLayout());
        initComponents();
        loadPrograms();
    }

    private void initComponents() {
        String[] columnNames = {"ID", "Nombre", "Código"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        programTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(programTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Agregar Carrera");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Eliminar");
        JButton refreshButton = new JButton("Actualizar");

        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteProgram());
        refreshButton.addActionListener(e -> loadPrograms());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadPrograms() {
        tableModel.setRowCount(0);
        List<Program> programs = programService.getAllPrograms();
        for (Program program : programs) {
            Object[] row = {program.getId(), program.getName(), program.getCode()};
            tableModel.addRow(row);
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Agregar Carrera", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        JTextField codeField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; dialog.add(codeField, gbc);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");

        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String code = codeField.getText().trim();

            if (programService.createProgram(name, code)) {
                JOptionPane.showMessageDialog(dialog, "Carrera creada exitosamente");
                loadPrograms();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al crear carrera", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        dialog.add(btnPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una carrera para editar");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Program program = programService.getProgramById(id);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Carrera", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(program.getName(), 20);
        JTextField codeField = new JTextField(program.getCode(), 20);

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; dialog.add(codeField, gbc);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");

        saveBtn.addActionListener(e -> {
            program.setName(nameField.getText().trim());
            program.setCode(codeField.getText().trim());

            if (programService.updateProgram(program)) {
                JOptionPane.showMessageDialog(dialog, "Carrera actualizada exitosamente");
                loadPrograms();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al actualizar carrera", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        dialog.add(btnPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteProgram() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una carrera para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar esta carrera?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            if (programService.deleteProgram(id)) {
                JOptionPane.showMessageDialog(this, "Carrera eliminada exitosamente");
                loadPrograms();
            } else {
                JOptionPane.showMessageDialog(this, "Error: No se puede eliminar una carrera con materias asociadas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

