import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class PayrollGUI extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public PayrollGUI() {
        setTitle("MotorPH Employee Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        // Table
        String[] columns = {"Employee ID", "Last Name", "First Name", "Position", "Daily Rate"};
        tableModel = new DefaultTableModel(columns, 0);
        employeeTable = new JTable(tableModel);
        loadEmployeeData();

        // Buttons
        JButton viewButton = new JButton("View Employee");
        JButton addButton = new JButton("New Employee");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Add components
        add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event handlers (to be expanded)
        viewButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = tableModel.getValueAt(selectedRow, 0).toString();
                String lastName = tableModel.getValueAt(selectedRow, 1).toString();
                String firstName = tableModel.getValueAt(selectedRow, 2).toString();
                String position = tableModel.getValueAt(selectedRow, 3).toString();
                String rate = tableModel.getValueAt(selectedRow, 4).toString();
                new ViewEmployeeForm(id, lastName, firstName, position, rate);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee first.");
            }
        });
        addButton.addActionListener(e -> new AddEmployeeForm(this));
        updateButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                String[] rowData = new String[tableModel.getColumnCount()];
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    rowData[i] = tableModel.getValueAt(selectedRow, i).toString();
                }
                new EditEmployeeForm(this, rowData);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee to update.");
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String employeeIdToDelete = tableModel.getValueAt(selectedRow, 0).toString();
                    deleteEmployeeFromFile(employeeIdToDelete);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee to delete.");
            }
        });


        setVisible(true);
    }

    private void loadEmployeeData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("employees.csv"))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                tableModel.addRow(data);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not load employee data.");
        }
    }

    // ✅ This is defined *outside* of loadEmployeeData()
    public void refreshTable() {
        tableModel.setRowCount(0); // Clear table
        loadEmployeeData();        // Reload from CSV
    }
    private void deleteEmployeeFromFile(String employeeId) {
        File inputFile = new File("employees.csv");
        File tempFile = new File("temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith(employeeId + ",")) {
                    continue; // Skip the line to delete
                }
                writer.println(line);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error deleting employee.");
            return;
        }

        // Replace original file with updated file
        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        } else {
            JOptionPane.showMessageDialog(this, "Could not update employee file.");
        }
    }

}
