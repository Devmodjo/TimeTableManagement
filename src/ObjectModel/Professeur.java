package ObjectModel;

public class Professeur {
	
	/* implementation du design pattern builder */
	
	// field
	private String idProf;
	private String nom;
	private String prenom;
	private String discipline;
	private Integer tel;
	private String mail;
	private String grade;
	private String sexe;
	
	
	public static class ProfesseurBuilder{
		
		// field
		private String idProf = "non definie";
		private String nom = "non definie";
		private String prenom = "non definie";
		private String discipline = "non definie";
		private Integer tel = null;
		private String mail = "non definie";
		private String grade = "non definie";
		private String sexe = "non definie";
		
		// affectation de chaque attribute a une methode permettant de leur afffecter des valeur
		
		public ProfesseurBuilder withIdProf(String idProf) {
			this.idProf = idProf;
			return this;
		}
		
		public ProfesseurBuilder withNomprof(String nom_prof) {
			this.nom = nom_prof;
			return this;
		}
		
		public ProfesseurBuilder withPrenomprof(String prenom_prof) {
			this.prenom = prenom_prof;
			return this;
		}
		
		public ProfesseurBuilder withDiscipline(String discipline) {
			this.discipline = discipline;
			return this;
		}
		
		public ProfesseurBuilder withGrade(String grade) {
			this.grade = grade;
			return this;
		}
		
		public ProfesseurBuilder withSexe(String sexe) {
			this.sexe = sexe;
			return this;
		}
		public ProfesseurBuilder withTel(Integer tel) {
			this.tel = tel;
			return this;
		}
		public ProfesseurBuilder withMail(String mail) {
			this.mail = mail;
			return this;
		}
		
		// methode permettant de construire l'objet final 
		public Professeur build() {
			
			Professeur prof = new Professeur();
			prof.idProf = idProf;
			prof.nom = nom;
			prof.prenom = prenom;
			prof.tel = tel;
			prof.mail = mail;
			prof.discipline = discipline;
			prof.grade = grade;
			prof.sexe = sexe;
			
			return prof;
		}
	}
	
	
	// getter and setter
	public String getNomprof() {
		return nom;
	}
	
	public String getPrenomprof() {
		return prenom;
	}
	
	public String getDiscipline() {
		return discipline;
	}
	
	public String getGrade() {
		return grade;
	}
	
	public String getSexe() {
		return sexe;
	}
	
	public String getIdProf() {
		return idProf;
	}

	public Integer getTel() {
		return tel;
	}
	
	public String getMail() {
		return mail;
	}
	

}
