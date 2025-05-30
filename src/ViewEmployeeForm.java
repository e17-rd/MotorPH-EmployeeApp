import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ViewEmployeeForm extends JFrame {
    private JLabel idLabel, nameLabel, positionLabel, rateLabel, payLabel;
    private JComboBox<String> monthCombo;
    private double dailyRate;

    public ViewEmployeeForm(String id, String lastName, String firstName, String position, String rateStr) {
        setTitle("Employee Details");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 5, 5));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        dailyRate = Double.parseDouble(rateStr);

        add(new JLabel("Employee ID:"));
        idLabel = new JLabel(id);
        add(idLabel);

        add(new JLabel("Name:"));
        nameLabel = new JLabel(firstName + " " + lastName);
        add(nameLabel);

        add(new JLabel("Position:"));
        positionLabel = new JLabel(position);
        add(positionLabel);

        add(new JLabel("Daily Rate:"));
        rateLabel = new JLabel(String.format("%.0f", dailyRate));
        add(rateLabel);

        add(new JLabel("Select Month:"));
        String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        monthCombo = new JComboBox<>(months);
        add(monthCombo);

        JButton computeBtn = new JButton("Compute Salary");
        computeBtn.addActionListener(e -> computePay());
        add(computeBtn);

        payLabel = new JLabel("Gross Pay: ");
        add(payLabel);

        setVisible(true);
    }

    private void computePay() {
        double grossPay = dailyRate * 22; // Assume 22 working days
        payLabel.setText("Gross Pay: PHP " + String.format("%.0f", grossPay));
    }
}
