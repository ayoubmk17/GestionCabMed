import cabinetclasses.Medecin;
import cabinetclasses.Patient;
import cabinetclasses.Secretaire;
import cabinetclasses.CabinetMedica;
import cabinetclasses.Rdv;

import java.util.List;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static Scanner scanner;
    private static CabinetMedica cabinet = new CabinetMedica();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args) {
        // Configuration de l'encodage pour Windows
        try {
            System.setProperty("file.encoding", "UTF-8");
            scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Erreur lors de la configuration de l'encodage: " + e.getMessage());
            scanner = new Scanner(System.in);
        }

        boolean running = true;
        while (running) {
            try {
                afficherMenu();
                int choix = lireChoixMenu();
                
                switch (choix) {
                    case 1:
                        ajouterMedecin();
                        break;
                    case 2:
                        ajouterPatient();
                        break;
                    case 3:
                        programmerRdv();
                        break;
                    case 4:
                        modifierRdv();
                        break;
                    case 5:
                        afficherRdvs();
                        break;
                    case 6:
                        consulterPlanning();
                        break;
                    case 7:
                        afficherStatistiques();
                        break;
                    case 8:
                        running = false;
                        System.out.println("Au revoir !");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
                System.out.println("Appuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }
        }
    }

    private static void afficherMenu() {
        System.out.println("\n=== Système de Gestion Médicale ===");
        System.out.println("1. Ajouter un médecin");
        System.out.println("2. Ajouter un patient");
        System.out.println("3. Programmer un rendez-vous");
        System.out.println("4. Modifier un rendez-vous");
        System.out.println("5. Afficher tous les rendez-vous");
        System.out.println("6. Consulter le planning d'un médecin");
        System.out.println("7. Afficher les statistiques du cabinet");
        System.out.println("8. Quitter");
        System.out.print("Votre choix : ");
    }

    private static int lireChoixMenu() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choix = Integer.parseInt(input);
                if (choix >= 1 && choix <= 8) {
                    return choix;
                }
                System.out.println("Veuillez entrer un nombre entre 1 et 8.");
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
            System.out.print("Votre choix : ");
        }
    }

    private static void ajouterMedecin() {
        System.out.println("\n--- Ajout d'un nouveau médecin ---");
        try {
            System.out.print("Nom : ");
            String nom = scanner.nextLine().trim();
            if (nom.isEmpty()) {
                throw new IllegalArgumentException("Le nom ne peut pas être vide");
            }

            System.out.print("Prénom : ");
            String prenom = scanner.nextLine().trim();
            if (prenom.isEmpty()) {
                throw new IllegalArgumentException("Le prénom ne peut pas être vide");
            }

            System.out.print("Email : ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty() || !email.contains("@")) {
                throw new IllegalArgumentException("Email invalide");
            }

            System.out.print("Spécialité : ");
            String specialite = scanner.nextLine().trim();
            if (specialite.isEmpty()) {
                throw new IllegalArgumentException("La spécialité ne peut pas être vide");
            }

            Medecin medecin = new Medecin(nom, prenom, email, specialite);
            cabinet.ajouterMed(medecin);
            System.out.println("Médecin ajouté avec succès !");
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private static void ajouterPatient() {
        System.out.println("\n--- Ajout d'un nouveau patient ---");
        try {
            System.out.print("Nom : ");
            String nom = scanner.nextLine().trim();
            if (nom.isEmpty()) {
                throw new IllegalArgumentException("Le nom ne peut pas être vide");
            }

            System.out.print("Prénom : ");
            String prenom = scanner.nextLine().trim();
            if (prenom.isEmpty()) {
                throw new IllegalArgumentException("Le prénom ne peut pas être vide");
            }

            System.out.print("Email : ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty() || !email.contains("@")) {
                throw new IllegalArgumentException("Email invalide");
            }

            int age = -1;
            while (age == -1) {
                System.out.print("Age : ");
                try {
                    age = Integer.parseInt(scanner.nextLine().trim());
                    if (age <= 0 || age > 150) {
                        System.out.println("L'âge doit être compris entre 1 et 150 ans.");
                        age = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un nombre valide.");
                }
            }

            System.out.print("Historique médical : ");
            String historique = scanner.nextLine().trim();

            Patient patient = new Patient(nom, prenom, email, age, historique);
            cabinet.ajouterPatient(patient);
            System.out.println("Patient ajouté avec succès !");
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private static void programmerRdv() {
        System.out.println("\n--- Programmation d'un rendez-vous ---");

        // Vérifier s'il y a des patients et des médecins
        List<Patient> patients = cabinet.getListePatient();
        List<Medecin> medecins = cabinet.getListeMed();

        if (patients.isEmpty()) {
            System.out.println("Aucun patient disponible. Veuillez d'abord ajouter un patient.");
            return;
        }
        if (medecins.isEmpty()) {
            System.out.println("Aucun médecin disponible. Veuillez d'abord ajouter un médecin.");
            return;
        }

        try {
            // Sélection du patient
            System.out.println("\nPatients disponibles :");
            for (int i = 0; i < patients.size(); i++) {
                Patient p = patients.get(i);
                System.out.printf("%d. %s %s\n", i + 1, p.getNom(), p.getPrenom());
            }

            int choixPatient = -1;
            while (choixPatient == -1) {
                System.out.print("Choisir un patient (numéro) : ");
                try {
                    choixPatient = Integer.parseInt(scanner.nextLine().trim()) - 1;
                    if (choixPatient < 0 || choixPatient >= patients.size()) {
                        System.out.println("Numéro invalide.");
                        choixPatient = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un nombre valide.");
                }
            }

            // Sélection du médecin
            System.out.println("\nMédecins disponibles :");
            for (int i = 0; i < medecins.size(); i++) {
                Medecin m = medecins.get(i);
                System.out.printf("%d. Dr. %s %s (%s)\n", 
                    i + 1, m.getNom(), m.getPrenom(), m.getSpecialite());
            }

            int choixMedecin = -1;
            while (choixMedecin == -1) {
                System.out.print("Choisir un médecin (numéro) : ");
                try {
                    choixMedecin = Integer.parseInt(scanner.nextLine().trim()) - 1;
                    if (choixMedecin < 0 || choixMedecin >= medecins.size()) {
                        System.out.println("Numéro invalide.");
                        choixMedecin = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un nombre valide.");
                }
            }

            // Saisie de la date et l'heure
            String date = null;
            while (date == null) {
                System.out.print("Date (AAAA-MM-JJ) : ");
                try {
                    date = scanner.nextLine().trim();
                    LocalDate.parse(date, DATE_FORMATTER);
                } catch (Exception e) {
                    System.out.println("Format de date invalide. Utilisez AAAA-MM-JJ");
                    date = null;
                }
            }

            String heure = null;
            while (heure == null) {
                System.out.print("Heure (HH:MM) : ");
                try {
                    heure = scanner.nextLine().trim();
                    LocalTime.parse(heure, TIME_FORMATTER);
                } catch (Exception e) {
                    System.out.println("Format d'heure invalide. Utilisez HH:MM");
                    heure = null;
                }
            }

            // Création du rendez-vous
            Patient patient = patients.get(choixPatient);
            Medecin medecin = medecins.get(choixMedecin);
            Secretaire secretaire = new Secretaire("Secrétaire", "Cabinet", 
                "secretariat@cabinet.com", "0102030405", cabinet);
            
            secretaire.programmerRdv(patient, medecin, date, heure);
            System.out.println("Rendez-vous programmé avec succès !");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private static void modifierRdv() {
        System.out.println("\n--- Modification de rendez-vous ---");
        List<Rdv> rdvs = cabinet.getListeRdv();
        
        if (rdvs.isEmpty()) {
            System.out.println("Aucun rendez-vous à modifier.");
            return;
        }

        try {
            // Afficher la liste des rendez-vous
            System.out.println("\nRendez-vous disponibles :");
            for (Rdv rdv : rdvs) {
                System.out.printf("RDV #%d - Le %s à %s\n", 
                    rdv.getId(), rdv.getDate(), rdv.getHeure());
                System.out.printf("  Dr. %s avec %s %s\n",
                    rdv.getMedecin().getNom(),
                    rdv.getPatient().getNom(),
                    rdv.getPatient().getPrenom());
            }

            // Sélection du rendez-vous
            Rdv rdvToModify = null;
            while (rdvToModify == null) {
                System.out.print("\nEntrez l'ID du rendez-vous à modifier : ");
                try {
                    final int idSaisi = Integer.parseInt(scanner.nextLine().trim());
                    rdvToModify = rdvs.stream()
                        .filter(r -> r.getId() == idSaisi)
                        .findFirst()
                        .orElse(null);
                    
                    if (rdvToModify == null) {
                        System.out.println("ID de rendez-vous invalide. Veuillez réessayer.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un nombre valide.");
                }
            }

            // Saisie de la nouvelle date et heure
            String nouvelleDate = null;
            while (nouvelleDate == null) {
                System.out.print("Nouvelle date (AAAA-MM-JJ) : ");
                try {
                    nouvelleDate = scanner.nextLine().trim();
                    LocalDate.parse(nouvelleDate, DATE_FORMATTER);
                } catch (Exception e) {
                    System.out.println("Format de date invalide. Utilisez AAAA-MM-JJ");
                    nouvelleDate = null;
                }
            }

            String nouvelleHeure = null;
            while (nouvelleHeure == null) {
                System.out.print("Nouvelle heure (HH:MM) : ");
                try {
                    nouvelleHeure = scanner.nextLine().trim();
                    LocalTime.parse(nouvelleHeure, TIME_FORMATTER);
                } catch (Exception e) {
                    System.out.println("Format d'heure invalide. Utilisez HH:MM");
                    nouvelleHeure = null;
                }
            }

            // Vérifier si le médecin est disponible à cette nouvelle date/heure
            if (!cabinet.estDisponible(rdvToModify.getMedecin(), nouvelleDate, nouvelleHeure)) {
                throw new IllegalStateException("Le médecin n'est pas disponible à ce créneau");
            }

            cabinet.modifierRdv(rdvToModify.getId(), nouvelleDate, nouvelleHeure);
            System.out.println("Rendez-vous modifié avec succès !");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private static void afficherRdvs() {
        System.out.println("\n--- Liste des rendez-vous ---");
        List<Rdv> rdvs = cabinet.getListeRdv();
        
        if (rdvs.isEmpty()) {
            System.out.println("Aucun rendez-vous programmé.");
            return;
        }

        cabinet.afficherRdv();
    }

    private static void consulterPlanning() {
        System.out.println("\n--- Consultation planning médecin ---");
        List<Medecin> medecins = cabinet.getListeMed();
        
        if (medecins.isEmpty()) {
            System.out.println("Aucun médecin disponible.");
            return;
        }

        // Afficher la liste des médecins
        System.out.println("\nListe des médecins :");
        for (int i = 0; i < medecins.size(); i++) {
            Medecin med = medecins.get(i);
            System.out.printf("%d. Dr. %s %s (%s)\n", 
                i + 1, med.getNom(), med.getPrenom(), med.getSpecialite());
        }

        // Sélection du médecin
        int choix = -1;
        while (choix == -1) {
            System.out.print("\nChoisir un médecin (numéro) : ");
            try {
                choix = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (choix < 0 || choix >= medecins.size()) {
                    System.out.println("Numéro invalide.");
                    choix = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }

        // Afficher le planning
        Medecin medecinChoisi = medecins.get(choix);
        if (medecinChoisi != null) {
            List<Rdv> rdvsMedecin = cabinet.getRdvMedecin(medecinChoisi);
            if (rdvsMedecin.isEmpty()) {
                System.out.println("\nAucun rendez-vous programmé pour ce médecin.");
            } else {
                medecinChoisi.consulterPlanning();
            }
        }
    }

    private static void afficherStatistiques() {
        System.out.println("\n--- Statistiques du cabinet ---");
        System.out.println("----------------------------------------");

        // Statistiques générales
        int nbMedecins = cabinet.getListeMed().size();
        int nbPatients = cabinet.getListePatient().size();
        int nbRdvTotal = cabinet.getListeRdv().size();

        System.out.println("Statistiques générales :");
        System.out.printf("- Nombre de médecins : %d\n", nbMedecins);
        System.out.printf("- Nombre de patients : %d\n", nbPatients);
        System.out.printf("- Nombre total de rendez-vous : %d\n", nbRdvTotal);

        if (nbMedecins > 0) {
            System.out.println("\nStatistiques par médecin :");
            System.out.println("----------------------------------------");
            
            for (Medecin medecin : cabinet.getListeMed()) {
                int nbRdvMedecin = cabinet.getRdvMedecin(medecin).size();
                
                System.out.printf("\nDr. %s %s (%s)\n",
                    medecin.getNom(),
                    medecin.getPrenom(),
                    medecin.getSpecialite());
                System.out.printf("- Nombre de rendez-vous : %d\n", nbRdvMedecin);
                
                if (nbRdvTotal > 0) {
                    double pourcentage = (nbRdvMedecin * 100.0) / nbRdvTotal;
                    System.out.printf("- Pourcentage des RDV : %.1f%%\n", pourcentage);
                }
            }
        }
        System.out.println("----------------------------------------");
    }
}