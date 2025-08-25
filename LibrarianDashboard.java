import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LibrarianDashboard extends JFrame {
    int librarianId;
    String librarianName;

    public LibrarianDashboard(int id, String name) {
        this.librarianId = id;
        this.librarianName = name;

        setTitle("Librarian Dashboard - " + librarianName);
        setSize(400, 400);
        setLayout(new GridLayout(6, 1, 10, 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel welcome = new JLabel("Welcome, " + librarianName, SwingConstants.CENTER);
        add(welcome);

        JButton manageBooksBtn = new JButton("📚 Manage Books");
        JButton issueBookBtn = new JButton("📖 Issue Book");
        JButton returnBookBtn = new JButton("🔁 Return Book");
        JButton viewStudentsBtn = new JButton("👥 View Students (Coming Soon)");
        JButton logoutBtn = new JButton("🚪 Logout");

        add(manageBooksBtn);
        add(issueBookBtn);
        add(returnBookBtn);
        add(viewStudentsBtn);
        add(logoutBtn);

        manageBooksBtn.addActionListener(e -> new BookManagement());
        issueBookBtn.addActionListener(e -> new IssueBook());
        returnBookBtn.addActionListener(e -> new ReturnBook());
        logoutBtn.addActionListener(e -> {
            this.dispose();
            new LoginScreen();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LibrarianDashboard(1, "Lib One");
    }
}
