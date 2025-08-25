import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BookManagement extends JFrame {
    JTextField titleField, authorField, genreField, isbnField;
    JTextArea displayArea;

    public BookManagement() {
        setTitle("Book Management");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        add(new JLabel("Title:"));
        titleField = new JTextField(20); add(titleField);

        add(new JLabel("Author:"));
        authorField = new JTextField(20); add(authorField);

        add(new JLabel("Genre:"));
        genreField = new JTextField(20); add(genreField);

        add(new JLabel("ISBN:"));
        isbnField = new JTextField(20); add(isbnField);

        JButton addBtn = new JButton("Add Book");
        JButton viewBtn = new JButton("View Books");
        JButton deleteBtn = new JButton("Delete Book");

        add(addBtn); add(viewBtn); add(deleteBtn);

        displayArea = new JTextArea(15, 50);
        add(new JScrollPane(displayArea));

        addBtn.addActionListener(e -> addBook());
        viewBtn.addActionListener(e -> viewBooks());
        deleteBtn.addActionListener(e -> deleteBook());

        setVisible(true);
    }

    private void addBook() {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO books (title, author, genre, isbn, copies_available) VALUES (?, ?, ?, ?, 5)"
            );
            stmt.setString(1, titleField.getText());
            stmt.setString(2, authorField.getText());
            stmt.setString(3, genreField.getText());
            stmt.setString(4, isbnField.getText());

            stmt.executeUpdate();
            displayArea.setText("Book added successfully.\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            displayArea.setText("Error adding book.\n");
        }
    }

    private void viewBooks() {
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");

            displayArea.setText("Books in Library:\n");
            while (rs.next()) {
                displayArea.append(
                    rs.getInt("book_id") + " - " +
                    rs.getString("title") + ", " +
                    rs.getString("author") + ", " +
                    rs.getString("genre") + ", " +
                    rs.getString("isbn") + ", Available: " +
                    rs.getInt("copies_available") + "\n"
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            displayArea.setText("Error viewing books.\n");
        }
    }

    private void deleteBook() {
        String id = JOptionPane.showInputDialog(this, "Enter Book ID to delete:");
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM books WHERE book_id = ?");
            stmt.setInt(1, Integer.parseInt(id));

            int rows = stmt.executeUpdate();
            displayArea.setText(rows > 0 ? "Book deleted.\n" : "Book not found.\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            displayArea.setText("Error deleting book.\n");
        }
    }
    public static void main(String args[]){
        new BookManagement();
    }
}