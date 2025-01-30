package model;

import java.util.Date;
import observateur.Observer;

public class Membre implements Observer {
    private int ID_Membre;
    private String Nom;
    private String Prenom;
    private String Email;
    private Date Date_Inscription;

    public Membre() {
        // Constructeur par défaut
    }
    public Membre(int ID_Membre, String Nom, String Prenom, String Email, Date Date_Inscription) {
        this.ID_Membre = ID_Membre;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Email = Email;
        this.Date_Inscription = Date_Inscription;
    }

    public int getID_Membre() {
        return ID_Membre;
    }

    public void setID_Membre(int ID_Membre) {
        this.ID_Membre = ID_Membre;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public Date getDate_Inscription() {
        return Date_Inscription;
    }

    public void setDate_Inscription(Date Date_Inscription) {
        this.Date_Inscription = Date_Inscription;
    }

    @Override
    public String toString() {
        return this.Nom + " " + this.Prenom; 
    }

    @Override
    public void update() {
        
    }
}
