package gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import personnel.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    
    public LoginFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initializeFrame();
        createComponents();
        layoutComponents();
        setupListeners();
    }
    
    private void initializeFrame() {
        setTitle("Connexion - Gestion du Personnel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void createComponents() {
        usernameField = new JTextField(20);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        loginButton = new JButton("Connexion") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        loginButton.setUI(new BasicButtonUI());
        loginButton.setContentAreaFilled(false);
        loginButton.setOpaque(true);
        
        cancelButton = new JButton("Quitter") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        cancelButton.setUI(new BasicButtonUI());
        cancelButton.setContentAreaFilled(false);
        cancelButton.setOpaque(true);
    }
    
    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel nord avec le titre
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Gestion du Personnel des Ligues", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        
        JLabel subtitleLabel = new JLabel("Veuillez vous connecter", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(52, 73, 94));
        
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Panel central avec formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel usernameLabel = new JLabel("Identifiant :");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        formPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);
        
        // Panel sud avec boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        
        // Assemblage final
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Ajouter un logo
        JPanel logoPanel = new JPanel(new BorderLayout());
        JLabel logoLabel = createLogoLabel();
        logoPanel.add(logoLabel, BorderLayout.CENTER);
        
        mainPanel.add(logoPanel, BorderLayout.WEST);
        
        setContentPane(mainPanel);
    }
    
    private JLabel createLogoLabel() {
        // Créer une image simple pour le logo
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Cercle de fond
        g2d.setColor(new Color(41, 128, 185));
        g2d.fillOval(0, 0, 64, 64);
        
        // Symbole utilisateur
        g2d.setColor(Color.WHITE);
        g2d.fillOval(22, 15, 20, 20); // tête
        g2d.fillRoundRect(15, 40, 35, 20, 10, 10); // corps
        
        g2d.dispose();
        
        JLabel logoLabel = new JLabel(new ImageIcon(image));
        return logoLabel;
    }
    
    private void setupListeners() {
        loginButton.addActionListener(new LoginButtonListener());
        cancelButton.addActionListener(e -> System.exit(0));
        
        // Permettre la validation avec la touche Entrée
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    new LoginButtonListener().actionPerformed(null);
                }
            }
        });
    }
    
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            try {
                // Configurer pour utiliser la sérialisation
                System.setProperty("PersistenceType", "SERIALIZATION");
                
                GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
                Employe root = gestionPersonnel.getRoot();
                
                if (root != null && root.getNom().equals(username) && root.checkPassword(password)) {
                    // Connexion réussie
                    dispose();
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                } else {
                    // Échec de connexion
                    JOptionPane.showMessageDialog(LoginFrame.this,
                        "Identifiant ou mot de passe incorrect.",
                        "Erreur d'authentification",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                    "Erreur lors de la connexion : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
} 