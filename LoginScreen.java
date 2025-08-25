import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginScreen extends JFrame {
    JTextField userField;
    JPasswordField passField;
    JComboBox<String> roleBox;
    JLabel resultLabel;

    public LoginScreen() {
        setTitle("Smart Library - Login");
        setSize(400, 300);
        setLayout(new GridLayout(6, 1, 10, 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[] { "Admin", "Librarian", "Student" });
        add(roleBox);

        add(new JLabel("Email / Username:"));
        userField = new JTextField();
        add(userField);

        add(new JLabel("Password:"));
        passField = new JPasswordField();
        add(passField);

        JButton loginBtn = new JButton("Login");
        add(loginBtn);

        resultLabel = new JLabel(" ", SwingConstants.CENTER);
        add(resultLabel);

        loginBtn.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        String role = (String) roleBox.getSelectedItem();
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        String table = "", idField = "", nameField = "", emailField = "";

        if (role.equals("Admin")) {
            table = "admins";
            idField = "admin_id";
            nameField = "username";
            emailField = "username";
        } else if (role.equals("Librarian")) {
            table = "librarians";
            idField = "librarian_id";
            nameField = "name";
            emailField = "email";
        } else {
            table = "students";
            idField = "student_id";
            nameField = "name";
            emailField = "email";
        }

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT " + idField + ", " + nameField + " FROM " + table + " WHERE " + emailField + " = ? AND password = ?"
            );
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(idField);
                String name = rs.getString(nameField);
                resultLabel.setText("✅ Welcome, " + name + " (" + role + ")");
                this.dispose();

                // 🔄 Redirect to correct dashboard
                switch (role) {
                    case "Admin":
                        new AdminDashboard(id, name);
                        break;
                    case "Librarian":
                        new LibrarianDashboard(id, name);
                        break;
                    case "Student":
                        new StudentDashboard(id, name);
                        break;
                }
            } else {
                resultLabel.setText("❌ Invalid credentials.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resultLabel.setText("❌ Login error.");
        }
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}
