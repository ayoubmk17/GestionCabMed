package gui;

import cabinetclasses.*;
import javax.swing.*;
import java.awt.*;

public class AjouterMedecinPanel extends JPanel {
    private CabinetMedica cabinet;
    private CabinetMedicalGUI mainFrame;

    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JTextField specialiteField;

    public AjouterMedecinPanel(CabinetMedica cabinet, CabinetMedicalGUI mainFrame) {
        this.cabinet = cabinet;
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel du titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Ajouter un Médecin");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Champs du formulaire
        nomField = new JTextField(20);
        prenomField = new JTextField(20);
        emailField = new JTextField(20);
        specialiteField = new JTextField(20);

        // Ajout des composants avec GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nomField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 1;
        formPanel.add(prenomField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Spécialité:"), gbc);
        gbc.gridx = 1;
        formPanel.add(specialiteField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Panel des boutons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");

        saveButton.addActionListener(e -> saveMedecin());
        cancelButton.addActionListener(e -> {
            clearFields();
            mainFrame.showHome();
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveMedecin() {
        try {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();
            String specialite = specialiteField.getText();

            Medecin medecin = new Medecin(nom, prenom, email, specialite);
            cabinet.ajouterMed(medecin);

            JOptionPane.showMessageDialog(this,
                "Médecin ajouté avec succès!",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            mainFrame.refreshRdvPanel();
            mainFrame.showHome();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                "Erreur: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nomField.setText("");
        prenomField.setText("");
        emailField.setText("");
        specialiteField.setText("");
    }
} 