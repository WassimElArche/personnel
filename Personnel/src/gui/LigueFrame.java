package gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import personnel.*;
import java.util.SortedSet;

public class LigueFrame extends JFrame {
    private GestionPersonnel gestionPersonnel;
    private JList<String> ligueList;
    private DefaultListModel<String> listModel;
    private JButton ajouterButton;
    private JButton supprimerButton;
    private JButton editerButton;
    private JButton employesButton;
    private JButton quitterButton;
    
    public LigueFrame(GestionPersonnel gestionPersonnel) {
        this.gestionPersonnel = gestionPersonnel;
        initializeFrame();
        createComponents();
        layoutComponents();
        setupListeners();
        updateLigueList();
    }
    
    private void initializeFrame() {
        setTitle("Gestion des Ligues");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 350);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void createComponents() {
        // Liste des ligues
        listModel = new DefaultListModel<>();
        ligueList = new JList<>(listModel);
        ligueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ligueList.setFont(new Font("Arial", Font.PLAIN, 14));
        ligueList.setFixedCellHeight(30);
        
        // Boutons
        Dimension buttonSize = new Dimension(160, 35);
        
        ajouterButton = createStyledButton("Ajouter", buttonSize, new Color(52, 152, 219), Color.WHITE);
        supprimerButton = createStyledButton("Supprimer", buttonSize, new Color(52, 152, 219), Color.WHITE);
        editerButton = createStyledButton("Éditer", buttonSize, new Color(52, 152, 219), Color.WHITE);
        employesButton = createStyledButton("Gérer les employés", buttonSize, new Color(52, 152, 219), Color.WHITE);
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
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1), "Liste des ligues"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Ajouter une barre de recherche
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(0, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        searchField.putClientProperty("JTextField.placeholderText", "Rechercher une ligue...");
        
        leftPanel.add(searchField, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(ligueList), BorderLayout.CENTER);
        
        // Panel droit (boutons)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        rightPanel.add(ajouterButton);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(supprimerButton);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(editerButton);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(employesButton);
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
                    updateLigueList();
                } else {
                    filterLigues(searchText);
                }
            }
        });
    }
    
    private void filterLigues(String searchText) {
        listModel.clear();
        SortedSet<Ligue> ligues = gestionPersonnel.getLigues();
        for (Ligue ligue : ligues) {
            String nom = ligue.getNom().toLowerCase();
            if (nom.contains(searchText)) {
                listModel.addElement(ligue.getNom());
            }
        }
    }
    
    private void setupListeners() {
        ajouterButton.addActionListener(new AjouterLigueListener());
        supprimerButton.addActionListener(new SupprimerLigueListener());
        editerButton.addActionListener(new EditerLigueListener());
        employesButton.addActionListener(new GestionEmployesListener());
        quitterButton.addActionListener(new QuitterListener());
        
        // Double-clic sur une ligue pour gérer ses employés
        ligueList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = ligueList.getSelectedIndex();
                    if (index != -1) {
                        new GestionEmployesListener().actionPerformed(null);
                    }
                }
            }
        });
    }
    
    // Écouteurs d'événements
    private class AjouterLigueListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nom = JOptionPane.showInputDialog(LigueFrame.this, 
                "Nom de la ligue :", 
                "Nouvelle ligue", 
                JOptionPane.QUESTION_MESSAGE);
                
            if (nom != null && !nom.trim().isEmpty()) {
                try {
                    gestionPersonnel.addLigue(nom);
                    updateLigueList();
                } catch (SauvegardeImpossible ex) {
                    JOptionPane.showMessageDialog(LigueFrame.this, 
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
            int index = ligueList.getSelectedIndex();
            if (index != -1) {
                String nomLigue = listModel.getElementAt(index);
                int confirm = JOptionPane.showConfirmDialog(LigueFrame.this, 
                    "Voulez-vous vraiment supprimer la ligue " + nomLigue + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        // Recherche de la ligue par son nom
                        SortedSet<Ligue> ligues = gestionPersonnel.getLigues();
                        for (Ligue ligue : ligues) {
                            if (ligue.getNom().equals(nomLigue)) {
                                // Suppression de la ligue
                                ligue.remove();
                                gestionPersonnel.sauvegarder();
                                break;
                            }
                        }
                        updateLigueList();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(LigueFrame.this, 
                            "Erreur lors de la suppression : " + ex.getMessage(), 
                            "Erreur", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    
    private class EditerLigueListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = ligueList.getSelectedIndex();
            if (index != -1) {
                String nomLigue = listModel.getElementAt(index);
                String nouveauNom = JOptionPane.showInputDialog(LigueFrame.this, 
                    "Nouveau nom de la ligue :", 
                    "Éditer la ligue", 
                    JOptionPane.QUESTION_MESSAGE);
                    
                if (nouveauNom != null && !nouveauNom.trim().isEmpty()) {
                    try {
                        // Recherche de la ligue par son nom
                        SortedSet<Ligue> ligues = gestionPersonnel.getLigues();
                        for (Ligue ligue : ligues) {
                            if (ligue.getNom().equals(nomLigue)) {
                                // Modification du nom de la ligue
                                ligue.setNom(nouveauNom);
                                gestionPersonnel.sauvegarder();
                                break;
                            }
                        }
                        updateLigueList();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(LigueFrame.this, 
                            "Erreur lors de la modification : " + ex.getMessage(), 
                            "Erreur", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    
    private class GestionEmployesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = ligueList.getSelectedIndex();
            if (index != -1) {
                String nomLigue = listModel.getElementAt(index);
                // Recherche de la ligue par son nom
                SortedSet<Ligue> ligues = gestionPersonnel.getLigues();
                for (Ligue ligue : ligues) {
                    if (ligue.getNom().equals(nomLigue)) {
                        // Ouverture de la fenêtre de gestion des employés
                        EmployeFrame employeFrame = new EmployeFrame(gestionPersonnel, ligue);
                        employeFrame.setVisible(true);
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
    
    private void updateLigueList() {
        listModel.clear();
        SortedSet<Ligue> ligues = gestionPersonnel.getLigues();
        for (Ligue ligue : ligues) {
            listModel.addElement(ligue.getNom());
        }
    }
} 