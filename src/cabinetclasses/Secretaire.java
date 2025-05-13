package cabinetclasses;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Secretaire extends Personne {
    private String telephone;
    private CabinetMedica cabinet;

    public Secretaire(String nom, String prenom, String email, String telephone, CabinetMedica cabinet) {
        super(nom, prenom, email);
        if (cabinet == null) {
            throw new IllegalArgumentException("Le cabinet ne peut pas être null");
        }
        this.telephone = telephone;
        this.cabinet = cabinet;
    }

    // Méthode pour valider le format de la date et de l'heure
    private void validerDateHeure(String date, String heure) {
        try {
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ (exemple: 2024-03-25)");
            }
            if (!heure.matches("\\d{2}:\\d{2}")) {
                throw new IllegalArgumentException("Format d'heure invalide. Utilisez HH:MM (exemple: 14:30)");
            }
            LocalDateTime.parse(date + " " + heure, 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date ou heure invalide. Vérifiez que la date et l'heure sont correctes.");
        }
    }

    // Méthode pour programmer un rendez-vous
    public void programmerRdv(Patient patient, Medecin medecin, String date, String heure) {
        if (patient == null || medecin == null) {
            throw new IllegalArgumentException("Le patient et le médecin ne peuvent pas être null");
        }
        
        // Valider la date et l'heure avant de créer le rendez-vous
        validerDateHeure(date, heure);
        
        // Si la validation passe, créer le rendez-vous
        try {
            Rdv rdv = new Rdv(0, date, heure, medecin, patient); // L'ID sera généré par le cabinet
            cabinet.ajouterRdv(rdv);
            System.out.println("Rendez-vous programmé pour le patient " + patient.getNom() + " avec le médecin " +
                    medecin.getNom() + " le " + date + " à " + heure);
        } catch (IllegalStateException e) {
            System.out.println("Erreur : " + e.getMessage());
            throw new IllegalStateException("Impossible de créer le rendez-vous : " + e.getMessage());
        }
    }

    // Méthode pour modifier un rendez-vous
    public void modifierRdv(Rdv rdv, String nouvelleDate, String nouvelleHeure) {
        if (rdv == null) {
            throw new IllegalArgumentException("Le rendez-vous ne peut pas être null");
        }
        
        validerDateHeure(nouvelleDate, nouvelleHeure);
        
        // Vérifier la disponibilité du médecin pour la nouvelle date/heure
        Rdv nouveauRdv = new Rdv(rdv.getId(), nouvelleDate, nouvelleHeure, rdv.getMedecin(), rdv.getPatient());
        try {
            cabinet.supprimerRdv(rdv.getId());
            cabinet.ajouterRdv(nouveauRdv);
            System.out.println("Rendez-vous modifié pour " + rdv.getPatient().getNom() + " : " +
                    nouvelleDate + " à " + nouvelleHeure);
        } catch (IllegalStateException e) {
            // Restaurer l'ancien rendez-vous
            cabinet.ajouterRdv(rdv);
            System.out.println("Impossible de modifier le rendez-vous : " + e.getMessage());
        }
    }

    // Méthode pour gérer le paiement d'un patient
    public void gererPaiement(Patient patient, double montant) {
        if (patient == null || montant <= 0) {
            throw new IllegalArgumentException("Patient invalide ou montant incorrect");
        }
        System.out.println("Paiement de " + montant + "€ effectué par le patient " + patient.getNom());
    }

    // Getters et Setters
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone ne peut pas être vide");
        }
        this.telephone = telephone;
    }
}