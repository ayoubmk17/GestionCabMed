import cabinetclasses.Medecin;
import cabinetclasses.Patient;
import cabinetclasses.Secretaire;
import cabinetclasses.CabinetMedica;
import cabinetclasses.Rdv;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static CabinetMedica cabinet = new CabinetMedica();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            afficherMenu();
            int choix = -1;
            
            // Gestion de l'entrée utilisateur avec validation
            boolean entreeValide = false;
            while (!entreeValide) {
                try {
                    String input = scanner.nextLine();
                    choix = Integer.parseInt(input);
                    if (choix >= 1 && choix <= 8) {
                        entreeValide = true;
                    } else {
                        System.out.println("Choix invalide. Veuillez entrer un nombre entre 1 et 8.");
                        System.out.print("Votre choix : ");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un nombre valide.");
                    System.out.print("Votre choix : ");
                }
            }

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

    private static void ajouterMedecin() {
        System.out.println("\n--- Ajout d'un nouveau médecin ---");
        try {
            System.out.print("Nom : ");
            String nom = scanner.nextLine();
            System.out.print("Prénom : ");
            String prenom = scanner.nextLine();
            System.out.print("Email : ");
            String email = scanner.nextLine();
            System.out.print("Spécialité : ");
            String specialite = scanner.nextLine();

            Medecin medecin = new Medecin(nom, prenom, email, specialite);
            cabinet.ajouterMed(medecin);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
            System.out.println("Veuillez réessayer avec des informations valides.");
        }
    }

    private static void ajouterPatient() {
        System.out.println("\n--- Ajout d'un nouveau patient ---");
        try {
            System.out.print("Nom : ");
            String nom = scanner.nextLine();
            System.out.print("Prénom : ");
            String prenom = scanner.nextLine();
            System.out.print("Email : ");
            String email = scanner.nextLine();
            
            int age = -1;
            while (age == -1) {
                System.out.print("Âge : ");
                try {
                    age = Integer.parseInt(scanner.nextLine());
                    if (age <= 0 || age > 150) {
                        System.out.println("L'âge doit être compris entre 1 et 150 ans.");
                        age = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un nombre valide pour l'âge.");
                }
            }
            
            System.out.print("Historique médical : ");
            String historique = scanner.nextLine();

            Patient patient = new Patient(nom, prenom, email, age, historique);
            cabinet.ajouterPatient(patient);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
            System.out.println("Veuillez réessayer avec des informations valides.");
        }
    }

    private static void programmerRdv() {
        System.out.println("\n--- Programmation d'un rendez-vous ---");

        // Afficher la liste des patients
        List<Patient> patients = cabinet.getListePatient();
        if (patients.isEmpty()) {
            System.out.println("Aucun patient disponible. Veuillez d'abord ajouter un patient.");
            return;
        }
        System.out.println("Patients disponibles :");
        for (int i = 0; i < patients.size(); i++) {
            System.out.println((i + 1) + ". " + patients.get(i).getNom() + " " + patients.get(i).getPrenom());
        }
        
        int choixPatient = -1;
        while (choixPatient == -1) {
            System.out.print("Choisir un patient (numéro) : ");
            try {
                choixPatient = Integer.parseInt(scanner.nextLine()) - 1;
                if (choixPatient < 0 || choixPatient >= patients.size()) {
                    System.out.println("Numéro de patient invalide.");
                    choixPatient = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }

        // Afficher la liste des médecins
        List<Medecin> medecins = cabinet.getListeMed();
        if (medecins.isEmpty()) {
            System.out.println("Aucun médecin disponible. Veuillez d'abord ajouter un médecin.");
            return;
        }
        System.out.println("Médecins disponibles :");
        for (int i = 0; i < medecins.size(); i++) {
            System.out.println((i + 1) + ". Dr " + medecins.get(i).getNom() + " (" + medecins.get(i).getSpecialite() + ")");
        }
        
        int choixMedecin = -1;
        while (choixMedecin == -1) {
            System.out.print("Choisir un médecin (numéro) : ");
            try {
                choixMedecin = Integer.parseInt(scanner.nextLine()) - 1;
                if (choixMedecin < 0 || choixMedecin >= medecins.size()) {
                    System.out.println("Numéro de médecin invalide.");
                    choixMedecin = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }

        boolean rdvValide = false;
        while (!rdvValide) {
            try {
                System.out.print("Date (AAAA-MM-JJ) : ");
                String date = scanner.nextLine();
                System.out.print("Heure (HH:MM) : ");
                String heure = scanner.nextLine();

                // Création et programmation du RDV
                Secretaire secretaire = new Secretaire("Secrétaire", "Cabinet", "secretariat@cabinet.com", "0102030405", cabinet);
                secretaire.programmerRdv(patients.get(choixPatient), medecins.get(choixMedecin), date, heure);
                rdvValide = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
                System.out.println("Voulez-vous réessayer ? (O/N) : ");
                String reponse = scanner.nextLine();
                if (!reponse.equalsIgnoreCase("O")) {
                    break;
                }
            }
        }
    }

    private static void modifierRdv() {
        System.out.println("\n--- Modification de rendez-vous ---");
        
        // Récupérer la liste des rendez-vous
        List<Rdv> rendezVous = cabinet.getListeRdv();
        if (rendezVous.isEmpty()) {
            System.out.println("Aucun rendez-vous à modifier.");
            return;
        }

        // Afficher la liste des rendez-vous
        System.out.println("Rendez-vous disponibles :");
        for (int i = 0; i < rendezVous.size(); i++) {
            Rdv rdv = rendezVous.get(i);
            System.out.println((i + 1) + ". RDV #" + rdv.getId() + " - Patient: " + 
                rdv.getPatient().getNom() + " - Médecin: Dr. " + rdv.getMedecin().getNom() + 
                " - Date: " + rdv.getDate() + " - Heure: " + rdv.getHeure());
        }

        // Sélectionner le rendez-vous à modifier
        int choixRdv = -1;
        while (choixRdv == -1) {
            System.out.print("Choisir un rendez-vous à modifier (numéro) : ");
            try {
                choixRdv = Integer.parseInt(scanner.nextLine()) - 1;
                if (choixRdv < 0 || choixRdv >= rendezVous.size()) {
                    System.out.println("Numéro de rendez-vous invalide.");
                    choixRdv = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }

        Rdv rdvAModifier = rendezVous.get(choixRdv);

        // Demander la nouvelle date et heure
        boolean modificationValide = false;
        while (!modificationValide) {
            try {
                System.out.print("Nouvelle date (AAAA-MM-JJ) : ");
                String nouvelleDate = scanner.nextLine();
                System.out.print("Nouvelle heure (HH:MM) : ");
                String nouvelleHeure = scanner.nextLine();

                // Créer une secrétaire et modifier le rendez-vous
                Secretaire secretaire = new Secretaire("Secrétaire", "Cabinet", "secretariat@cabinet.com", "0102030405", cabinet);
                secretaire.modifierRdv(rdvAModifier, nouvelleDate, nouvelleHeure);
                modificationValide = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
                System.out.println("Voulez-vous réessayer ? (O/N) : ");
                String reponse = scanner.nextLine();
                if (!reponse.equalsIgnoreCase("O")) {
                    break;
                }
            }
        }
    }

    private static void afficherRdvs() {
        System.out.println("\n--- Liste des rendez-vous ---");
        cabinet.afficherRdv();
    }

    private static void consulterPlanning() {
        System.out.println("\n--- Consultation planning médecin ---");
        List<Medecin> medecins = cabinet.getListeMed();
        if (medecins.isEmpty()) {
            System.out.println("Aucun médecin disponible.");
            return;
        }

        System.out.println("Médecins disponibles :");
        for (int i = 0; i < medecins.size(); i++) {
            System.out.println((i + 1) + ". Dr " + medecins.get(i).getNom());
        }
        System.out.print("Choisir un médecin (numéro) : ");
        int choix = scanner.nextInt() - 1;
        scanner.nextLine();

        medecins.get(choix).consulterPlanning();
    }

    private static void afficherStatistiques() {
        System.out.println("\n--- Statistiques du cabinet ---");
        System.out.println("Nombre de médecins: " + cabinet.getListeMed().size());
        System.out.println("Nombre de patients: " + cabinet.getListePatient().size());
    }
}