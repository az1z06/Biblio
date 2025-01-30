package model;

import java.sql.Date;

public class Auteur {
    private int ID_Auteur;
    private String Nom;
    private String Prenom;
    private Date Date_Naissance;

    public Auteur(int ID_Auteur, String Nom, String Prenom, Date Date_Naissance) {
        this.ID_Auteur = ID_Auteur;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Date_Naissance = Date_Naissance;
    }

    public int getID_Auteur() {
        return ID_Auteur;
    }

    public void setID_Auteur(int ID_Auteur) {
        this.ID_Auteur = ID_Auteur;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String Prenom) {
        this.Prenom = Prenom;
    }

    public Date getDate_Naissance() {
        return Date_Naissance;
    }

    public void setDate_Naissance(Date Date_Naissance) {
        this.Date_Naissance = Date_Naissance;
    }
}
