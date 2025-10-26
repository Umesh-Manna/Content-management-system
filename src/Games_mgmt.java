import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;    //for date
import java.time.format.DateTimeFormatter;		//for date

public class Games_mgmt extends JFrame implements ActionListener {

    // GUI Components declaration
	/*
    JLabel lblId, lblName, lblGrade, lblCourse;
    JTextField txtId, txtName, txtGrade, txtCourse;
    JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnViewAll;
    JTextArea textArea;
    */
	
	JLabel lblId, lblName, lblGenre, lblAmount;
	JTextField txtId, txtName, txtGenre, txtAmount;
	JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnViewAll;
	JTextArea textArea;
	

    // JDBC Variables declaration
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    public Games_mgmt() {
        setTitle("Game");
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

        lblGenre = new JLabel("Genre:");
        lblGenre.setBounds(30, 110, 100, 30);
        add(lblGenre);

        txtGenre = new JTextField();
        txtGenre.setBounds(140, 110, 200, 30);
        add(txtGenre);
        
        lblAmount = new JLabel("Amount:");
        lblAmount.setBounds(30, 150, 100, 30);
        add(lblAmount);

        txtAmount = new JTextField();
        txtAmount.setBounds(140, 150, 200, 30);
        add(txtAmount);

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
                String sql = "INSERT INTO Games (game_id, game_name, game_genre, game_amount) VALUES (?, ? , ?, ?)";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                pst.setString(2, txtName.getText());
                pst.setString(3, txtGenre.getText());
                pst.setInt(4, Integer.parseInt(txtAmount.getText()));

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Game Added");
          

            } else if (e.getSource() == btnUpdate) {
                String sql = "UPDATE Games SET game_name=?, game_genre=?, game_amount WHERE game_id=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtName.getText());
                pst.setString(2, txtGenre.getText());
                pst.setInt(3, Integer.parseInt(txtAmount.getText()));
                pst.setInt(3, Integer.parseInt(txtId.getText()));
                
                int updated = pst.executeUpdate();
                if (updated > 0)
                    JOptionPane.showMessageDialog(this, "game data Updated");
                else
                    JOptionPane.showMessageDialog(this, "game not found");

            } else if (e.getSource() == btnDelete) {
                String sql = "DELETE FROM Games WHERE game_id=?";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                int deleted = pst.executeUpdate();
                if (deleted > 0)
                    JOptionPane.showMessageDialog(this, "Game Deleted");
                else
                    JOptionPane.showMessageDialog(this, "Game not found");

            } else if (e.getSource() == btnSearch) {
                String sql = "SELECT * FROM Games WHERE game_id=?";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtName.setText(rs.getString("game_name"));
                    txtGenre.setText(rs.getString("game_amount"));
                    txtAmount.setText(String.valueOf(rs.getInt("game_amount")));

                } else {
                    JOptionPane.showMessageDialog(this, "game not found");
                }

            } else if (e.getSource() == btnViewAll) {
                String sql = "SELECT * FROM Games";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                textArea.setText("");
                while (rs.next()) {
                    String data = "App_id: " + rs.getInt("game_id") +
                            ", Name: " + rs.getString("app_name") +
                            ", Genre: " + rs.getString("app_genre") +
                            ", Amount: " + rs.getInt("game_amount") +  "\n";
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
        txtGenre.setText("");
        txtAmount.setText("");
    }

    public static void main(String[] args) {
        new Games_mgmt().setVisible(true);
    }
}


