import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class AddEmployeeForm extends JFrame {
    private JTextField idField, lastNameField, firstNameField, positionField, rateField;
    private PayrollGUI mainGUI;

    public AddEmployeeForm(PayrollGUI gui) {
        this.mainGUI = gui;

        setTitle("Add New Employee");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 5, 5));

        // Input fields
        add(new JLabel("Employee ID:"));
        idField = new JTextField();
        add(idField);

        add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        add(lastNameField);

        add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        add(firstNameField);

        add(new JLabel("Position:"));
        positionField = new JTextField();
        add(positionField);

        add(new JLabel("Daily Rate:"));
        rateField = new JTextField();
        add(rateField);

        // Buttons
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        add(saveBtn);
        add(cancelBtn);

        // Action listeners
        saveBtn.addActionListener(e -> saveEmployee());
        cancelBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void saveEmployee() {
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

            // Append to CSV
            try (FileWriter fw = new FileWriter("employees.csv", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                out.printf("%d,%s,%s,%s,%.0f", id, lastName, firstName, position, rate);
            }

            // ✅ Refresh the employee table in main GUI
            mainGUI.refreshTable();

            // Optional confirmation
            JOptionPane.showMessageDialog(this, "Employee saved successfully.");
            dispose(); // ✅ Close the form after saving

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format. Please enter valid numeric values.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving employee.");
        }
    }
}
