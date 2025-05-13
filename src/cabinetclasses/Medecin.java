package cabinetclasses;

import java.util.List;

public class Medecin extends Personne {
    private String specialite;
    private List<Patient> listePatient;

    public Medecin(String nom, String prenom, String email, String specialite) {
        super(nom, prenom, email);
        this.specialite = specialite;
    }

    // Méthode pour consulter le planning
    public void consulterPlanning() {
        // Affiche le planning des rendez-vous (exemple simple)
        System.out.println("Planning pour " + getNom() + " " + getPrenom() + " (Spécialité: " + specialite + ")");
    }

    // Méthode pour vérifier l'historique médical d'un patient
    public void verifierHistorique(Patient patient) {
        // Affiche l'historique médical du patient
        System.out.println("Historique médical de " + patient.getNom() + ": " + patient.getHistorique());
    }

    // Getters et Setters
    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public List<Patient> getListePatient() {
        return listePatient;
    }

    public void setListePatient(List<Patient> listePatient) {
        this.listePatient = listePatient;
    }
}