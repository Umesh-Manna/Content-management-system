import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;    //for date
import java.time.format.DateTimeFormatter;		//for date

public class Movies_mgmt extends JFrame implements ActionListener {

    // GUI Components declaration
	/*
    JLabel lblId, lblName, lblGrade, lblCourse;
    JTextField txtId, txtName, txtGrade, txtCourse;
    JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnViewAll;
    JTextArea textArea;
    */
	
	JLabel lblId, lblTitle, lblRelease;
	JTextField txtId, txtTitle, txtRelease;
	JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnViewAll;
	JTextArea textArea;

    // JDBC Variables declaration
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    public Movies_mgmt() {
        setTitle("Movie");
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


        lblRelease = new JLabel("Release date:");
        lblRelease.setBounds(30, 110, 100, 30);
        add(lblRelease);
        txtRelease= new JTextField();
        txtRelease.setBounds(140, 110, 200, 30);
        add(txtRelease);

        
        // Buttons
        btnAdd = new JButton("Add");
        btnAdd.setBounds(30, 150, 90, 30);
        btnAdd.addActionListener(this);           //method that takes an ActionListener object as an argument and adds it to the component's list of event listeners.
        add(btnAdd);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(130, 150, 90, 30);
        btnUpdate.addActionListener(this);
        add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(230, 150, 90, 30);
        btnDelete.addActionListener(this);
        add(btnDelete);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(330, 150, 90, 30);
        btnSearch.addActionListener(this);
        add(btnSearch);

        btnViewAll = new JButton("View All");
        btnViewAll.setBounds(180, 190, 120, 30);
        btnViewAll.addActionListener(this);
        add(btnViewAll);

        // Text Area
        textArea = new JTextArea();
        textArea.setBounds(30, 230, 400, 400);
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
                String sql = "INSERT INTO Movies (movie_id, movie_title, movie_release) VALUES (?, ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                pst.setString(2, txtTitle.getText());
                
                // debug statement: checking if getText() part of the code is working properly
//                System.out.println("DOB field: '" + txtDOB.getText() + "'");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //creating a date formatter
                LocalDate DOB = LocalDate.parse(txtRelease.getText(), formatter); //parsing the text input to local date
                // debug statement: checking if LocalDate DOB is working properly, especially the parsing part
//                System.out.println("LocalDate: '" + DOB + "'");
                java.sql.Date sqlDOB = java.sql.Date.valueOf(DOB); //converting local date to java.sql.date 
                pst.setDate(3, sqlDOB); 
                //debug statement: checking if the sqlDOB is working properly...
//                System.out.println("sqlDOB: '" + sqlDOB + "'");
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Movie Added");
          

            } else if (e.getSource() == btnUpdate) {
                String sql = "UPDATE Movies SET movie_title=?, movie_release=? WHERE movie_id=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtTitle.getText());
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //creating a date formatter
                LocalDate DOB = LocalDate.parse(txtRelease.getText(), formatter); //parsing the text input to local date
                java.sql.Date sqlDOB = java.sql.Date.valueOf(DOB); //converting local date to java.sql.date 
                pst.setDate(2, sqlDOB); 
                
                pst.setInt(3, Integer.parseInt(txtId.getText()));
                int updated = pst.executeUpdate();
                if (updated > 0)
                    JOptionPane.showMessageDialog(this, "movie data Updated");
                else
                    JOptionPane.showMessageDialog(this, "movie not found");

            } else if (e.getSource() == btnDelete) {
                String sql = "DELETE FROM Movies WHERE movie_id=?";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                int deleted = pst.executeUpdate();
                if (deleted > 0)
                    JOptionPane.showMessageDialog(this, "Movie Deleted");
                else
                    JOptionPane.showMessageDialog(this, "Movie not found");

            } else if (e.getSource() == btnSearch) {
                String sql = "SELECT * FROM Movies WHERE movie_id=?";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtTitle.setText(rs.getString("movie_title"));
                    txtRelease.setText(rs.getDate("movie_release").toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else {
                    JOptionPane.showMessageDialog(this, "movie not found");
                }

            } else if (e.getSource() == btnViewAll) {
                String sql = "SELECT * FROM Movies";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                textArea.setText("");
                while (rs.next()) {
                    String data = "Movie ID: " + rs.getInt("movie_id") +
                            ", Title: " + rs.getString("movie_title") +
                            ", Release: " + rs.getDate("movie_release").toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\n";
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
        txtRelease.setText("");
    }

    public static void main(String[] args) {
        new Movies_mgmt().setVisible(true);
    }
}


