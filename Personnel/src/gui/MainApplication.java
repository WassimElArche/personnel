package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import personnel.*;

public class MainApplication extends JFrame {
    private GestionPersonnel gestionPersonnel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    // Constantes pour identifier les cartes
    public static final String LOGIN_PAGE = "LOGIN";
    public static final String MAIN_PAGE = "MAIN";
    public static final String LIGUE_PAGE = "LIGUE";
    public static final String EMPLOYE_PAGE = "EMPLOYE";
    
    // Pages
    private LoginPanel loginPanel;
    private MainPanel mainPanel;
    private LiguePanel liguePanel;
    private EmployePanel employePanel;
    
    public MainApplication() {
        // Forcer l'utilisation de la sérialisation
        System.setProperty("PersistenceType", "SERIALIZATION");
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            gestionPersonnel = GestionPersonnel.getGestionPersonnel();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        
        initializeFrame();
        createPanels();
        layoutPanels();
    }
    
    private void initializeFrame() {
        setTitle("Gestion du Personnel des Ligues");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Initialiser le gestionnaire de cartes
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        getContentPane().add(cardPanel);
        
        // Ajouter un événement pour sauvegarder à la fermeture
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (gestionPersonnel != null) {
                        gestionPersonnel.sauvegarder();
                    }
                } catch (SauvegardeImpossible ex) {
                    JOptionPane.showMessageDialog(MainApplication.this, 
                        "Erreur lors de la sauvegarde", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void createPanels() {
        // Initialiser les différentes vues
        loginPanel = new LoginPanel(this);
        mainPanel = new MainPanel(this);
        liguePanel = new LiguePanel(this);
        employePanel = new EmployePanel(this);
    }
    
    private void layoutPanels() {
        // Ajouter les panneaux au gestionnaire de cartes
        cardPanel.add(loginPanel, LOGIN_PAGE);
        cardPanel.add(mainPanel, MAIN_PAGE);
        cardPanel.add(liguePanel, LIGUE_PAGE);
        cardPanel.add(employePanel, EMPLOYE_PAGE);
        
        // Afficher la page de connexion initialement
        cardLayout.show(cardPanel, LOGIN_PAGE);
    }
    
    // Méthodes pour la navigation
    public void navigateTo(String page) {
        cardLayout.show(cardPanel, page);
    }
    
    public void showLiguePanel() {
        liguePanel.updateLigueList();
        navigateTo(LIGUE_PAGE);
    }
    
    public void showEmployePanel(Ligue ligue) {
        employePanel.setLigue(ligue);
        employePanel.updateEmployeList();
        navigateTo(EMPLOYE_PAGE);
    }
    
    // Accesseurs
    public GestionPersonnel getGestionPersonnel() {
        return gestionPersonnel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApplication app = new MainApplication();
            app.setVisible(true);
        });
    }
} 