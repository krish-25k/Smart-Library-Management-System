import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FineReports extends JFrame {
    JTextArea outputArea;

    public FineReports() {
        setTitle("Fine Reports");
        setSize(600, 500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JButton viewBtn = new JButton("📄 View Fine Report");
        outputArea = new JTextArea(20, 50);
        add(viewBtn);
        add(new JScrollPane(outputArea));

        viewBtn.addActionListener(e -> showFineReport());

        setVisible(true);
    }

    private void showFineReport() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                SELECT s.name AS student_name, b.title AS book_title,
                       r.return_date, r.fine
                FROM returns r
                JOIN issued_books i ON r.issue_id = i.issue_id
                JOIN students s ON i.student_id = s.student_id
                JOIN books b ON i.book_id = b.book_id
                ORDER BY r.return_date DESC
            """;

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            double totalFine = 0.0;
            outputArea.setText("📊 Fine Report:\n\n");
            while (rs.next()) {
                String student = rs.getString("student_name");
                String book = rs.getString("book_title");
                String date = rs.getString("return_date");
                double fine = rs.getDouble("fine");
                totalFine += fine;

                outputArea.append("📘 " + book + "\n");
                outputArea.append("👤 " + student + "\n");
                outputArea.append("📅 Returned: " + date + "\n");
                outputArea.append("💰 Fine: ₹" + fine + "\n");
                outputArea.append("----------------------------\n");
            }

            outputArea.append("\n💸 Total Fine Collected: ₹" + totalFine);

        } catch (Exception ex) {
            ex.printStackTrace();
            outputArea.setText("❌ Error generating fine report.");
        }
    }

    public static void main(String[] args) {
        new FineReports();
    }
}
