import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;    //for date
import java.time.format.DateTimeFormatter;		//for date

public class Users_mgmt extends JFrame implements ActionListener {

    // GUI Components declaration
	/*
    JLabel lblId, lblName, lblGrade, lblCourse;
    JTextField txtId, txtName, txtGrade, txtCourse;
    JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnViewAll;
    JTextArea textArea;
    */
	
	JLabel lblId, lblName, lblDOB, lblEmail, lblAcct;
	JTextField txtId, txtName, txtDOB, txtEmail, txtAcct;
	JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnViewAll;
	JTextArea textArea;
	

    // JDBC Variables declaration
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    public Users_mgmt() {
        setTitle("User");
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

        lblName = new JLabel("Name:");
        lblName.setBounds(30, 70, 100, 30);
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(140, 70, 200, 30);
        add(txtName);

        lblDOB = new JLabel("DOB:");
        lblDOB.setBounds(30, 110, 100, 30);
        add(lblDOB);

        txtDOB = new JTextField();
        txtDOB.setBounds(140, 110, 200, 30);
        add(txtDOB);

        lblEmail = new JLabel("Email:");
        lblEmail.setBounds(30, 150, 100, 30);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(140, 150, 200, 30);
        add(txtEmail);

        lblAcct = new JLabel("Account creation date:");
        lblAcct.setBounds(30, 190, 100, 30);
        add(lblAcct);
        
        txtAcct = new JTextField();
        txtAcct.setBounds(140, 190, 200, 30);
        add(txtAcct);
        
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
                String sql = "INSERT INTO users (user_id, user_name, user_DOB, user_email, acct_created_date) VALUES (?, ?, ?, ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                pst.setString(2, txtName.getText());
                
                // debug statement: checking if getText() part of the code is working properly
//                System.out.println("DOB field: '" + txtDOB.getText() + "'");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //creating a date formatter
                LocalDate DOB = LocalDate.parse(txtDOB.getText(), formatter); //parsing the text input to local date
                // debug statement: checking if LocalDate DOB is working properly, especially the parsing part
//                System.out.println("LocalDate: '" + DOB + "'");
                java.sql.Date sqlDOB = java.sql.Date.valueOf(DOB); //converting local date to java.sql.date 
                pst.setDate(3, sqlDOB); 
                //debug statement: checking if the sqlDOB is working properly...
//                System.out.println("sqlDOB: '" + sqlDOB + "'");
                
                pst.setString(4, txtEmail.getText());
                
                LocalDate acct = LocalDate.parse(txtAcct.getText(), formatter);
                java.sql.Date sqlAcct = java.sql.Date.valueOf(acct);
                pst.setDate(5,  sqlAcct); //for account creation date
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student Added");
          

            } else if (e.getSource() == btnUpdate) {
                String sql = "UPDATE users SET user_name=?, user_DOB=?, user_email=?, acct_created_date=? WHERE user_id=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtName.getText());
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //creating a date formatter
                LocalDate DOB = LocalDate.parse(txtDOB.getText(), formatter); //parsing the text input to local date
                java.sql.Date sqlDOB = java.sql.Date.valueOf(DOB); //converting local date to java.sql.date 
                pst.setDate(2, sqlDOB); 
                
                pst.setString(3, (txtEmail.getText()));
                
                LocalDate acct = LocalDate.parse(txtAcct.getText(), formatter);
                java.sql.Date sqlAcct = java.sql.Date.valueOf(acct);
                pst.setDate(4,  sqlAcct); //for account creation date
                
                pst.setInt(5, Integer.parseInt(txtId.getText()));
                int updated = pst.executeUpdate();
                if (updated > 0)
                    JOptionPane.showMessageDialog(this, "user data Updated");
                else
                    JOptionPane.showMessageDialog(this, "user not found");

            } else if (e.getSource() == btnDelete) {
                String sql = "DELETE FROM users WHERE user_id=?";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                int deleted = pst.executeUpdate();
                if (deleted > 0)
                    JOptionPane.showMessageDialog(this, "User Deleted");
                else
                    JOptionPane.showMessageDialog(this, "User not found");

            } else if (e.getSource() == btnSearch) {
                String sql = "SELECT * FROM users WHERE user_id=?";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtName.setText(rs.getString("user_name"));
                    txtDOB.setText(rs.getDate("user_DOB").toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    txtEmail.setText(rs.getString("user_email"));
                    txtAcct.setText(rs.getDate("acct_created_date").toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));


                } else {
                    JOptionPane.showMessageDialog(this, "user not found");
                }

            } else if (e.getSource() == btnViewAll) {
                String sql = "SELECT * FROM users";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                textArea.setText("");
                while (rs.next()) {
                    String data = "User ID: " + rs.getInt("user_id") +
                            ", Name: " + rs.getString("user_name") +
                            ", DOB: " + rs.getDate("user_DOB").toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                            ", Email: " + rs.getString("user_email") + 
                             ", Account creation date: "  + rs.getDate("acct_created_date").toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\n";
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
        txtName.setText("");
        txtDOB.setText("");
        txtEmail.setText("");
        txtAcct.setText("");
    }

    public static void main(String[] args) {
        new Users_mgmt().setVisible(true);
    }
}


