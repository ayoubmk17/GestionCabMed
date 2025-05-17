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

    public void programmerRdv(Patient patient, Medecin medecin, String date, String heure) {
        if (patient == null || medecin == null) {
            throw new IllegalArgumentException("Le patient et le médecin ne peuvent pas être null");
        }
        
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ");
        }
        if (!heure.matches("\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Format d'heure invalide. Utilisez HH:MM");
        }
        
        Rdv rdv = new Rdv(0, date, heure, medecin, patient);
        cabinet.ajouterRdv(rdv);
        System.out.println("Rendez-vous programmé pour " + patient.getNom() + " avec Dr. " + medecin.getNom());
    }

    public void modifierRdv(Rdv rdv, String nouvelleDate, String nouvelleHeure) {
        if (rdv == null) {
            throw new IllegalArgumentException("Le rendez-vous ne peut pas être null");
        }
        
        if (!nouvelleDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ");
        }
        if (!nouvelleHeure.matches("\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Format d'heure invalide. Utilisez HH:MM");
        }
        
        Rdv nouveauRdv = new Rdv(rdv.getId(), nouvelleDate, nouvelleHeure, rdv.getMedecin(), rdv.getPatient());
        cabinet.supprimerRdv(rdv.getId());
        cabinet.ajouterRdv(nouveauRdv);
        System.out.println("Rendez-vous modifié pour " + rdv.getPatient().getNom());
    }

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