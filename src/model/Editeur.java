package model;

public class Editeur {
    private int idEditeur;
    private String nom;
    private String adresse; 
    
    public Editeur(int idEditeur, String nom, String adresse) {
        this.idEditeur = idEditeur;
        this.nom = nom;
        this.adresse = adresse;
    }

    public int getIdEditeur() {
        return idEditeur;
    }

    public void setIdEditeur(int idEditeur) {
        this.idEditeur = idEditeur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
