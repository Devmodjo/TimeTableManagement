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

	public static void printByClass(String classname, String schoolYear ,String path) throws IOException, DocumentException{
	
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
		
		/* processus d'impression d'emploie de temps */
		
		// creation et ouverture du document
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(path));
		document.open();
		
		// police
		Font font  = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
		Font font2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
		
		// cree tableau a 4 dimension
		 PdfPTable table = new PdfPTable(new float[]{2, 3, 4, 2});
         table.setWidthPercentage(100);
         
         // entete
         table.addCell(new PdfPCell(new Phrase("Jour", font)));
         table.addCell(new PdfPCell(new Phrase("Horaire", font)));
         table.addCell(new PdfPCell(new Phrase("Discipline", font)));
         table.addCell(new PdfPCell(new Phrase("Durée", font)));
         
		
	}
	
	
	public static String[][] convertToArray(List<String> listJournalière) {
		int jours = listJournalière.size();
		String[][] tableau = new String[jours][];
		
		for(int i=0; i<jours; i++) {
			String line = listJournalière.get(i);
			String[] matJour = line.split(";");
			tableau[i] = matJour;
			
			
		}
		return null; 
	}
	
}
