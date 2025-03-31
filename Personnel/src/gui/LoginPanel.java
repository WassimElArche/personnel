package gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import personnel.*;

public class LoginPanel extends JPanel {
    private MainApplication application;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    
    public LoginPanel(MainApplication application) {
        this.application = application;
        createComponents();
        layoutComponents();
        setupListeners();
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
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);
        
        // Panel nord avec le titre
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Gestion du Personnel des Ligues", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        
        JLabel subtitleLabel = new JLabel("Veuillez vous connecter", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(52, 73, 94));
        
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Panel central avec formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel usernameLabel = new JLabel("Identifiant :");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
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
        
        // Panel avec le formulaire au centre
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(formPanel, BorderLayout.CENTER);
        
        // Ajouter le logo
        JLabel logoLabel = createLogoLabel();
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.add(logoLabel, BorderLayout.CENTER);
        centerPanel.add(logoPanel, BorderLayout.WEST);
        
        // Panel sud avec boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        
        // Assemblage final
        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Message d'information
        JLabel infoLabel = new JLabel("Utilisez 'root' comme identifiant et 'motdepasse123' comme mot de passe.", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(100, 100, 100));
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        infoPanel.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
        
        add(infoPanel, BorderLayout.PAGE_END);
    }
    
    private JLabel createLogoLabel() {
        // Créer une image simple pour le logo
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Cercle de fond
        g2d.setColor(new Color(41, 128, 185));
        g2d.fillOval(0, 0, 100, 100);
        
        // Symbole utilisateur
        g2d.setColor(Color.WHITE);
        g2d.fillOval(35, 25, 30, 30); // tête
        g2d.fillRoundRect(25, 60, 50, 30, 10, 10); // corps
        
        g2d.dispose();
        
        JLabel logoLabel = new JLabel(new ImageIcon(image));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
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
                GestionPersonnel gestionPersonnel = application.getGestionPersonnel();
                Employe root = gestionPersonnel.getRoot();
                
                if (root != null && root.getNom().equals(username) && root.checkPassword(password)) {
                    // Connexion réussie
                    application.navigateTo(MainApplication.MAIN_PAGE);
                    
                    // Réinitialiser les champs
                    usernameField.setText("");
                    passwordField.setText("");
                } else {
                    // Échec de connexion
                    JOptionPane.showMessageDialog(LoginPanel.this,
                        "Identifiant ou mot de passe incorrect.\nUtilisez 'root' et 'toor' par défaut.",
                        "Erreur d'authentification",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(LoginPanel.this,
                    "Erreur lors de la connexion : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
} 