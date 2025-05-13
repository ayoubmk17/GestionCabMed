package gui;

import cabinetclasses.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CabinetMedicalGUI extends JFrame {
    private CabinetMedica cabinet;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Panels pour chaque fonctionnalité
    private JPanel homePanel;
    private AjouterMedecinPanel medecinPanel;
    private AjouterPatientPanel patientPanel;
    private GestionRdvPanel rdvPanel;

    public CabinetMedicalGUI() {
        cabinet = new CabinetMedica();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Gestion Cabinet Médical");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Création du layout principal
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Création des panels
        createHomePanel();
        medecinPanel = new AjouterMedecinPanel(cabinet, this);
        patientPanel = new AjouterPatientPanel(cabinet, this);
        rdvPanel = new GestionRdvPanel(cabinet, this);

        // Ajout des panels au card layout
        mainPanel.add(homePanel, "HOME");
        mainPanel.add(medecinPanel, "MEDECIN");
        mainPanel.add(patientPanel, "PATIENT");
        mainPanel.add(rdvPanel, "RDV");

        add(mainPanel);

        // Afficher le panel d'accueil
        cardLayout.show(mainPanel, "HOME");
    }

    private void createHomePanel() {
        homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titleLabel = new JLabel("Gestion du Cabinet Médical");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        homePanel.add(titleLabel);
        homePanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Boutons
        String[] buttonLabels = {
            "Ajouter un Médecin",
            "Ajouter un Patient",
            "Gestion des Rendez-vous",
            "Quitter"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(300, 50));
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            
            button.addActionListener(e -> {
                switch (label) {
                    case "Ajouter un Médecin":
                        cardLayout.show(mainPanel, "MEDECIN");
                        break;
                    case "Ajouter un Patient":
                        cardLayout.show(mainPanel, "PATIENT");
                        break;
                    case "Gestion des Rendez-vous":
                        cardLayout.show(mainPanel, "RDV");
                        break;
                    case "Quitter":
                        System.exit(0);
                        break;
                }
            });

            homePanel.add(button);
            homePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
    }

    public void showHome() {
        cardLayout.show(mainPanel, "HOME");
    }

    public void refreshRdvPanel() {
        rdvPanel.refreshData();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new CabinetMedicalGUI().setVisible(true);
        });
    }
} 