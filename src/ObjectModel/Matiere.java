package ObjectModel;

public class Matiere {
	
	private Integer idMatiere;
	private String nomMatiere;
	private String nomClasse;
	
	public static class MatiereBuilder{
		private Integer idMatiere;
		private String nomMatiere = "non definie";
		private String nomClasse = "non definie";
		
		public MatiereBuilder withIdMatiere(int idMatiere) {
			this.idMatiere = idMatiere;
			return this;
		}
		
		public MatiereBuilder withNomMatiere(String nomMatiere) {
			this.nomMatiere = nomMatiere;
			return this;
		}
		
		public MatiereBuilder withNomClasse(String nomClasse) {
			this.nomClasse = nomClasse;
			return this;
		}
		
		// construction de l'objet finale
		public Matiere build() {
			Matiere mat = new Matiere();
			mat.idMatiere = idMatiere;
			mat.nomMatiere = nomMatiere;
			mat.setNomClasse(nomClasse);
			return mat;
		}
	}

	
	// getter and setter
	public Integer getIdMatiere() {
		return idMatiere;
	}

	public void setIdMatiere(Integer idMatiere) {
		this.idMatiere = idMatiere;
	}

	public String getNomMatiere() {
		return nomMatiere;
	}

	public void setNomMatiere(String nomMatiere) {
		this.nomMatiere = nomMatiere;
	}

	public String getNomClasse() {
		return nomClasse;
	}

	public void setNomClasse(String nomClasse) {
		this.nomClasse = nomClasse;
	}

}
