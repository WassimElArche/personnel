package gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import personnel.*;

public class MainFrame extends JFrame {
    private GestionPersonnel gestionPersonnel;
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel menuPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JButton liguesButton;
    private JButton employesButton;
    private JButton quitterButton;
    
    public MainFrame() {
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
        createComponents();
        layoutComponents();
        setupListeners();
    }
    
    private void initializeFrame() {
        setTitle("Gestion du Personnel des Ligues");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        
        // Ajouter une icône à la fenêtre
        ImageIcon icon = createImageIcon();
        if (icon != null) {
            setIconImage(icon.getImage());
        }
        
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);
    }
    
    private ImageIcon createImageIcon() {
        // Créer une icône simple
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(new Color(41, 128, 185));
        g2d.fillRect(0, 0, 32, 32);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(8, 8, 16, 16);
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    private void createComponents() {
        // Titre et sous-titre
        titleLabel = new JLabel("Gestion du Personnel des Ligues", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185)); // Bleu
        
        subtitleLabel = new JLabel("Sélectionnez une action", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(52, 73, 94)); // Gris foncé
        
        // Menu
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        menuPanel.setBackground(new Color(236, 240, 241)); // Gris clair
        
        // Boutons
        Dimension buttonSize = new Dimension(180, 40);
        
        liguesButton = createStyledButton("Gérer les ligues", buttonSize, new Color(52, 152, 219), Color.WHITE);
        quitterButton = createStyledButton("Quitter", buttonSize, new Color(231, 76, 60), Color.WHITE);
        
        // Panel de contenu
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
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
        // Ajout des composants au menu
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(liguesButton);
        menuPanel.add(Box.createVerticalStrut(15));
        menuPanel.add(employesButton);
        menuPanel.add(Box.createVerticalStrut(30));
        menuPanel.add(quitterButton);
        menuPanel.add(Box.createVerticalGlue());
        
        // Ajout des composants au panel de contenu
        JPanel titlePanel = new JPanel(new BorderLayout(0, 10));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);
        
        // Ajouter une image ou un logo
        JLabel logoLabel = createLogoLabel();
        
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        contentPanel.add(logoLabel, BorderLayout.CENTER);
        
        // Layout final
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    private JLabel createLogoLabel() {
        // Créer une image simple pour le logo
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dessiner un cercle
        g2d.setColor(new Color(41, 128, 185, 100));
        g2d.fillOval(25, 25, 150, 150);
        
        // Dessiner un symbole stylisé de personnes
        g2d.setColor(new Color(41, 128, 185));
        g2d.fillOval(80, 50, 40, 40); // Tête
        g2d.fillRect(85, 90, 30, 60); // Corps
        g2d.fillRect(65, 100, 20, 10); // Bras gauche
        g2d.fillRect(115, 100, 20, 10); // Bras droit
        
        g2d.dispose();
        
        JLabel logoLabel = new JLabel(new ImageIcon(image));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return logoLabel;
    }
    
    private void setupListeners() {
        liguesButton.addActionListener(new LigueButtonListener());
        employesButton.addActionListener(new EmployeButtonListener());
        quitterButton.addActionListener(new QuitterButtonListener());
    }
    
    // Écouteurs d'événements
    private class LigueButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LigueFrame ligueFrame = new LigueFrame(gestionPersonnel);
            ligueFrame.setVisible(true);
        }
    }
    
    private class EmployeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(MainFrame.this, 
                "Veuillez d'abord sélectionner une ligue", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private class QuitterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                gestionPersonnel.sauvegarder();
            } catch (SauvegardeImpossible ex) {
                JOptionPane.showMessageDialog(MainFrame.this, 
                    "Erreur lors de la sauvegarde", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Démarrer avec l'écran de connexion plutôt que MainFrame directement
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
} 