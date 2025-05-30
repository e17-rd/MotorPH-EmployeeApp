import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class EditEmployeeForm extends JFrame {
    private JTextField idField, lastNameField, firstNameField, positionField, rateField;
    private PayrollGUI mainGUI;
    private String originalId;

    public EditEmployeeForm(PayrollGUI gui, String[] employeeData) {
        this.mainGUI = gui;
        this.originalId = employeeData[0]; // Used to locate the correct record

        setTitle("Edit Employee");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 5, 5));

        // Fields
        add(new JLabel("Employee ID:"));
        idField = new JTextField(employeeData[0]);
        add(idField);

        add(new JLabel("Last Name:"));
        lastNameField = new JTextField(employeeData[1]);
        add(lastNameField);

        add(new JLabel("First Name:"));
        firstNameField = new JTextField(employeeData[2]);
        add(firstNameField);

        add(new JLabel("Position:"));
        positionField = new JTextField(employeeData[3]);
        add(positionField);

        add(new JLabel("Daily Rate:"));
        rateField = new JTextField(employeeData[4]);
        add(rateField);

        // Buttons
        JButton saveBtn = new JButton("Save Changes");
        JButton cancelBtn = new JButton("Cancel");
        add(saveBtn);
        add(cancelBtn);

        saveBtn.addActionListener(e -> saveChanges());
        cancelBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void saveChanges() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            double rate = Double.parseDouble(rateField.getText().trim());

            String lastName = lastNameField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String position = positionField.getText().trim();

            if (lastName.isEmpty() || firstName.isEmpty() || position.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            File inputFile = new File("employees.csv");
            File tempFile = new File("employees_temp.csv");

            try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                PrintWriter writer = new PrintWriter(new FileWriter(tempFile))
            ) {
                String line;
                boolean headerSkipped = false;
                while ((line = reader.readLine()) != null) {
                    if (!headerSkipped) {
                        writer.println(line); // write header
                        headerSkipped = true;
                        continue;
                    }

                    String[] data = line.split(",");
                    if (data[0].equals(originalId)) {
                        writer.printf("%d,%s,%s,%s,%.0f%n", id, lastName, firstName, position, rate);
                    } else {
                        writer.println(line);
                    }
                }
            }

            if (!inputFile.delete()) {
                throw new IOException("Could not delete original file");
            }
            if (!tempFile.renameTo(inputFile)) {
                throw new IOException("Could not rename temp file");
            }

            mainGUI.refreshTable();
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error updating employee.");
        }
    }
}
