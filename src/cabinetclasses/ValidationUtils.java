package cabinetclasses;

import java.util.regex.Pattern;

public class ValidationUtils {
    // Expression régulière pour valider le format d'email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    // Méthode pour valider un email
    public static boolean estEmailValide(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Méthode pour valider un email avec exception
    public static void validerEmail(String email) {
        if (!estEmailValide(email)) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
    }
} 