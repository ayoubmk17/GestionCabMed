package cabinetclasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Medecin extends Personne {
    private String specialite;
    private List<Patient> listePatient;
    private CabinetMedica cabinet;

    public Medecin(String nom, String prenom, String email, String specialite) {
        super(nom, prenom, email);
        if (specialite == null || specialite.trim().isEmpty()) {
            throw new IllegalArgumentException("La spécialité ne peut pas être vide");
        }
        this.specialite = specialite.trim();
        this.listePatient = new ArrayList<>();
    }

    public void setCabinet(CabinetMedica cabinet) {
        this.cabinet = cabinet;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Medecin medecin = (Medecin) obj;
        return getNom().equals(medecin.getNom()) && 
               getPrenom().equals(medecin.getPrenom()) && 
               getEmail().equals(medecin.getEmail());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (specialite != null ? specialite.hashCode() : 0);
        return result;
    }

    public void consulterPlanning() {
        if (cabinet == null) {
            throw new IllegalStateException("Erreur: Cabinet non défini");
        }

        List<Rdv> rdvs = cabinet.getRdvMedecin(this);
        
        System.out.println("\nPlanning du Dr. " + getNom() + " " + getPrenom());
        System.out.println("Spécialité: " + specialite);
        System.out.println("----------------------------------------");

        if (rdvs.isEmpty()) {
            System.out.println("Aucun rendez-vous programmé");
        } else {
            // Grouper les rendez-vous par date
            String dateActuelle = null;
            for (Rdv rdv : rdvs) {
                if (!rdv.getDate().equals(dateActuelle)) {
                    dateActuelle = rdv.getDate();
                    // Formater la date pour l'affichage
                    LocalDate date = LocalDate.parse(dateActuelle);
                    System.out.printf("\nDate: %s\n", 
                        date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
                }
                System.out.printf("- %s : Patient %s %s\n",
                    rdv.getHeure(),
                    rdv.getPatient().getNom(),
                    rdv.getPatient().getPrenom());
            }
        }
        System.out.println("----------------------------------------");
    }

    public void consulterPlanningDate(String date) {
        if (cabinet == null) {
            throw new IllegalStateException("Erreur: Cabinet non défini");
        }

        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ");
        }

        List<Rdv> rdvs = cabinet.getRdvMedecin(this);
        
        System.out.printf("\nPlanning du Dr. %s %s pour le %s\n", 
            getNom(), getPrenom(), date);
        System.out.println("----------------------------------------");

        boolean aDesRdv = false;
        for (Rdv rdv : rdvs) {
            if (rdv.getDate().equals(date)) {
                System.out.printf("%s - Patient: %s %s\n",
                    rdv.getHeure(),
                    rdv.getPatient().getNom(),
                    rdv.getPatient().getPrenom());
                aDesRdv = true;
            }
        }

        if (!aDesRdv) {
            System.out.println("Aucun rendez-vous programmé pour cette date");
        }
        System.out.println("----------------------------------------");
    }

    public void verifierHistorique(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Le patient ne peut pas être null");
        }
        System.out.printf("Historique médical de %s %s: %s\n", 
            patient.getNom(), 
            patient.getPrenom(), 
            patient.getHistorique());
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        if (specialite == null || specialite.trim().isEmpty()) {
            throw new IllegalArgumentException("La spécialité ne peut pas être vide");
        }
        this.specialite = specialite.trim();
    }

    public List<Patient> getListePatient() {
        return new ArrayList<>(listePatient);
    }

    public void setListePatient(List<Patient> listePatient) {
        this.listePatient = new ArrayList<>(listePatient);
    }

    public void ajouterPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Le patient ne peut pas être null");
        }
        if (!listePatient.contains(patient)) {
            listePatient.add(patient);
        }
    }

    public boolean estDisponible(String date, String heure) {
        if (cabinet == null) {
            throw new IllegalStateException("Cabinet non défini");
        }
        
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ");
        }

        for (Rdv rdv : cabinet.getListeRdv()) {
            if (rdv.getMedecin().equals(this) && 
                rdv.getDate().equals(date) && 
                rdv.getHeure().equals(heure)) {
                return false;
            }
        }
        return true;
    }
}