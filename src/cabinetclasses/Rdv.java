package cabinetclasses;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Rdv implements Comparable<Rdv> {
    private int id;
    private String date;
    private String heure;
    private Medecin medecin;
    private Patient patient;

    public Rdv(int id, String date, String heure, Medecin medecin, Patient patient) {
        if (medecin == null) {
            throw new IllegalArgumentException("Le médecin ne peut pas être null");
        }
        if (patient == null) {
            throw new IllegalArgumentException("Le patient ne peut pas être null");
        }
        
        // Validation du format de la date
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ");
        }

        // Validation du format de l'heure
        try {
            LocalTime.parse(heure, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Format d'heure invalide. Utilisez HH:MM");
        }

        this.id = id;
        this.date = date;
        this.heure = heure;
        this.medecin = medecin;
        this.patient = patient;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rdv rdv = (Rdv) obj;
        return id == rdv.id &&
               date.equals(rdv.date) &&
               heure.equals(rdv.heure) &&
               medecin.equals(rdv.medecin) &&
               patient.equals(rdv.patient);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + date.hashCode();
        result = 31 * result + heure.hashCode();
        result = 31 * result + medecin.hashCode();
        result = 31 * result + patient.hashCode();
        return result;
    }

    @Override
    public int compareTo(Rdv other) {
        int dateComparison = this.date.compareTo(other.date);
        if (dateComparison != 0) {
            return dateComparison;
        }
        return this.heure.compareTo(other.heure);
    }

    @Override
    public String toString() {
        return String.format("RDV #%d - %s à %s - Dr. %s avec %s %s",
            id, date, heure, medecin.getNom(), patient.getNom(), patient.getPrenom());
    }

    // Getters et Setters avec validation
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.date = date;
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ");
        }
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        try {
            LocalTime.parse(heure, DateTimeFormatter.ofPattern("HH:mm"));
            this.heure = heure;
        } catch (Exception e) {
            throw new IllegalArgumentException("Format d'heure invalide. Utilisez HH:MM");
        }
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        if (medecin == null) {
            throw new IllegalArgumentException("Le médecin ne peut pas être null");
        }
        this.medecin = medecin;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Le patient ne peut pas être null");
        }
        this.patient = patient;
    }

    public LocalDate getLocalDate() {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public LocalTime getLocalTime() {
        return LocalTime.parse(heure, DateTimeFormatter.ofPattern("HH:mm"));
    }
}