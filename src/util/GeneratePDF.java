package util;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class GeneratePDF {

	public static void printByClass(String classname, String schoolYear, List<String>listJournaliere  ,String path) throws IOException, DocumentException{
	
		final String[] listHoraire = new String[] {
				"7h30 - 8h30", 
				"8h30 - 9h30",
				"9h30 - 10h30", 
				"10h30 - 11h30",
				"11h30 - 12h30",
				"12h30 - 13h30", 
				"13h30 - 14h40"
		};
		final String duree = "1h";
		final String[] listDay = { "lundi",  "mardi", "mercredi", "jeudi", "vendredi"};
		
		// convertir l'arrayList en tableau 2D
		int jours = listJournaliere.size();
		String[][] tableau = new String[jours][];
		
		for(int i=0; i<jours; i++) {
			String line = listJournaliere.get(i);
			String[] matJour = line.split(";");
			tableau[i] = matJour;
			
		}
		
		// creation et ouverture du document
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(path));
		document.open();
		
		// police
		Font font  = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
		Font font2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
		Font font3 = FontFactory.getFont(FontFactory.COURIER_BOLD, 17, Font.BOLD);
		
		 // cree tableau a 4 dimension
		 PdfPTable table = new PdfPTable(new float[]{2, 3, 4, 2});
         table.setWidthPercentage(100);
         
         // titre du document
         Paragraph header = new Paragraph("EMPLOI DE TEMPS DE LA " + classname + " année scolaire : " + schoolYear, font3);
         header.setSpacingAfter(10);
         document.add(header);
         
         // entete
         table.addCell(new PdfPCell(new Phrase("Jour", font)));
         table.addCell(new PdfPCell(new Phrase("Horaire", font)));
         table.addCell(new PdfPCell(new Phrase("Discipline", font)));
         table.addCell(new PdfPCell(new Phrase("Durée", font)));
         
         
         // processus de generation et d'impression du document
         for(int j=0; j<tableau.length; j++) {
        	 // affichage des jours
        	 //table.addCell(new PdfPCell(new Phrase(listDay[j], font2)));
        	 String[] matiere = tableau[j];
        	 PdfPCell jourCell = new PdfPCell(new Phrase(listDay[j], font));
        	 jourCell.setRowspan(matiere.length);
        	 table.addCell(jourCell);
        	 
        	 for(int k=0; k < tableau[j].length; k++) {
        		 // matiere
                 table.addCell(new PdfPCell(new Phrase(listHoraire[k], font2))); // horaire
                 table.addCell(new PdfPCell(new Phrase(tableau[j][k], font2))); // discipline
                 table.addCell(new PdfPCell(new Phrase(duree, font2))); // duree
        	 }
         }
         
         document.add(table);
         document.close();
	}
	
	
}
