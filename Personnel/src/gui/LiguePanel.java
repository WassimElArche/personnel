package gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import personnel.*;
import java.util.SortedSet;

public class LiguePanel extends JPanel {
    private MainApplication application;
    private JList<Ligue> ligueList;
    private DefaultListModel<Ligue> listModel;
    private JButton ajouterButton;
    private JButton supprimerButton;
    private JButton editerButton;
    private JButton employesButton;
    private JButton retourButton;
    private JTextField searchField;
    
    public LiguePanel(MainApplication application) {
        this.application = application;
        createComponents();
        layoutComponents();
        setupListeners();
    }
    
    private void createComponents() {
        // Liste des ligues
        listModel = new DefaultListModel<>();
        ligueList = new JList<>(listModel);
        ligueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ligueList.setFont(new Font("Arial", Font.PLAIN, 14));
        ligueList.setFixedCellHeight(30);
        ligueList.setCellRenderer(new LigueCellRenderer());
        
        // Barre de recherche
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(0, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        searchField.putClientProperty("JTextField.placeholderText", "Rechercher une ligue...");
        
        // Boutons
        Dimension buttonSize = new Dimension(160, 35);
        
        ajouterButton = createStyledButton("Ajouter", buttonSize, new Color(52, 152, 219), Color.WHITE);
        supprimerButton = createStyledButton("Supprimer", buttonSize, new Color(231, 76, 60), Color.WHITE);
        editerButton = createStyledButton("Éditer", buttonSize, new Color(46, 204, 113), Color.WHITE);
        employesButton = createStyledButton("Gérer les employés", buttonSize, new Color(52, 152, 219), Color.WHITE);
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
        
        // Titre
        JLabel titleLabel = new JLabel("Gestion des Ligues", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(41, 128, 185));
        
        // Panel contenant le titre
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Panel gauche (liste)
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1), "Liste des ligues"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        leftPanel.add(searchField, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(ligueList), BorderLayout.CENTER);
        
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
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(employesButton);
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
    
    // Renderer personnalisé pour afficher juste le nom de la ligue
    private class LigueCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                     boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Ligue) {
                Ligue ligue = (Ligue) value;
                setText(ligue.getNom());
            }
            
            return this;
        }
    }
    
    private void setupListeners() {
        ajouterButton.addActionListener(new AjouterLigueListener());
        supprimerButton.addActionListener(new SupprimerLigueListener());
        editerButton.addActionListener(new EditerLigueListener());
        employesButton.addActionListener(new GestionEmployesListener());
        retourButton.addActionListener(e -> application.navigateTo(MainApplication.MAIN_PAGE));
        
        // Double-clic sur une ligue pour gérer ses employés
        ligueList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = ligueList.getSelectedIndex();
                    if (index != -1) {
                        new GestionEmployesListener().actionPerformed(null);
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
                    updateLigueList();
                } else {
                    filterLigues(searchText);
                }
            }
        });
    }
    
    public void updateLigueList() {
        listModel.clear();
        GestionPersonnel gestionPersonnel = application.getGestionPersonnel();
        SortedSet<Ligue> ligues = gestionPersonnel.getLigues();
        for (Ligue ligue : ligues) {
            listModel.addElement(ligue);
        }
    }
    
    private void filterLigues(String searchText) {
        listModel.clear();
        GestionPersonnel gestionPersonnel = application.getGestionPersonnel();
        SortedSet<Ligue> ligues = gestionPersonnel.getLigues();
        for (Ligue ligue : ligues) {
            String nom = ligue.getNom().toLowerCase();
            if (nom.contains(searchText)) {
                listModel.addElement(ligue);
            }
        }
    }
    
    // Écouteurs d'événements
    private class AjouterLigueListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nom = JOptionPane.showInputDialog(LiguePanel.this, 
                "Nom de la ligue :", 
                "Nouvelle ligue", 
                JOptionPane.QUESTION_MESSAGE);
                
            if (nom != null && !nom.trim().isEmpty()) {
                try {
                    application.getGestionPersonnel().addLigue(nom);
                    updateLigueList();
                } catch (SauvegardeImpossible ex) {
                    JOptionPane.showMessageDialog(LiguePanel.this, 
                        "Erreur lors de la création de la ligue", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private class SupprimerLigueListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Ligue ligue = ligueList.getSelectedValue();
            if (ligue != null) {
                int confirm = JOptionPane.showConfirmDialog(LiguePanel.this, 
                    "Voulez-vous vraiment supprimer la ligue " + ligue.getNom() + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        ligue.remove();
                        application.getGestionPersonnel().sauvegarder();
                        updateLigueList();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(LiguePanel.this, 
                            "Erreur lors de la suppression : " + ex.getMessage(), 
                            "Erreur", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(LiguePanel.this, 
                    "Veuillez sélectionner une ligue à supprimer", 
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private class EditerLigueListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Ligue ligue = ligueList.getSelectedValue();
            if (ligue != null) {
                String nouveauNom = JOptionPane.showInputDialog(LiguePanel.this, 
                    "Nouveau nom de la ligue :", 
                    "Éditer la ligue", 
                    JOptionPane.QUESTION_MESSAGE);
                    
                if (nouveauNom != null && !nouveauNom.trim().isEmpty()) {
                    try {
                        ligue.setNom(nouveauNom);
                        application.getGestionPersonnel().sauvegarder();
                        updateLigueList();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(LiguePanel.this, 
                            "Erreur lors de la modification : " + ex.getMessage(), 
                            "Erreur", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(LiguePanel.this, 
                    "Veuillez sélectionner une ligue à éditer", 
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private class GestionEmployesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Ligue ligue = ligueList.getSelectedValue();
            if (ligue != null) {
                application.showEmployePanel(ligue);
            } else {
                JOptionPane.showMessageDialog(LiguePanel.this, 
                    "Veuillez sélectionner une ligue", 
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
} 