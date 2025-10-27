import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainDashboard extends JFrame {

    public MainDashboard() {
        setTitle("Content Management System Dashboard");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ==== Background Panel ====
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Create gradient background
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(63, 81, 181);  // Indigo
                Color color2 = new Color(144, 164, 174); // Light gray-blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ==== Title ====
        JLabel title = new JLabel("Content Management Dashboard", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        backgroundPanel.add(title, BorderLayout.NORTH);

        // ==== Buttons ====
        JButton appsButton = createStyledButton(" Manage Apps", "app_icon.png");
        JButton booksButton = createStyledButton(" Manage Books", "book_icon.png");
        JButton gamesButton = createStyledButton(" Manage Games", "game_icon.png");
        JButton moviesButton = createStyledButton(" Manage Movies", "movie_icon.png");
        JButton usersButton = createStyledButton(" Manage Users", "user_icon.png");

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.add(appsButton);
        buttonPanel.add(booksButton);
        buttonPanel.add(gamesButton);
        buttonPanel.add(moviesButton);
        buttonPanel.add(usersButton);

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        add(backgroundPanel);

        // ==== Button actions ====
        appsButton.addActionListener(e -> new Apps_mgmt().setVisible(true));
        booksButton.addActionListener(e -> new Book().setVisible(true));
        gamesButton.addActionListener(e -> new Games_mgmt().setVisible(true));
        moviesButton.addActionListener(e -> new Movies_mgmt().setVisible(true));
        usersButton.addActionListener(e -> new Users_mgmt().setVisible(true));
    }

    // ==== Reusable button creation method ====
    private JButton createStyledButton(String text, String iconName) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(33, 150, 243)); // blue accent
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(25, 118, 210)); // darker blue
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(33, 150, 243));
            }
        });

        // Optionally add icons (put icon images in the same project folder)
        try {
            ImageIcon icon = new ImageIcon(iconName);
            Image scaled = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            // Ignore if icons not found
        }

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainDashboard().setVisible(true);
        });
    }
}
