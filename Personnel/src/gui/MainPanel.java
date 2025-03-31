package gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import personnel.*;
import java.time.LocalDate;

public class MainPanel extends JPanel {
    private MainApplication application;
    private JButton liguesButton;
    private JButton employesButton;
    private JButton quitterButton;
    private JButton deconnexionButton;
    private JButton rootButton;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    
    public MainPanel(MainApplication application) {
        this.application = application;
        createComponents();
        layoutComponents();
        setupListeners();
    }
    
    private void createComponents() {
        // Titre et sous-titre
        titleLabel = new JLabel("Gestion du Personnel des Ligues", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        
        subtitleLabel = new JLabel("Sélectionnez une action", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(52, 73, 94));
        
        // Boutons
        Dimension buttonSize = new Dimension(200, 45);
        
        liguesButton = createStyledButton("Gérer les ligues", buttonSize, new Color(52, 152, 219), Color.WHITE);
        employesButton = createStyledButton("Voir tous les employés", buttonSize, new Color(52, 152, 219), Color.WHITE);
        rootButton = createStyledButton("Modifier administrateur", buttonSize, new Color(155, 89, 182), Color.WHITE);
        deconnexionButton = createStyledButton("Déconnexion", buttonSize, new Color(41, 128, 185), Color.WHITE);
        quitterButton = createStyledButton("Quitter", buttonSize, new Color(231, 76, 60), Color.WHITE);
    }
    
    private JButton createStyledButton(String text, Dimension size, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
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
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(size);
        button.setPreferredSize(size);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Désactiver complètement le look and feel système pour les boutons
        button.setUI(new BasicButtonUI());
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return button;
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);
        
        // Panel nord avec le titre
        JPanel titlePanel = new JPanel(new BorderLayout(0, 10));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);
        
        // Panel central avec boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(liguesButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(employesButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(rootButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(deconnexionButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(quitterButton);
        buttonPanel.add(Box.createVerticalGlue());
        
        // Centrer les boutons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);
        
        // Ajouter une image ou un logo
        JLabel logoLabel = createLogoLabel();
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.add(logoLabel, BorderLayout.CENTER);
        
        // Layout final
        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(logoPanel, BorderLayout.SOUTH);
    }
    
    private JLabel createLogoLabel() {
        // Créer une image simple pour le logo
        BufferedImage image = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dessiner un cercle
        g2d.setColor(new Color(41, 128, 185, 100));
        g2d.fillOval(50, 0, 100, 100);
        
        // Dessiner un symbole stylisé de personnes
        g2d.setColor(new Color(41, 128, 185));
        g2d.fillOval(85, 20, 30, 30); // Tête
        g2d.fillRect(90, 50, 20, 40); // Corps
        g2d.fillRect(75, 60, 15, 8); // Bras gauche
        g2d.fillRect(110, 60, 15, 8); // Bras droit
        
        g2d.dispose();
        
        JLabel logoLabel = new JLabel(new ImageIcon(image));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return logoLabel;
    }
    
    private void setupListeners() {
        liguesButton.addActionListener(e -> {
            application.showLiguePanel();
        });
        
        employesButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(MainPanel.this,
                "Veuillez d'abord sélectionner une ligue",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        rootButton.addActionListener(e -> {
            showRootEditDialog();
        });
        
        deconnexionButton.addActionListener(e -> {
            application.navigateTo(MainApplication.LOGIN_PAGE);
        });
        
        quitterButton.addActionListener(e -> {
            try {
                application.getGestionPersonnel().sauvegarder();
            } catch (SauvegardeImpossible ex) {
                JOptionPane.showMessageDialog(MainPanel.this,
                    "Erreur lors de la sauvegarde",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        });
    }
    
    private void showRootEditDialog() {
        // Utiliser Frame.getFrames()[0] pour obtenir le frame parent ou créer un JDialog simple
        JDialog dialog;
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof Frame) {
            dialog = new JDialog((Frame) window, "Modifier l'administrateur", true);
        } else {
            dialog = new JDialog();
            dialog.setTitle("Modifier l'administrateur");
            dialog.setModal(true);
        }
        
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 450);
        dialog.setLocationRelativeTo(window);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Récupérer les informations actuelles du root
        Employe root = application.getGestionPersonnel().getRoot();
        
        // Création des champs
        JTextField nomField = createStyledTextField();
        JTextField prenomField = createStyledTextField();
        JTextField mailField = createStyledTextField();
        JTextField dateArriveeField = createStyledTextField();
        JTextField dateDepartField = createStyledTextField();
        JPasswordField ancienPasswordField = createStyledPasswordField();
        JPasswordField nouveauPasswordField = createStyledPasswordField();
        JPasswordField confirmPasswordField = createStyledPasswordField();
        
        // Préremplir avec les valeurs existantes
        if (root != null) {
            nomField.setText(root.getNom());
            prenomField.setText(root.getPrenom());
            mailField.setText(root.getMail());
            
            LocalDate dateArrivee = root.getDateArrivee();
            if (dateArrivee != null) {
                dateArriveeField.setText(dateArrivee.toString());
            }
            
            LocalDate dateDepart = root.getDateDepart();
            if (dateDepart != null) {
                dateDepartField.setText(dateDepart.toString());
            }
        }
        
        // Titre
        JLabel infoLabel = new JLabel("Informations de l'administrateur", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoLabel.setForeground(new Color(155, 89, 182));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        formPanel.add(infoLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Ligne 1 - Nom
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Nom :"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(nomField, gbc);
        
        // Ligne 2 - Prénom
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Prénom :"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(prenomField, gbc);
        
        // Ligne 3 - Mail
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Email :"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(mailField, gbc);
        
        // Ligne 4 - Date d'arrivée
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Date d'arrivée (YYYY-MM-DD) :"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(dateArriveeField, gbc);
        
        // Ligne 5 - Date de départ
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Date de départ (YYYY-MM-DD) :"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(dateDepartField, gbc);
        
        // Séparateur pour la section mot de passe
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setForeground(new Color(189, 195, 199));
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 10, 5);
        formPanel.add(separator, gbc);
        
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Titre section mot de passe
        JLabel passwordLabel = new JLabel("Changer le mot de passe", SwingConstants.CENTER);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(155, 89, 182));
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Ligne 8 - Ancien mot de passe
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Ancien mot de passe :"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(ancienPasswordField, gbc);
        
        // Ligne 9 - Nouveau mot de passe
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Nouveau mot de passe :"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(nouveauPasswordField, gbc);
        
        // Ligne 10 - Confirmation
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Confirmer mot de passe :"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(confirmPasswordField, gbc);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton cancelButton = createDialogButton("Annuler", new Color(231, 76, 60), Color.WHITE);
        JButton saveButton = createDialogButton("Enregistrer", new Color(46, 204, 113), Color.WHITE);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        // Assemblage du dialogue
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Gestionnaires d'événements
        cancelButton.addActionListener(e -> dialog.dispose());
        
        saveButton.addActionListener(e -> {
            try {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String mail = mailField.getText();
                String ancienPassword = new String(ancienPasswordField.getPassword());
                String nouveauPassword = new String(nouveauPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                
                LocalDate dateArrivee = null;
                if (!dateArriveeField.getText().isEmpty()) {
                    dateArrivee = LocalDate.parse(dateArriveeField.getText());
                }
                
                LocalDate dateDepart = null;
                if (!dateDepartField.getText().isEmpty()) {
                    dateDepart = LocalDate.parse(dateDepartField.getText());
                }
                
                // Vérifications
                if (nom.trim().isEmpty()) {
                    throw new Exception("Le nom ne peut pas être vide");
                }
                
                if (ancienPasswordField.getPassword().length != 0 && 
                    (nouveauPasswordField.getPassword().length > 0 || confirmPasswordField.getPassword().length > 0)) {
                    // Changement de mot de passe demandé
                    if (!root.checkPassword(ancienPassword)) {
                        throw new Exception("L'ancien mot de passe est incorrect");
                    }
                    
                    if (nouveauPassword.isEmpty()) {
                        throw new Exception("Le nouveau mot de passe ne peut pas être vide");
                    }
                    
                    if (!nouveauPassword.equals(confirmPassword)) {
                        throw new Exception("Les mots de passe ne correspondent pas");
                    }
                }
                
                // Appliquer les modifications
                root.setNom(nom);
                root.setPrenom(prenom);
                root.setMail(mail);
                
                if (dateArrivee != null) {
                    root.setDateArrivee(dateArrivee);
                }
                
                root.setDateDepart(dateDepart); // Peut être null
                
                // Changer le mot de passe uniquement si demandé
                if (!nouveauPassword.isEmpty() && !ancienPassword.isEmpty() && root.checkPassword(ancienPassword)) {
                    root.setPassword(nouveauPassword);
                }
                
                // Sauvegarder
                application.getGestionPersonnel().sauvegarder();
                
                JOptionPane.showMessageDialog(dialog,
                    "Les informations de l'administrateur ont été mises à jour",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.setVisible(true);
    }
    
    private JButton createDialogButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
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
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        
        button.setUI(new BasicButtonUI());
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return button;
    }
    
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return passwordField;
    }
} 