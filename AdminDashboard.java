import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    int adminId;
    String adminName;

    public AdminDashboard(int id, String name) {
        this.adminId = id;
        this.adminName = name;

        setTitle("Admin Dashboard - " + adminName);
        setSize(400, 400);
        setLayout(new GridLayout(6, 1, 10, 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel welcome = new JLabel("Welcome, " + adminName, SwingConstants.CENTER);
        add(welcome);

        JButton manageLibrariansBtn = new JButton("👥 Manage Librarians");
        JButton viewReportsBtn = new JButton("📊 View Fine Reports");
        JButton manageUsersBtn = new JButton("🧑‍💻 Manage Students");
        JButton logoutBtn = new JButton("🚪 Logout");

        add(manageLibrariansBtn);
        add(viewReportsBtn);
        add(manageUsersBtn);
        add(logoutBtn);

        // Action Listeners
        manageLibrariansBtn.addActionListener(e -> new ManageLibrarians());
        viewReportsBtn.addActionListener(e -> new FineReports());
        manageUsersBtn.addActionListener(e -> new ManageStudents());
        logoutBtn.addActionListener(e -> {
            this.dispose();
            new LoginScreen();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new AdminDashboard(1, "admin");
    }
}
