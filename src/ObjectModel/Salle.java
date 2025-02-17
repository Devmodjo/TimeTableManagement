package ObjectModel;

public class Salle {

	/* implementation du design pattern builder */
	private Integer idSalle;
	private Integer capacite;
	private String nomSalle;
	
	public static class SalleBuilder {
		
		private Integer idSalle;
		private Integer capacite = null;
		private String nomSalle = "non definie";
		
		public SalleBuilder withIdSalle(Integer idSalle) {
			this.idSalle = idSalle;
			return this;
		}
		
		public SalleBuilder withCapacite(Integer capacite) {
			this.capacite = capacite;
			return this;
		}
		
		public SalleBuilder withNomSalle(String nomSalle) {
			this.nomSalle = nomSalle;
			return this;
		}
		
		public Salle build() {
			Salle salle = new Salle();
			salle.idSalle = idSalle;
			salle.capacite = capacite;
			salle.nomSalle = nomSalle;
			return salle;
		}
		
	}

	// getter and setter
	public Integer getIdSalle() {
		return idSalle;
	}

	public void setIdSalle(Integer idSalle) {
		this.idSalle = idSalle;
	}

	public String getNomSalle() { 
		return nomSalle;
	}

	public void setNomSalle(String nomSalle) {
		this.nomSalle = nomSalle;
	}

	public Integer getCapacite() {
		return capacite;
	}

	public void setCapacite(Integer capacite) {
		this.capacite = capacite;
	}
}
