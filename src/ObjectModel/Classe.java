package ObjectModel;

public class Classe {
	
	private Integer idClasse;
	private String nomClasse;
	private String cycle; // premier/second cycle
	
	public static class ClasseBuilder{
		private Integer idClasse;
		private String nomClasse = "non definie";
		private String cycle = "non definie";
		
		public ClasseBuilder withIdClasse(int idClasse) {
			this.idClasse = idClasse;
			return this;
		}
		
		public ClasseBuilder withNomClasse(String nomClasse) {
			this.nomClasse = nomClasse;
			return this;
		}
		
		public ClasseBuilder withCycle(String cycle) {
			this.cycle = cycle;
			return this;
		}
		
		// construction de l'objet finale
		public Classe build() {
			Classe classe = new Classe();
			classe.idClasse = idClasse;
			classe.nomClasse = nomClasse;
			classe.cycle = cycle;
			return classe;
		}
		
	}
	
	// getter and setter
	public Integer getIdClasse() {
		return idClasse;
	}

	public void setIdClasse(Integer idClasse) {
		this.idClasse = idClasse;
	}

	public String getNomClasse() {
		return nomClasse;
	}

	public void setNomClasse(String nomClasse) {
		this.nomClasse = nomClasse;
	}

	public String getCyle() {
		return cycle;
	}

	public void setCyle(String cyle) {
		this.cycle = cyle;
	}
	
	

}
