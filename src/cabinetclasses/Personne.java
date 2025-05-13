package cabinetclasses;

public class Personne {
    private String nom;
    private String prenom;
    private String email;

    // Constructeur
    public Personne(String nom, String prenom, String email) {
        setNom(nom);
        setPrenom(prenom);
        setEmail(email);
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        this.nom = nom.trim();
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }
        this.prenom = prenom.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        ValidationUtils.validerEmail(email);
        this.email = email.trim();
    }
}