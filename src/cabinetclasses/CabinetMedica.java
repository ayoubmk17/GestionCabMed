package cabinetclasses;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    // Méthode pour générer un nouvel ID de rendez-vous
    private int genererIdRdv() {
        return ++dernierIdRdv;
    }

    // Méthode pour ajouter un médecin à la liste
    public void ajouterMed(Medecin medecin) {
        if (medecin == null) {
            throw new IllegalArgumentException("Le médecin ne peut pas être null");
        }
        this.listeMed.add(medecin);
        System.out.println("Médecin " + medecin.getNom() + " ajouté avec succès.");
    }

    // Méthode pour ajouter un patient à la liste
    public void ajouterPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Le patient ne peut pas être null");
        }
        this.listePatient.add(patient);
        System.out.println("Patient " + patient.getNom() + " ajouté avec succès.");
    }

    // Méthode pour vérifier la disponibilité du médecin
    private boolean estMedecinDisponible(Medecin medecin, String date, String heure) {
        try {
            LocalDateTime rdvDateTime = LocalDateTime.parse(date + " " + heure, 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            
            for (Rdv rdv : listeRdv) {
                if (rdv.getMedecin().equals(medecin)) {
                    LocalDateTime rdvExistant = LocalDateTime.parse(rdv.getDate() + " " + rdv.getHeure(), 
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    
                    // Vérifier si le rendez-vous est le même jour et à la même heure
                    if (rdvDateTime.equals(rdvExistant)) {
                        return false;
                    }
                }
            }
            return true;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date ou heure invalide");
        }
    }

    // Méthode pour ajouter un rendez-vous
    public void ajouterRdv(Rdv rdv) {
        if (rdv == null) {
            throw new IllegalArgumentException("Le rendez-vous ne peut pas être null");
        }
        
        if (!estMedecinDisponible(rdv.getMedecin(), rdv.getDate(), rdv.getHeure())) {
            throw new IllegalStateException("Le médecin n'est pas disponible à cette date et heure");
        }
        
        rdv.setId(genererIdRdv());
        this.listeRdv.add(rdv);
    }

    // Méthode pour supprimer un rendez-vous
    public void supprimerRdv(int idRdv) {
        listeRdv.removeIf(rdv -> rdv.getId() == idRdv);
    }

    // Méthode pour afficher les rendez-vous
    public void afficherRdv() {
        if (this.listeRdv.isEmpty()) {
            System.out.println("Aucun rendez-vous à afficher.");
        } else {
            for (Rdv rdv : listeRdv) {
                System.out.println("RDV #" + rdv.getId() + " avec Dr. " + rdv.getMedecin().getNom() + 
                    " pour " + rdv.getPatient().getNom() + " le " + rdv.getDate() + " à " + rdv.getHeure());
            }
        }
    }

    // Getter pour la liste des médecins
    public List<Medecin> getListeMed() {
        return listeMed;
    }

    // Getter pour la liste des patients
    public List<Patient> getListePatient() {
        return listePatient;
    }

    // Getter pour la liste des rendez-vous
    public List<Rdv> getListeRdv() {
        return listeRdv;
    }
}