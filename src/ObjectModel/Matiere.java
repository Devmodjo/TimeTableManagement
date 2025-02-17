package ObjectModel;

public class Matiere {
	
	private Integer idMatiere;
	private String nomMatiere;
	
	public static class MatiereBuilder{
		private Integer idMatiere;
		private String nomMatiere = "non definie";
		
		public MatiereBuilder withIdMatiere(int idMatiere) {
			this.idMatiere = idMatiere;
			return this;
		}
		
		public MatiereBuilder withNomMatiere(String nomMatiere) {
			this.nomMatiere = nomMatiere;
			return this;
		}
		
		// construction de l'objet finale
		public Matiere build() {
			Matiere mat = new Matiere();
			mat.idMatiere = idMatiere;
			mat.nomMatiere = nomMatiere;
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

}
