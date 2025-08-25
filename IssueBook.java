import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class IssueBook extends JFrame {
    JTextField studentIdField, bookIdField;
    JTextArea resultArea;

    public IssueBook() {
        setTitle("Issue Book");
        setSize(500, 400);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new JLabel("Student ID:"));
        studentIdField = new JTextField(20);
        add(studentIdField);

        add(new JLabel("Book ID:"));
        bookIdField = new JTextField(20);
        add(bookIdField);

        JButton issueBtn = new JButton("Issue Book");
        add(issueBtn);

        resultArea = new JTextArea(10, 40);
        add(new JScrollPane(resultArea));

        issueBtn.addActionListener(e -> issueBook());

        setVisible(true);
    }

    private void issueBook() {
        int studentId = Integer.parseInt(studentIdField.getText());
        int bookId = Integer.parseInt(bookIdField.getText());

        try (Connection conn = DBConnection.getConnection()) {
            // Check book availability
            PreparedStatement checkBook = conn.prepareStatement("SELECT copies_available FROM books WHERE book_id = ?");
            checkBook.setInt(1, bookId);
            ResultSet rs = checkBook.executeQuery();

            if (!rs.next()) {
                resultArea.setText("❌ Book ID not found.");
                return;
            }

            int available = rs.getInt("copies_available");
            if (available <= 0) {
                resultArea.setText("❌ Book is not available currently.");
                return;
            }

            // Issue the book
            LocalDate today = LocalDate.now();
            LocalDate dueDate = today.plusDays(14);  // 2-week issue period

            PreparedStatement issueStmt = conn.prepareStatement(
                "INSERT INTO issued_books (student_id, book_id, issue_date, due_date) VALUES (?, ?, ?, ?)"
            );
            issueStmt.setInt(1, studentId);
            issueStmt.setInt(2, bookId);
            issueStmt.setDate(3, Date.valueOf(today));
            issueStmt.setDate(4, Date.valueOf(dueDate));
            issueStmt.executeUpdate();

            // Update available copies
            PreparedStatement updateCopies = conn.prepareStatement(
                "UPDATE books SET copies_available = copies_available - 1 WHERE book_id = ?"
            );
            updateCopies.setInt(1, bookId);
            updateCopies.executeUpdate();

            resultArea.setText("✅ Book issued successfully.\nDue Date: " + dueDate);
        } catch (Exception ex) {
            ex.printStackTrace();
            resultArea.setText("❌ Error while issuing the book.");
        }
    }
    public static void main(String args[]){
        new IssueBook();
    }
}
