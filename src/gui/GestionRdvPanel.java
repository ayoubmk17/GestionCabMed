package gui;

import cabinetclasses.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GestionRdvPanel extends JPanel {
    private CabinetMedica cabinet;
    private CabinetMedicalGUI mainFrame;
    private JTable rdvTable;
    private DefaultTableModel tableModel;
    private JComboBox<PatientItem> patientCombo;
    private JComboBox<MedecinItem> medecinCombo;
    private JTextField dateField;
    private JTextField heureField;

    // Classes internes pour les items des ComboBox
    private class PatientItem {
        Patient patient;
        public PatientItem(Patient patient) {
            this.patient = patient;
        }
        @Override
        public String toString() {
            return patient.getNom() + " " + patient.getPrenom();
        }
    }

    private class MedecinItem {
        Medecin medecin;
        public MedecinItem(Medecin medecin) {
            this.medecin = medecin;
        }
        @Override
        public String toString() {
            return "Dr. " + medecin.getNom() + " (" + medecin.getSpecialite() + ")";
        }
    }

    public GestionRdvPanel(CabinetMedica cabinet, CabinetMedicalGUI mainFrame) {
        this.cabinet = cabinet;
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel du titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Gestion des Rendez-vous");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel principal avec split
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);

        // Panel supérieur avec la liste des rendez-vous
        JPanel upperPanel = new JPanel(new BorderLayout());
        createRdvTable();
        upperPanel.add(new JScrollPane(rdvTable), BorderLayout.CENTER);

        // Panel des boutons de gestion
        JPanel rdvButtonPanel = new JPanel();
        JButton refreshButton = new JButton("Rafraîchir");
        JButton deleteButton = new JButton("Supprimer");
        
        refreshButton.addActionListener(e -> refreshRdvTable());
        deleteButton.addActionListener(e -> deleteSelectedRdv());
        
        rdvButtonPanel.add(refreshButton);
        rdvButtonPanel.add(deleteButton);
        upperPanel.add(rdvButtonPanel, BorderLayout.SOUTH);

        // Panel inférieur pour ajouter un rendez-vous
        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.add(createAddRdvPanel(), BorderLayout.CENTER);

        splitPane.setTopComponent(upperPanel);
        splitPane.setBottomComponent(lowerPanel);
        add(splitPane, BorderLayout.CENTER);

        // Bouton retour
        JButton backButton = new JButton("Retour");
        backButton.addActionListener(e -> mainFrame.showHome());
        add(backButton, BorderLayout.SOUTH);

        refreshRdvTable();
        updateCombos();
    }

    private void createRdvTable() {
        String[] columnNames = {"ID", "Patient", "Médecin", "Date", "Heure"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        rdvTable = new JTable(tableModel);
        rdvTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private JPanel createAddRdvPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Création des composants
        patientCombo = new JComboBox<>();
        medecinCombo = new JComboBox<>();
        dateField = new JTextField(10);
        heureField = new JTextField(5);

        // Layout
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        panel.add(patientCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Médecin:"), gbc);
        gbc.gridx = 1;
        panel.add(medecinCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Date (AAAA-MM-JJ):"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Heure (HH:MM):"), gbc);
        gbc.gridx = 1;
        panel.add(heureField, gbc);

        JButton addButton = new JButton("Ajouter Rendez-vous");
        addButton.addActionListener(e -> addRdv());
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(addButton, gbc);

        return panel;
    }

    private void refreshRdvTable() {
        tableModel.setRowCount(0);
        List<Rdv> rdvs = cabinet.getListeRdv();
        for (Rdv rdv : rdvs) {
            tableModel.addRow(new Object[]{
                rdv.getId(),
                rdv.getPatient().getNom() + " " + rdv.getPatient().getPrenom(),
                "Dr. " + rdv.getMedecin().getNom(),
                rdv.getDate(),
                rdv.getHeure()
            });
        }
    }

    private void updateCombos() {
        patientCombo.removeAllItems();
        medecinCombo.removeAllItems();

        List<Patient> patients = cabinet.getListePatient();
        List<Medecin> medecins = cabinet.getListeMed();

        if (patients.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Aucun patient n'est enregistré. Veuillez d'abord ajouter un patient.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
        }

        if (medecins.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Aucun médecin n'est enregistré. Veuillez d'abord ajouter un médecin.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
        }

        for (Patient patient : patients) {
            patientCombo.addItem(new PatientItem(patient));
        }

        for (Medecin medecin : medecins) {
            medecinCombo.addItem(new MedecinItem(medecin));
        }
    }

    private void addRdv() {
        try {
            if (patientCombo.getSelectedItem() == null || medecinCombo.getSelectedItem() == null) {
                throw new IllegalArgumentException("Veuillez sélectionner un patient et un médecin");
            }

            Patient patient = ((PatientItem)patientCombo.getSelectedItem()).patient;
            Medecin medecin = ((MedecinItem)medecinCombo.getSelectedItem()).medecin;
            String date = dateField.getText().trim();
            String heure = heureField.getText().trim();

            if (date.isEmpty() || heure.isEmpty()) {
                throw new IllegalArgumentException("La date et l'heure sont obligatoires");
            }

            Secretaire secretaire = new Secretaire("Secrétaire", "Cabinet", "secretariat@cabinet.com", "0102030405", cabinet);
            secretaire.programmerRdv(patient, medecin, date, heure);

            // Si on arrive ici, c'est que le rendez-vous a été créé avec succès
            refreshRdvTable();
            clearFields();

            JOptionPane.showMessageDialog(this,
                "Rendez-vous ajouté avec succès!",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(this,
                "Erreur: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedRdv() {
        int selectedRow = rdvTable.getSelectedRow();
        if (selectedRow != -1) {
            int rdvId = (Integer) tableModel.getValueAt(selectedRow, 0);
            cabinet.supprimerRdv(rdvId);
            refreshRdvTable();
            JOptionPane.showMessageDialog(this,
                "Rendez-vous supprimé avec succès!",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un rendez-vous à supprimer",
                "Attention",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearFields() {
        dateField.setText("");
        heureField.setText("");
        patientCombo.setSelectedIndex(patientCombo.getItemCount() > 0 ? 0 : -1);
        medecinCombo.setSelectedIndex(medecinCombo.getItemCount() > 0 ? 0 : -1);
    }

    public void refreshData() {
        updateCombos();
        refreshRdvTable();
    }
} 