package util;

import java.util.Random;

public class GenerateMatricule {

	public static String generateMatricule() {
		
		String string = "ABCDEFGHIJKLMOPQRSTUVWXYZ";
        String number = "0123456789";
        String all = string + number;
        int taill = 8;
        StringBuilder matricule = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < taill; i++) {
            int index = random.nextInt(all.length());
            matricule.append(all.charAt(index));
        }
        return matricule.toString();
	}
}


