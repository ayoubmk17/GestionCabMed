package cabinetclasses;

import java.util.Date;

public class Rdv {
    private int id;
    private String date;
    private String heure;
    private Medecin medecin;
    private Patient patient;

    public Rdv(int id, String date, String heure, Medecin medecin, Patient patient) {
        this.id = id;
        this.date = date;
        this.heure = heure;
        this.medecin = medecin;
        this.patient = patient;
    }

    // Getters et Setters
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
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}