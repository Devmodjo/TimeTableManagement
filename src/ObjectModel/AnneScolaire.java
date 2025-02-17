package ObjectModel;

public class AnneScolaire {

	private Integer idAnneeScolaire;
	private Integer anneeScolaire;
	
	
	/* implementation du design pattern builder */
	public static class anneeScolaireBuilder{
		private Integer idAnneeScolaire;
		private Integer anneeScolaire;
		
		
		public anneeScolaireBuilder withIdAnneScolaire(Integer idAnneeScolaire) {
			this.idAnneeScolaire = idAnneeScolaire;
			return this;
		}
		
		public anneeScolaireBuilder withAnneScolaire(Integer anneeScolaire) {
			this.anneeScolaire = anneeScolaire;
			return this;
		}
		
		
		/* construction de l'objet final */
		public AnneScolaire build() {
			AnneScolaire ann = new AnneScolaire();
			ann.idAnneeScolaire = idAnneeScolaire;
			ann.anneeScolaire = anneeScolaire;
			return ann;
		}
	}
	
	// getter an setter
	public Integer getIdAnneeScolaire() {
		return idAnneeScolaire;
	}
	public void setIdAnneeScolaire(Integer idAnneeScolaire) {
		this.idAnneeScolaire = idAnneeScolaire;
	}
	public Integer getAnneeScolaire() {
		return anneeScolaire;
	}
	public void setAnneeScolaire(Integer anneeScolaire) {
		this.anneeScolaire = anneeScolaire;
	}
}
