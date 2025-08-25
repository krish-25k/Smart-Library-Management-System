import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentDashboard extends JFrame {
    int studentId;
    String studentName;

    public StudentDashboard(int id, String name) {
        this.studentId = id;
        this.studentName = name;

        setTitle("Student Dashboard - " + studentName);
        setSize(400, 350);
        setLayout(new GridLayout(6, 1, 10, 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel welcome = new JLabel("Welcome, " + studentName, SwingConstants.CENTER);
        add(welcome);

        JButton viewBooksBtn = new JButton("📚 View All Books");
        JButton borrowBookBtn = new JButton("📖 Borrow Book");
        JButton returnBookBtn = new JButton("🔁 Return Book");
        JButton notificationsBtn = new JButton("🔔 View Notifications");
        JButton logoutBtn = new JButton("🚪 Logout");

        add(viewBooksBtn);
        add(borrowBookBtn);
        add(returnBookBtn);
        add(notificationsBtn);
        add(logoutBtn);

        viewBooksBtn.addActionListener(e -> new BookManagement()); // reuse view
        borrowBookBtn.addActionListener(e -> {
            IssueBook screen = new IssueBook();
            screen.studentIdField.setText(String.valueOf(studentId));
        });
        returnBookBtn.addActionListener(e -> {
            ReturnBook screen = new ReturnBook();
            screen.studentIdField.setText(String.valueOf(studentId));
        });
        notificationsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "🔔 Notifications feature coming soon!"));
        logoutBtn.addActionListener(e -> {
            this.dispose();
            new LoginScreen();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new StudentDashboard(1, "Student One");
    }
}
