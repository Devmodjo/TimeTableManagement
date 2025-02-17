package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    
    public static boolean isValidEmail(String email) {
        // Expression régulière pour valider l'email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        
        // Création du pattern à partir de l'expression régulière
        Pattern pattern = Pattern.compile(emailRegex);
        
        // Vérification si l'email correspond au pattern
        Matcher matcher = pattern.matcher(email);
        
        return matcher.matches();
    }

    public static void main(String[] args) {
        // Test de la fonction
        String email = "test@example.com";
        
        if (isValidEmail(email)) {
            System.out.println("L'email est valide.");
        } else {
            System.out.println("L'email n'est pas valide.");
        }
    }
}
