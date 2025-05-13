package cabinetclasses;

public class Patient extends Personne {
    private int age;
    private String historique;

    public Patient(String nom, String prenom, String email, int age, String historique) {
        super(nom, prenom, email);
        this.age = age;
        this.historique = historique;
    }

    // Méthode pour enregistrer les informations personnelles
    public void enregistrerInfo(String historique) {
        this.historique = historique;
        System.out.println("Informations mises à jour pour le patient " + getNom());
    }

    // Méthode pour demander un changement (nécessite une validation)
    public void demanderChangement(String nouveauHistorique) {
        System.out.println("Demande de changement d'historique envoyée pour " + getNom());
    }

    // Getters et Setters
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHistorique() {
        return historique;
    }

    public void setHistorique(String historique) {
        this.historique = historique;
    }
}
