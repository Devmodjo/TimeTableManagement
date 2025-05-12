package util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;


public class GenerateExcel {
	
	public static void printExcel(Map<String, String[][]> emploisParClasse, String cheminFichier) {
	    Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet("Emploi du Temps");

	    String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
	    String[] heures = {"1ère", "2e", "3e", "4e", "5e", "6e", "7e"};

	    Row entete = sheet.createRow(0);
	    entete.createCell(0).setCellValue("Jour");
	    entete.createCell(1).setCellValue("Heure");

	    int colIndex = 2;
	    List<String> classesTriees = new ArrayList<>(emploisParClasse.keySet());
	    Collections.sort(classesTriees);

	    for (String classe : classesTriees) {
	        entete.createCell(colIndex++).setCellValue(classe);
	    }

	    int rowIndex = 1;
	    for (int jour = 0; jour < jours.length; jour++) {
	        for (int h = 0; h < heures.length; h++) {
	            Row row = sheet.createRow(rowIndex++);
	            row.createCell(0).setCellValue(jours[jour]);
	            row.createCell(1).setCellValue(heures[h]);

	            colIndex = 2;
	            for (String classe : classesTriees) {
	                String[][] emploi = emploisParClasse.get(classe);
	                String matiere = emploi[jour][h];
	                row.createCell(colIndex++).setCellValue(matiere != null ? matiere : "");
	            }
	        }
	    }

	    try (FileOutputStream fos = new FileOutputStream(cheminFichier)) {
	        workbook.write(fos);
	        workbook.close();
	        new DialogBox().infoAlertBox("SUCCESS", "Fichier Excel généré avec succès : " + cheminFichier);;
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


}
