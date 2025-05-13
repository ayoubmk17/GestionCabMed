package gui;

import cabinetclasses.*;
import javax.swing.*;
import java.awt.*;

public class AjouterPatientPanel extends JPanel {
    private CabinetMedica cabinet;
    private CabinetMedicalGUI mainFrame;

    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JSpinner ageSpinner;
    private JTextArea historiqueArea;

    public AjouterPatientPanel(CabinetMedica cabinet, CabinetMedicalGUI mainFrame) {
        this.cabinet = cabinet;
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel du titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Ajouter un Patient");
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
        ageSpinner = new JSpinner(new SpinnerNumberModel(18, 0, 150, 1));
        historiqueArea = new JTextArea(5, 20);
        historiqueArea.setLineWrap(true);
        historiqueArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(historiqueArea);

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
        formPanel.add(new JLabel("Âge:"), gbc);
        gbc.gridx = 1;
        formPanel.add(ageSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Historique médical:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(scrollPane, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Panel des boutons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");

        saveButton.addActionListener(e -> savePatient());
        cancelButton.addActionListener(e -> {
            clearFields();
            mainFrame.showHome();
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void savePatient() {
        try {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();
            int age = Integer.parseInt(ageSpinner.getValue().toString());
            String historique = historiqueArea.getText();

            Patient patient = new Patient(nom, prenom, email, age, historique);
            cabinet.ajouterPatient(patient);

            JOptionPane.showMessageDialog(this,
                "Patient ajouté avec succès!",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            mainFrame.refreshRdvPanel();
            mainFrame.showHome();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Erreur: L'âge doit être un nombre valide",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
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
        ageSpinner.setValue(18);
        historiqueArea.setText("");
    }
} 