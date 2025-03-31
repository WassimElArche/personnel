package gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import personnel.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.SortedSet;

public class EmployeFrame extends JFrame {
    private GestionPersonnel gestionPersonnel;
    private Ligue ligue;
    private JList<String> employeList;
    private DefaultListModel<String> listModel;
    private JButton ajouterButton;
    private JButton supprimerButton;
    private JButton editerButton;
    private JButton quitterButton;
    
    public EmployeFrame(GestionPersonnel gestionPersonnel, Ligue ligue) {
        this.gestionPersonnel = gestionPersonnel;
        this.ligue = ligue;
        initializeFrame();
        createComponents();
        layoutComponents();
        setupListeners();
        updateEmployeList();
    }
    
    private void initializeFrame() {
        setTitle("Gestion des Employés - " + ligue.getNom());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 400);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void createComponents() {
        // Liste des employés
        listModel = new DefaultListModel<>();
        employeList = new JList<>(listModel);
        employeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeList.setFont(new Font("Arial", Font.PLAIN, 14));
        employeList.setFixedCellHeight(30);
        
        // Boutons
        Dimension buttonSize = new Dimension(160, 35);
        
        ajouterButton = createStyledButton("Ajouter", buttonSize, new Color(52, 152, 219), Color.WHITE);
        supprimerButton = createStyledButton("Supprimer", buttonSize, new Color(52, 152, 219), Color.WHITE);
        editerButton = createStyledButton("Éditer", buttonSize, new Color(52, 152, 219), Color.WHITE);
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
        button.setFont(new Font("Arial", Font.BOLD, 13));
        
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
        setLayout(new BorderLayout(10, 10));
        
        // Panel gauche (liste)
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1), "Liste des employés"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Ajouter une barre de recherche
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(0, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        searchField.putClientProperty("JTextField.placeholderText", "Rechercher un employé...");
        
        leftPanel.add(searchField, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(employeList), BorderLayout.CENTER);
        
        // Panel droit (boutons)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        rightPanel.add(ajouterButton);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(supprimerButton);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(editerButton);
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(quitterButton);
        
        // Layout final
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Configuration du champ de recherche
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
    
    private void filterEmployes(String searchText) {
        listModel.clear();
        SortedSet<Employe> employes = ligue.getEmployes();
        for (Employe employe : employes) {
            String nomComplet = employe.getNom() + " " + employe.getPrenom();
            if (nomComplet.toLowerCase().contains(searchText)) {
                listModel.addElement(nomComplet);
            }
        }
    }
    
    private void setupListeners() {
        ajouterButton.addActionListener(new AjouterEmployeListener());
        supprimerButton.addActionListener(new SupprimerEmployeListener());
        editerButton.addActionListener(new EditerEmployeListener());
        quitterButton.addActionListener(new QuitterListener());
        
        // Double-clic sur un employé pour l'éditer
        employeList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = employeList.getSelectedIndex();
                    if (index != -1) {
                        new EditerEmployeListener().actionPerformed(null);
                    }
                }
            }
        });
    }
    
    // Écouteurs d'événements
    private class AjouterEmployeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog dialog = new JDialog(EmployeFrame.this, "Nouvel employé", true);
            dialog.setLayout(new GridBagLayout());
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(EmployeFrame.this);
            
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            
            JTextField nomField = createStyledTextField();
            JTextField prenomField = createStyledTextField();
            JTextField mailField = createStyledTextField();
            JTextField dateArriveeField = createStyledTextField();
            JTextField dateDepartField = createStyledTextField();
            JPasswordField passwordField = createStyledPasswordField();
            JTextField numeroSecuriteSocialeField = createStyledTextField();
            
            // Ligne 0
            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanel.add(createStyledLabel("Nom :"), gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formPanel.add(nomField, gbc);
            
            // Ligne 1
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0.0;
            formPanel.add(createStyledLabel("Prénom :"), gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formPanel.add(prenomField, gbc);
            
            // Ligne 2
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weightx = 0.0;
            formPanel.add(createStyledLabel("Mail :"), gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formPanel.add(mailField, gbc);
            
            // Ligne 3
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.weightx = 0.0;
            formPanel.add(createStyledLabel("Mot de passe :"), gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formPanel.add(passwordField, gbc);
            
            // Ligne 4
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.weightx = 0.0;
            formPanel.add(createStyledLabel("Date d'arrivée (YYYY-MM-DD) :"), gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formPanel.add(dateArriveeField, gbc);
            
            // Ligne 5
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.weightx = 0.0;
            formPanel.add(createStyledLabel("Date de départ (YYYY-MM-DD) :"), gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formPanel.add(dateDepartField, gbc);
            
            // Ligne 6 - Numéro de sécurité sociale
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.weightx = 0.0;
            formPanel.add(createStyledLabel("Numéro de sécurité sociale :"), gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            formPanel.add(numeroSecuriteSocialeField, gbc);
            
            // Ligne 7 - Boutons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton cancelButton = new JButton("Annuler");
            JButton okButton = new JButton("OK");
            
            styleDialogButton(cancelButton, new Color(231, 76, 60), Color.WHITE);
            styleDialogButton(okButton, new Color(46, 204, 113), Color.WHITE);
            
            buttonPanel.add(cancelButton);
            buttonPanel.add(okButton);
            
            gbc.gridx = 0;
            gbc.gridy = 7;
            gbc.gridwidth = 2;
            gbc.weightx = 1.0;
            formPanel.add(buttonPanel, gbc);
            
            dialog.add(formPanel);
            
            cancelButton.addActionListener(event -> dialog.dispose());
            
            okButton.addActionListener(new AjouterEmployeDialogListener(dialog, nomField, prenomField, 
                mailField, dateArriveeField, dateDepartField, passwordField, numeroSecuriteSocialeField));
            
            dialog.setVisible(true);
        }
    }
    
    private void styleDialogButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setUI(new BasicButtonUI());
        button.setContentAreaFilled(false);
        button.setOpaque(true);
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
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
    
    private class AjouterEmployeDialogListener implements ActionListener {
        private JDialog dialog;
        private JTextField nomField, prenomField, mailField, dateArriveeField, dateDepartField, numeroSecuriteSocialeField;
        private JPasswordField passwordField;
        
        public AjouterEmployeDialogListener(JDialog dialog, JTextField nomField, JTextField prenomField,
                                          JTextField mailField, JTextField dateArriveeField, 
                                          JTextField dateDepartField, JPasswordField passwordField,
                                          JTextField numeroSecuriteSocialeField) {
            this.dialog = dialog;
            this.nomField = nomField;
            this.prenomField = prenomField;
            this.mailField = mailField;
            this.dateArriveeField = dateArriveeField;
            this.dateDepartField = dateDepartField;
            this.passwordField = passwordField;
            this.numeroSecuriteSocialeField = numeroSecuriteSocialeField;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String mail = mailField.getText();
                String password = new String(passwordField.getPassword());
                
                LocalDate dateArrivee = null;
                if (!dateArriveeField.getText().isEmpty()) {
                    dateArrivee = LocalDate.parse(dateArriveeField.getText());
                }
                else {
                    // Date d'arrivée par défaut à aujourd'hui
                    dateArrivee = LocalDate.now();
                }
                
                LocalDate dateDepart = null;
                if (!dateDepartField.getText().isEmpty()) {
                    dateDepart = LocalDate.parse(dateDepartField.getText());
                }
                
                String numeroSecuriteSociale = numeroSecuriteSocialeField.getText();
                
                // Ajout de l'employé à la ligue avec la signature correcte
                Employe employe = ligue.addEmploye(nom, prenom, mail, password, dateArrivee, dateDepart, false, numeroSecuriteSociale);
                
                // Sauvegarde
                gestionPersonnel.sauvegarder();
                
                updateEmployeList();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Erreur lors de la création de l'employé : " + ex.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private class SupprimerEmployeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = employeList.getSelectedIndex();
            if (index != -1) {
                String nomComplet = listModel.getElementAt(index);
                int confirm = JOptionPane.showConfirmDialog(EmployeFrame.this, 
                    "Voulez-vous vraiment supprimer l'employé " + nomComplet + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        // Recherche et suppression de l'employé
                        SortedSet<Employe> employes = ligue.getEmployes();
                        for (Employe employe : employes) {
                            String nomEmploye = employe.getNom() + " " + employe.getPrenom();
                            if (nomComplet.equals(nomEmploye)) {
                                employe.remove();
                                gestionPersonnel.sauvegarder();
                                break;
                            }
                        }
                        updateEmployeList();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(EmployeFrame.this, 
                            "Erreur lors de la suppression : " + ex.getMessage(), 
                            "Erreur", 
                            JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    
    private class EditerEmployeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = employeList.getSelectedIndex();
            if (index != -1) {
                String nomComplet = listModel.getElementAt(index);
                
                // Recherche de l'employé
                SortedSet<Employe> employes = ligue.getEmployes();
                for (Employe employe : employes) {
                    String nomEmploye = employe.getNom() + " " + employe.getPrenom();
                    if (nomComplet.equals(nomEmploye)) {
                        // Utiliser le même style que le dialogue d'ajout
                        JDialog dialog = new JDialog(EmployeFrame.this, "Modifier employé", true);
                        dialog.setLayout(new GridBagLayout());
                        dialog.setSize(400, 300);
                        dialog.setLocationRelativeTo(EmployeFrame.this);
                        
                        JPanel formPanel = new JPanel(new GridBagLayout());
                        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                        
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.insets = new Insets(5, 5, 5, 5);
                        
                        // Créer les champs pré-remplis
                        JTextField nomField = createStyledTextField();
                        nomField.setText(employe.getNom());
                        
                        JTextField prenomField = createStyledTextField();
                        prenomField.setText(employe.getPrenom());
                        
                        JTextField mailField = createStyledTextField();
                        mailField.setText(employe.getMail());
                        
                        JPasswordField passwordField = createStyledPasswordField();
                        
                        LocalDate dateArrivee = employe.getDateArrivee();
                        String dateArriveeStr = dateArrivee != null ? dateArrivee.toString() : "";
                        JTextField dateArriveeField = createStyledTextField();
                        dateArriveeField.setText(dateArriveeStr);
                        
                        LocalDate dateDepart = employe.getDateDepart();
                        String dateDepartStr = dateDepart != null ? dateDepart.toString() : "";
                        JTextField dateDepartField = createStyledTextField();
                        dateDepartField.setText(dateDepartStr);
                        
                        JTextField numeroSecuriteSocialeField = createStyledTextField();
                        String numeroSecu = employe.getNumeroSecuriteSociale();
                        if (numeroSecu != null) {
                            numeroSecuriteSocialeField.setText(numeroSecu);
                        }
                        
                        // Disposition similaire au dialogue d'ajout
                        // Ligne 0
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        formPanel.add(createStyledLabel("Nom :"), gbc);
                        
                        gbc.gridx = 1;
                        gbc.weightx = 1.0;
                        formPanel.add(nomField, gbc);
                        
                        // Ligne 1
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        gbc.weightx = 0.0;
                        formPanel.add(createStyledLabel("Prénom :"), gbc);
                        
                        gbc.gridx = 1;
                        gbc.weightx = 1.0;
                        formPanel.add(prenomField, gbc);
                        
                        // Ligne 2
                        gbc.gridx = 0;
                        gbc.gridy = 2;
                        gbc.weightx = 0.0;
                        formPanel.add(createStyledLabel("Mail :"), gbc);
                        
                        gbc.gridx = 1;
                        gbc.weightx = 1.0;
                        formPanel.add(mailField, gbc);
                        
                        // Ligne 3
                        gbc.gridx = 0;
                        gbc.gridy = 3;
                        gbc.weightx = 0.0;
                        formPanel.add(createStyledLabel("Mot de passe :"), gbc);
                        
                        gbc.gridx = 1;
                        gbc.weightx = 1.0;
                        formPanel.add(passwordField, gbc);
                        
                        // Ligne 4
                        gbc.gridx = 0;
                        gbc.gridy = 4;
                        gbc.weightx = 0.0;
                        formPanel.add(createStyledLabel("Date d'arrivée (YYYY-MM-DD) :"), gbc);
                        
                        gbc.gridx = 1;
                        gbc.weightx = 1.0;
                        formPanel.add(dateArriveeField, gbc);
                        
                        // Ligne 5
                        gbc.gridx = 0;
                        gbc.gridy = 5;
                        gbc.weightx = 0.0;
                        formPanel.add(createStyledLabel("Date de départ (YYYY-MM-DD) :"), gbc);
                        
                        gbc.gridx = 1;
                        gbc.weightx = 1.0;
                        formPanel.add(dateDepartField, gbc);
                        
                        // Ligne 6 - Numéro de sécurité sociale
                        gbc.gridx = 0;
                        gbc.gridy = 6;
                        gbc.weightx = 0.0;
                        formPanel.add(createStyledLabel("Numéro de sécurité sociale :"), gbc);
                        
                        gbc.gridx = 1;
                        gbc.weightx = 1.0;
                        formPanel.add(numeroSecuriteSocialeField, gbc);
                        
                        // Ligne 7 - Boutons
                        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                        JButton cancelButton = new JButton("Annuler");
                        JButton okButton = new JButton("OK");
                        
                        styleDialogButton(cancelButton, new Color(231, 76, 60), Color.WHITE);
                        styleDialogButton(okButton, new Color(46, 204, 113), Color.WHITE);
                        
                        buttonPanel.add(cancelButton);
                        buttonPanel.add(okButton);
                        
                        gbc.gridx = 0;
                        gbc.gridy = 7;
                        gbc.gridwidth = 2;
                        gbc.weightx = 1.0;
                        formPanel.add(buttonPanel, gbc);
                        
                        dialog.add(formPanel);
                        
                        cancelButton.addActionListener(event -> dialog.dispose());
                        
                        okButton.addActionListener(event -> {
                            try {
                                employe.setNom(nomField.getText());
                                employe.setPrenom(prenomField.getText());
                                employe.setMail(mailField.getText());
                                
                                String password = new String(passwordField.getPassword());
                                if (!password.isEmpty()) {
                                    employe.setPassword(password);
                                }
                                
                                if (!dateArriveeField.getText().isEmpty()) {
                                    LocalDate nouvelleArrivee = LocalDate.parse(dateArriveeField.getText());
                                    employe.setDateArrivee(nouvelleArrivee);
                                }
                                
                                if (!dateDepartField.getText().isEmpty()) {
                                    LocalDate nouveauDepart = LocalDate.parse(dateDepartField.getText());
                                    employe.setDateDepart(nouveauDepart);
                                } else {
                                    employe.setDateDepart(null);
                                }
                                
                                employe.setNumeroSecuriteSociale(numeroSecuriteSocialeField.getText());
                                
                                gestionPersonnel.sauvegarder();
                                updateEmployeList();
                                dialog.dispose();
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(dialog, 
                                    "Erreur lors de la modification : " + ex.getMessage(), 
                                    "Erreur", 
                                    JOptionPane.ERROR_MESSAGE);
                                ex.printStackTrace();
                            }
                        });
                        
                        dialog.setVisible(true);
                        break;
                    }
                }
            }
        }
    }
    
    private class QuitterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
    
    private void updateEmployeList() {
        listModel.clear();
        SortedSet<Employe> employes = ligue.getEmployes();
        for (Employe employe : employes) {
            String nomComplet = employe.getNom() + " " + employe.getPrenom();
            listModel.addElement(nomComplet);
        }
    }
} 