import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;    //for date
import java.time.format.DateTimeFormatter;		//for date

public class Book extends JFrame implements ActionListener {

    // GUI Components declaration
	/*
    JLabel lblId, lblName, lblGrade, lblCourse;
    JTextField txtId, txtName, txtGrade, txtCourse;
    JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnViewAll;
    JTextArea textArea;
    */
	
	JLabel lblId, lblTitle, lblAuthor, lblCost;
	JTextField txtId, txtTitle, txtAuthor, txtCost;
	JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnViewAll;
	JTextArea textArea;
	

    // JDBC Variables declaration
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    public Book() {
        setTitle("Books");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);  //Allows for absolute positioning of components

        // Labels and TextFields
        lblId = new JLabel("ID:");
        lblId.setBounds(30, 30, 100, 30);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(140, 30, 200, 30);
        add(txtId);
        
        lblTitle = new JLabel("Title:");
        lblTitle.setBounds(30, 70, 100, 30);
        add(lblTitle);
        
        txtTitle = new JTextField();
        txtTitle.setBounds(140, 70, 200, 30);
        add(txtTitle);
        
        lblAuthor = new JLabel("Author:");
        lblAuthor.setBounds(30, 110, 100, 30);
        add(lblAuthor);

        txtAuthor = new JTextField();
        txtAuthor.setBounds(140, 110, 200, 30);
        add(txtAuthor);
        
        lblCost = new JLabel("Cost:");
        lblCost.setBounds(30, 150, 100, 30);
        add(lblCost);

        txtCost= new JTextField();
        txtCost.setBounds(140, 150, 200, 30);
        add(txtCost);
        

        // Buttons
        btnAdd = new JButton("Add");
        btnAdd.setBounds(30, 250, 90, 30);
        btnAdd.addActionListener(this);           //method that takes an ActionListener object as an argument and adds it to the component's list of event listeners.
        add(btnAdd);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(130, 250, 90, 30);
        btnUpdate.addActionListener(this);
        add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(230, 250, 90, 30);
        btnDelete.addActionListener(this);
        add(btnDelete);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(330, 250, 90, 30);
        btnSearch.addActionListener(this);
        add(btnSearch);

        btnViewAll = new JButton("View All");
        btnViewAll.setBounds(180, 290, 120, 30);
        btnViewAll.addActionListener(this);
        add(btnViewAll);

        // Text Area
        textArea = new JTextArea();
        textArea.setBounds(30, 330, 400, 250);
        add(textArea);

        // DB Connection
        connectToDB();
    }

    public void connectToDB() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");   //vendor specific driver class, for Oracle database

            conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE",
                "system", 
                "manager"  
            );
            System.out.println("Connected to Oracle 10g.");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Connection Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == btnAdd) {
                String sql = "INSERT INTO Books (book_id, book_title, book_author, book_cost) VALUES (?, ?, ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                pst.setString(2, txtTitle.getText());
                pst.setString(3, txtAuthor.getText());
                pst.setInt(4, Integer.parseInt(txtCost.getText()));
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Book Added");
          

            } else if (e.getSource() == btnUpdate) {
                String sql = "UPDATE Books SET book_title=?, book_author=?, book_cost=? WHERE book_id=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtTitle.getText());
                pst.setString(2, txtAuthor.getText());
                pst.setInt(3, Integer.parseInt(txtCost.getText()));
                pst.setInt(4, Integer.parseInt(txtId.getText())); 
                
                int updated = pst.executeUpdate();
                if (updated > 0)
                    JOptionPane.showMessageDialog(this, "book data Updated");
                else
                    JOptionPane.showMessageDialog(this, "book not found");

            } else if (e.getSource() == btnDelete) {
                String sql = "DELETE FROM Books WHERE book_id=?";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                int deleted = pst.executeUpdate();
                if (deleted > 0)
                    JOptionPane.showMessageDialog(this, "Book Deleted");
                else
                    JOptionPane.showMessageDialog(this, "Book not found");

            } else if (e.getSource() == btnSearch) {
                String sql = "SELECT * FROM Books WHERE book_id=?";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtTitle.setText(rs.getString("book_title"));
                    txtAuthor.setText(rs.getString("book_author"));
                    txtCost.setText(String.valueOf(rs.getInt("book_cost")));

                } else {
                    JOptionPane.showMessageDialog(this, "book not found");
                }

            } else if (e.getSource() == btnViewAll) {
                String sql = "SELECT * FROM Books";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                textArea.setText("");
                while (rs.next()) {
                    String data = "Book_id: " + rs.getInt("book_id") +
                            ", Name: " + rs.getString("book_title") +
                            ", Author: " + rs.getString("book_author")  + ", Cost: " + rs.getInt("book_cost") + "\n";
                    textArea.append(data);
                }
            }

            // Clear fields after action
            if (e.getSource() != btnSearch && e.getSource() != btnViewAll) {
                clearFields();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        txtCost.setText("");
    }

    public static void main(String[] args) {
        new Book().setVisible(true);
    }
}


