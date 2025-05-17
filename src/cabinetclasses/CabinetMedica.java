package cabinetclasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CabinetMedica {
    private List<Medecin> listeMed;
    private List<Patient> listePatient;
    private List<Rdv> listeRdv;
    private int dernierIdRdv;

    // Constructeur pour initialiser les listes
    public CabinetMedica() {
        this.listeMed = new ArrayList<>();
        this.listePatient = new ArrayList<>();
        this.listeRdv = new ArrayList<>();
        this.dernierIdRdv = 0;
    }

    // Méthode pour ajouter un médecin à la liste
    public void ajouterMed(Medecin medecin) {
        if (medecin == null) {
            throw new IllegalArgumentException("Le médecin ne peut pas être null");
        }
        // Vérifier si le médecin existe déjà
        if (listeMed.stream().anyMatch(m -> m.equals(medecin))) {
            throw new IllegalArgumentException("Ce médecin existe déjà dans le cabinet");
        }
        medecin.setCabinet(this);
        this.listeMed.add(medecin);
        System.out.println("Médecin " + medecin.getNom() + " ajouté avec succès.");
    }

    // Méthode pour ajouter un patient à la liste
    public void ajouterPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Le patient ne peut pas être null");
        }
        // Vérifier si le patient existe déjà
        if (listePatient.stream().anyMatch(p -> p.equals(patient))) {
            throw new IllegalArgumentException("Ce patient existe déjà dans le cabinet");
        }
        this.listePatient.add(patient);
        System.out.println("Patient " + patient.getNom() + " ajouté avec succès.");
    }

    // Méthode pour ajouter un rendez-vous
    public void ajouterRdv(Rdv rdv) {
        if (rdv == null) {
            throw new IllegalArgumentException("Le rendez-vous ne peut pas être null");
        }
        
        // Validation de la date et l'heure
        try {
            LocalDate.parse(rdv.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime.parse(rdv.getHeure(), DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date ou d'heure invalide");
        }
        
        // Vérifier si le médecin est disponible
        for (Rdv rdvExistant : listeRdv) {
            if (rdvExistant.getMedecin().equals(rdv.getMedecin()) && 
                rdvExistant.getDate().equals(rdv.getDate()) && 
                rdvExistant.getHeure().equals(rdv.getHeure())) {
                throw new IllegalStateException("Le médecin n'est pas disponible à cette date et heure");
            }
        }
        
        rdv.setId(++dernierIdRdv);
        this.listeRdv.add(rdv);
    }

    // Méthode pour supprimer un rendez-vous
    public void supprimerRdv(int idRdv) {
        boolean removed = listeRdv.removeIf(rdv -> rdv.getId() == idRdv);
        if (!removed) {
            throw new IllegalArgumentException("Rendez-vous non trouvé avec l'ID: " + idRdv);
        }
    }

    // Méthode pour modifier un rendez-vous
    public void modifierRdv(int idRdv, String nouvelleDate, String nouvelleHeure) {
        Rdv rdv = listeRdv.stream()
                         .filter(r -> r.getId() == idRdv)
                         .findFirst()
                         .orElseThrow(() -> new IllegalArgumentException("Rendez-vous non trouvé"));
        
        // Validation du nouveau format de date et heure
        try {
            LocalDate.parse(nouvelleDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime.parse(nouvelleHeure, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date ou d'heure invalide");
        }
        
        // Vérifier la disponibilité pour la nouvelle date/heure
        for (Rdv rdvExistant : listeRdv) {
            if (rdvExistant.getId() != idRdv && 
                rdvExistant.getMedecin().equals(rdv.getMedecin()) && 
                rdvExistant.getDate().equals(nouvelleDate) && 
                rdvExistant.getHeure().equals(nouvelleHeure)) {
                throw new IllegalStateException("Le médecin n'est pas disponible à cette date et heure");
            }
        }
        
        rdv.setDate(nouvelleDate);
        rdv.setHeure(nouvelleHeure);
    }

    // Méthode pour afficher les rendez-vous
    public void afficherRdv() {
        if (this.listeRdv.isEmpty()) {
            System.out.println("Aucun rendez-vous à afficher.");
            return;
        }

        // Trier les rendez-vous par date et heure
        listeRdv.sort(Comparator.comparing(Rdv::getDate).thenComparing(Rdv::getHeure));

        System.out.println("\nListe des rendez-vous:");
        System.out.println("----------------------------------------");
        for (Rdv rdv : listeRdv) {
            System.out.printf("RDV #%d - %s à %s\n", 
                rdv.getId(), rdv.getDate(), rdv.getHeure());
            System.out.printf("Médecin: Dr. %s %s (%s)\n", 
                rdv.getMedecin().getNom(), 
                rdv.getMedecin().getPrenom(),
                rdv.getMedecin().getSpecialite());
            System.out.printf("Patient: %s %s\n",
                rdv.getPatient().getNom(),
                rdv.getPatient().getPrenom());
            System.out.println("----------------------------------------");
        }
    }

    // Méthode pour obtenir les rendez-vous d'un médecin triés par date
    public List<Rdv> getRdvMedecin(Medecin medecin) {
        List<Rdv> rdvsMedecin = new ArrayList<>();
        for (Rdv rdv : listeRdv) {
            if (rdv.getMedecin().equals(medecin)) {
                rdvsMedecin.add(rdv);
            }
        }
        rdvsMedecin.sort(Comparator.comparing(Rdv::getDate).thenComparing(Rdv::getHeure));
        return rdvsMedecin;
    }

    // Getters
    public List<Medecin> getListeMed() {
        return new ArrayList<>(listeMed); // Retourne une copie de la liste
    }

    public List<Patient> getListePatient() {
        return new ArrayList<>(listePatient); // Retourne une copie de la liste
    }

    public List<Rdv> getListeRdv() {
        return new ArrayList<>(listeRdv); // Retourne une copie de la liste
    }

    // Méthode pour vérifier si un médecin est disponible à une date et heure données
    public boolean estDisponible(Medecin medecin, String date, String heure) {
        if (medecin == null) {
            throw new IllegalArgumentException("Le médecin ne peut pas être null");
        }
        
        // Validation du format de date et heure
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime.parse(heure, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date ou d'heure invalide");
        }
        
        // Vérifier s'il n'y a pas déjà un rendez-vous à cette date et heure
        return listeRdv.stream()
            .noneMatch(rdv -> rdv.getMedecin().equals(medecin) && 
                             rdv.getDate().equals(date) && 
                             rdv.getHeure().equals(heure));
    }
}