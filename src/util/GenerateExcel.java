package util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class GenerateExcel {
    public static void printExcel(Map<String, String[][]> emploisParClasse, String cheminFichier) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Emploi du Temps");

            String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
            String[] heures = {"1ère", "2e", "3e", "4e", "5e", "6e", "7e"};

            // Styles
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Création des entêtes
            Row entete = sheet.createRow(0);
            entete.createCell(0).setCellValue("Jour");
            entete.getCell(0).setCellStyle(headerStyle);
            entete.createCell(1).setCellValue("Heure");
            entete.getCell(1).setCellStyle(headerStyle);

            int colIndex = 2;
            List<String> classesTriees = new ArrayList<>(emploisParClasse.keySet());
            Collections.sort(classesTriees);

            for (String classe : classesTriees) {
                Cell cell = entete.createCell(colIndex++);
                cell.setCellValue(classe);
                cell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;
            for (int jour = 0; jour < jours.length; jour++) {
                // Fusion de cellule pour le jour
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + heures.length - 1, 0, 0));
                for (int h = 0; h < heures.length; h++) {
                    Row row = sheet.createRow(rowIndex);

                    if (h == 0) {
                        Cell jourCell = row.createCell(0);
                        jourCell.setCellValue(jours[jour]);
                        jourCell.setCellStyle(cellStyle);
                    }

                    Cell heureCell = row.createCell(1);
                    heureCell.setCellValue(heures[h]);
                    heureCell.setCellStyle(cellStyle);

                    colIndex = 2;
                    for (String classe : classesTriees) {
                        String[][] emploi = emploisParClasse.get(classe);
                        String matiere = emploi[jour][h];
                        Cell cell = row.createCell(colIndex++);
                        cell.setCellValue(matiere != null ? matiere : "");
                        cell.setCellStyle(cellStyle);
                    }
                    rowIndex++;
                }
            }

            for (int i = 0; i < colIndex; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(cheminFichier)) {
                workbook.write(fos);
            }

            new DialogBox().infoAlertBox("SUCCESS", "Fichier Excel généré avec succès : " + cheminFichier);
        } catch (IOException e) {
            e.printStackTrace();
            new DialogBox().errorAlertBox("ERREUR", "Erreur lors de la génération du fichier Excel : " + e.getMessage());
        }
    }
}
