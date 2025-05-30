import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox rememberMeCheckbox;
    private int attemptsLeft = 3;

    public LoginFrame() {
        setTitle("MotorPH Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 300);
        setLocationRelativeTo(null);

        // Main panel with vertical layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(panel);

        // Username
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(usernameField);

        // Password
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(passwordField);

        // Show password checkbox
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '•');
        });
        panel.add(showPassword);

        // Remember Me checkbox
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        rememberMeCheckbox = new JCheckBox("Remember Me");
        panel.add(rememberMeCheckbox);

        // Forgot password label
        JLabel forgotLabel = new JLabel("<HTML><U>Forgot Password?</U></HTML>");
        forgotLabel.setForeground(Color.BLUE.darker());
        forgotLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Please contact HR to reset your password.",
                        "Forgot Password",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(forgotLabel);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(loginButton);

        loginButton.addActionListener(e -> authenticate());

        loadRememberedUsername();
        setVisible(true);
    }

    private void authenticate() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        try (BufferedReader reader = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            boolean found = false;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                if (rememberMeCheckbox.isSelected()) {
                    saveRememberedUsername(username);
                } else {
                    clearRememberedUsername();
                }

                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                new PayrollGUI(); // Launch main app
            } else {
                attemptsLeft--;
                if (attemptsLeft > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid credentials. Attempts left: " + attemptsLeft);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Too many failed attempts. Exiting.");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading users.csv");
        }
    }

    private void saveRememberedUsername(String username) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("rememberme.txt"))) {
            writer.println(username);
        } catch (IOException e) {
            // Ignore write errors silently
        }
    }

    private void loadRememberedUsername() {
        try (BufferedReader reader = new BufferedReader(new FileReader("rememberme.txt"))) {
            String remembered = reader.readLine();
            if (remembered != null && !remembered.isEmpty()) {
                usernameField.setText(remembered);
                rememberMeCheckbox.setSelected(true);
            }
        } catch (IOException e) {
            // Ignore read errors silently
        }
    }

    private void clearRememberedUsername() {
        File file = new File("rememberme.txt");
        if (file.exists()) {
            file.delete();
        }
    }
}
