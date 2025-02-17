package ObjectModel;
import java.util.ArrayList;
import java.time.LocalTime;

public class Cour {
	
	private Integer idCour;
	private LocalTime heureDebut;
	private LocalTime heureFin;
	private ArrayList<String> lundiCour;
	private ArrayList<String> mardiCour;
	private ArrayList<String> mercrediCour;
	private ArrayList<String> jeudiCour;
	private ArrayList<String> vendrediCour;
	
	// implementation du design pattern builder
	public static class CourBuilder{
		
		private Integer idCour;
		private LocalTime heureDebut;
		private LocalTime heureFin;
		private ArrayList<String> lundiCour;
		private ArrayList<String> mardiCour;
		private ArrayList<String> mercrediCour;
		private ArrayList<String> jeudiCour;
		private ArrayList<String> vendrediCour;
		
		public CourBuilder withIdCour(int idCour) {
			this.idCour = idCour;
			return this;
		}
		
		public CourBuilder withHeureDebut(LocalTime heureDebut) {
			this.heureDebut = heureDebut;
			return this;
		}
		
		public CourBuilder withHeureFin(LocalTime heureFin) {
			this.heureFin = heureFin;
			return this;
		}
		
		public CourBuilder withLundiCour(ArrayList<String> lundiCour) {
			this.lundiCour = lundiCour;
			return this;
		}
		
		public CourBuilder withMardiCour(ArrayList<String> mardiCour) {
			this.mardiCour = mardiCour;
			return this;
		}
		
		public CourBuilder withMercrediCour(ArrayList<String> mercrediCour) {
			this.mercrediCour = mercrediCour;
			return this;
		}
		
		public CourBuilder withJeudiCour(ArrayList<String> jeudiCour) {
			this.jeudiCour = jeudiCour;
			return this;
		}
		
		public CourBuilder withVendrediCour(ArrayList<String> vendrediCour) {
			this.vendrediCour = vendrediCour;
			return this;
		}
		
		// consrtuction de l'objet final
		public Cour build(){
			Cour c = new Cour();
			c.idCour = idCour;
			c.heureDebut = heureDebut;
			c.heureFin = heureFin;
			c.lundiCour = lundiCour;
			c.mardiCour = mardiCour;
			c.mercrediCour = mercrediCour;
			c.jeudiCour = jeudiCour;
			c.vendrediCour = vendrediCour;
			return c;
		}
	}
	
	// getter and setter
	public ArrayList<String> getLundiCour() {
		return lundiCour;
	}
	public void setLundiCour(ArrayList<String> lundiCour) {
		this.lundiCour = lundiCour;
	}
	public ArrayList<String> getMardiCour() {
		return mardiCour;
	}
	public void setMardiCour(ArrayList<String> mardiCour) {
		this.mardiCour = mardiCour;
	}
	public ArrayList<String> getMercrediCour() {
		return mercrediCour;
	}
	public void setMercrediCour(ArrayList<String> mercrediCour) {
		this.mercrediCour = mercrediCour;
	}
	public ArrayList<String> getJeudiCour() {
		return jeudiCour;
	}
	public void setJeudiCour(ArrayList<String> jeudiCour) {
		this.jeudiCour = jeudiCour;
	}
	public ArrayList<String> getVendrediCour() {
		return vendrediCour;
	}
	public void setVendrediCour(ArrayList<String> vendrediCour) {
		this.vendrediCour = vendrediCour;
	}
	public Integer getIdCour() {
		return idCour;
	}
	public void setIdCour(Integer idCour) {
		this.idCour = idCour;
	}
	public LocalTime getHeureDebut() {
		return heureDebut;
	}
	public void setHeureDebut(LocalTime heureDebut) {
		this.heureDebut = heureDebut;
	}
	public LocalTime getHeureFin() {
		return heureFin;
	}
	public void setHeureFin(LocalTime heureFin) {
		this.heureFin = heureFin;
	}
	

}
