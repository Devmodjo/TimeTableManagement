package util;


/*
 *  Class permettant de gerer facilemetn les ligne pour la consultation des enmploie de temps 
 *  par classe
 */

public class LigneEmploiTemps {
    private String classe;
    private String disponibilite;
    private String nombre;

    public LigneEmploiTemps(String classe, String disponibilite, String nombre) {
        this.classe = classe;
        this.disponibilite = disponibilite;
        this.nombre = nombre;
    }

    public String getClasse() {
        return classe;
    }
    
    public void setClasse(String classname) {
    	this.classe = classname;
    }

    public String getDisponibilite() {
        return disponibilite;
    }
    
    public void setDisponibilite(String disponibilite) {
    	this.disponibilite = disponibilite;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
    	this.nombre = nombre;
    }
}