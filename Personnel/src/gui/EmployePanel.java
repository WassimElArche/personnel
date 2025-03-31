package gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import personnel.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.SortedSet;

public class EmployePanel extends JPanel {
    private MainApplication application;
    private Ligue ligue;
    private JList<Employe> employeList;
    private DefaultListModel<Employe> listModel;
    private JButton ajouterButton;
    private JButton supprimerButton;
    private JButton editerButton;
    private JButton retourButton;
    private JTextField searchField;
    private JLabel titreLabel;
    
    public EmployePanel(MainApplication application) {
        this.application = application;
        createComponents();
        layoutComponents();
        setupListeners();
    }
    
    public void setLigue(Ligue ligue) {
        this.ligue = ligue;
        titreLabel.setText("Gestion des Employés - " + ligue.getNom());
    }
    
    private void createComponents() {
        // Titre
        titreLabel = new JLabel("Gestion des Employés", SwingConstants.CENTER);
        titreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titreLabel.setForeground(new Color(41, 128, 185));
        
        // Liste des employés
        listModel = new DefaultListModel<>();
        employeList = new JList<>(listModel);
        employeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeList.setFont(new Font("Arial", Font.PLAIN, 14));
        employeList.setFixedCellHeight(30);
        employeList.setCellRenderer(new EmployeCellRenderer());
        
        // Barre de recherche
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(0, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        searchField.putClientProperty("JTextField.placeholderText", "Rechercher un employé...");
        
        // Boutons
        Dimension buttonSize = new Dimension(160, 35);
        
        ajouterButton = createStyledButton("Ajouter", buttonSize, new Color(52, 152, 219), Color.WHITE);
        supprimerButton = createStyledButton("Supprimer", buttonSize, new Color(231, 76, 60), Color.WHITE);
        editerButton = createStyledButton("Éditer", buttonSize, new Color(46, 204, 113), Color.WHITE);
        retourButton = createStyledButton("Retour", buttonSize, new Color(41, 128, 185), Color.WHITE);
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
        button.setFont(new Font("Arial", Font.BOLD, 13));
        
        button.setUI(new BasicButtonUI());
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return button;
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        // Panel contenant le titre
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        titlePanel.add(titreLabel, BorderLayout.CENTER);
        
        // Panel gauche (liste)
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1), "Liste des employés"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        leftPanel.add(searchField, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(employeList), BorderLayout.CENTER);
        
        // Panel droit (boutons)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        rightPanel.add(ajouterButton);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(supprimerButton);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(editerButton);
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(retourButton);
        
        // Layout final
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);
        contentPanel.add(leftPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);
        
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    // Renderer personnalisé pour afficher le nom et prénom de l'employé
    private class EmployeCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                     boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Employe) {
                Employe employe = (Employe) value;
                setText(employe.getNom() + " " + employe.getPrenom());
            }
            
            return this;
        }
    }
    
    private void setupListeners() {
        ajouterButton.addActionListener(new AjouterEmployeListener());
        supprimerButton.addActionListener(new SupprimerEmployeListener());
        editerButton.addActionListener(new EditerEmployeListener());
        retourButton.addActionListener(e -> application.navigateTo(MainApplication.LIGUE_PAGE));
        
        // Double-clic sur un employé pour l'éditer
        employeList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = employeList.getSelectedIndex();
                    if (index != -1) {
                        new EditerEmployeListener().actionPerformed(null);
                    }
                }
            }
        });
        
        // Filtrage sur la recherche
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = searchField.getText().toLowerCase();
                if (searchText.isEmpty()) {
                    updateEmployeList();
                } else {
                    filterEmployes(searchText);
                }
            }
        });
    }
    
    public void updateEmployeList() {
        if (ligue == null) return;
        
        listModel.clear();
        SortedSet<Employe> employes = ligue.getEmployes();
        for (Employe employe : employes) {
            listModel.addElement(employe);
        }
    }
    
    private void filterEmployes(String searchText) {
        if (ligue == null) return;
        
        listModel.clear();
        SortedSet<Employe> employes = ligue.getEmployes();
        for (Employe employe : employes) {
            String nomComplet = employe.getNom() + " " + employe.getPrenom();
            if (nomComplet.toLowerCase().contains(searchText)) {
                listModel.addElement(employe);
            }
        }
    }
    
    // Écouteurs d'événements pour les actions
    private class AjouterEmployeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (ligue == null) return;
            
            JDialog dialog = createEmployeDialog("Nouvel employé", null);
            dialog.setVisible(true);
        }
    }
    
    private class SupprimerEmployeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Employe employe = employeList.getSelectedValue();
            if (employe != null) {
                int confirm = JOptionPane.showConfirmDialog(EmployePanel.this, 
                    "Voulez-vous vraiment supprimer l'employé " + employe.getNom() + " " + employe.getPrenom() + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        employe.remove();
                        application.getGestionPersonnel().sauvegarder();
                        updateEmployeList();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(EmployePanel.this, 
                            "Erreur lors de la suppression : " + ex.getMessage(), 
                            "Erreur", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(EmployePanel.this, 
                    "Veuillez sélectionner un employé à supprimer", 
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private class EditerEmployeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Employe employe = employeList.getSelectedValue();
            if (employe != null) {
                JDialog dialog = createEmployeDialog("Modifier employé", employe);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(EmployePanel.this, 
                    "Veuillez sélectionner un employé à éditer", 
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private JDialog createEmployeDialog(String titre, Employe employeExistant) {
        JDialog dialog;
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof Frame) {
            dialog = new JDialog((Frame) window, titre, true);
        } else {
            dialog = new JDialog();
            dialog.setTitle(titre);
            dialog.setModal(true);
        }
        
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(window);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Création des champs
        JTextField nomField = createStyledTextField();
        JTextField prenomField = createStyledTextField();
        JTextField mailField = createStyledTextField();
        JTextField dateArriveeField = createStyledTextField();
        JTextField dateDepartField = createStyledTextField();
        JPasswordField passwordField = createStyledPasswordField();
        JTextField numeroSecuriteSocialeField = createStyledTextField();
        
        // Remplir avec les valeurs existantes si c'est une édition
        if (employeExistant != null) {
            nomField.setText(employeExistant.getNom());
            prenomField.setText(employeExistant.getPrenom());
            mailField.setText(employeExistant.getMail());
            
            LocalDate dateArrivee = employeExistant.getDateArrivee();
            if (dateArrivee != null) {
                dateArriveeField.setText(dateArrivee.toString());
            }
            
            LocalDate dateDepart = employeExistant.getDateDepart();
            if (dateDepart != null) {
                dateDepartField.setText(dateDepart.toString());
            }
            
            String numeroSecu = employeExistant.getNumeroSecuriteSociale();
            if (numeroSecu != null) {
                numeroSecuriteSocialeField.setText(numeroSecu);
            }
        }
        
        // Positionnement des champs dans la grille
        // Ligne 0 - Nom
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Nom :"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(nomField, gbc);
        
        // Ligne 1 - Prénom
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Prénom :"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(prenomField, gbc);
        
        // Ligne 2 - Mail
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Mail :"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(mailField, gbc);
        
        // Ligne 3 - Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel(employeExistant == null ? "Mot de passe :" : "Nouveau mot de passe :"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);
        
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
        
        // Ligne 6 - Numéro de sécurité sociale
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Numéro de sécurité sociale :"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(numeroSecuriteSocialeField, gbc);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton cancelButton = new JButton("Annuler") {
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
        cancelButton.setUI(new BasicButtonUI());
        cancelButton.setContentAreaFilled(false);
        cancelButton.setOpaque(true);
        cancelButton.setFocusPainted(false);
        
        JButton okButton = new JButton("OK") {
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
        okButton.setBackground(new Color(46, 204, 113));
        okButton.setForeground(Color.WHITE);
        okButton.setUI(new BasicButtonUI());
        okButton.setContentAreaFilled(false);
        okButton.setOpaque(true);
        okButton.setFocusPainted(false);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        
        // Assemblage du dialogue
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Gestionnaires d'événements
        cancelButton.addActionListener(e -> dialog.dispose());
        
        okButton.addActionListener(e -> {
            try {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String mail = mailField.getText();
                String password = new String(passwordField.getPassword());
                
                LocalDate dateArrivee = null;
                if (!dateArriveeField.getText().isEmpty()) {
                    dateArrivee = LocalDate.parse(dateArriveeField.getText());
                } else {
                    dateArrivee = LocalDate.now();
                }
                
                LocalDate dateDepart = null;
                if (!dateDepartField.getText().isEmpty()) {
                    dateDepart = LocalDate.parse(dateDepartField.getText());
                }
                
                if (employeExistant == null) {
                    // Création d'un nouvel employé
                    ligue.addEmploye(nom, prenom, mail, password, dateArrivee, dateDepart, false, numeroSecuriteSocialeField.getText());
                } else {
                    // Modification d'un employé existant
                    employeExistant.setNom(nom);
                    employeExistant.setPrenom(prenom);
                    employeExistant.setMail(mail);
                    
                    if (!password.isEmpty()) {
                        employeExistant.setPassword(password);
                    }
                    
                    employeExistant.setDateArrivee(dateArrivee);
                    employeExistant.setDateDepart(dateDepart);
                    employeExistant.setNumeroSecuriteSociale(numeroSecuriteSocialeField.getText());
                }
                
                application.getGestionPersonnel().sauvegarder();
                updateEmployeList();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        return dialog;
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