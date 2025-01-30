package model;

import java.util.ArrayList;
import java.util.List;
import observateur.Observer;
import observateur.Subject;

public class Livre extends Subject {
    private int ID_Livre;
    private String Titre;
    private int Annee_Publication;
    private String ISBN;
    private int ID_Editeur;
    private int ID_Categorie;
    private boolean disponible;

    private String nomCategorie;
    private String nomEditeur;

 

    private List<Observer> observateurs = new ArrayList<>();

    public Livre() {
        // Constructeur par défaut
    }
    public Livre(int ID_Livre, String Titre, int Annee_Publication, String ISBN, int ID_Editeur, int ID_Categorie) {
        this.ID_Livre = ID_Livre;
        this.Titre = Titre;
        this.Annee_Publication = Annee_Publication;
        this.ISBN = ISBN;
        this.ID_Editeur = ID_Editeur;
        this.ID_Categorie = ID_Categorie;
        this.disponible = true;
    }

    public int getID_Livre() {
        return ID_Livre;
    }

    public void setID_Livre(int ID_Livre) {
        this.ID_Livre = ID_Livre;
    }

    public String getTitre() {
        return Titre;
    }

    public void setTitre(String Titre) {
        this.Titre = Titre;
    }

    public int getAnnee_Publication() {
        return Annee_Publication;
    }

    public void setAnnee_Publication(int Annee_Publication) {
        this.Annee_Publication = Annee_Publication;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getID_Editeur() {
        return ID_Editeur;
    }

    public void setID_Editeur(int ID_Editeur) {
        this.ID_Editeur = ID_Editeur;
    }

    public int getID_Categorie() {
        return ID_Categorie;
    }

    public void setID_Categorie(int ID_Categorie) {
        this.ID_Categorie = ID_Categorie;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
        if (disponible) {
            notifierDisponibilite();
        }
    }
    
    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getNomEditeur() {
        return nomEditeur;
    }

    public void setNomEditeur(String nomEditeur) {
        this.nomEditeur = nomEditeur;
    }

    @Override
    public String toString() {
        return this.Titre; 
    }

    @Override
    public void addObserver(Observer obs) {
        if (!observateurs.contains(obs)) {
            observateurs.add(obs);
        }
    }

    @Override
    public void removeObserver(Observer obs) {
        observateurs.remove(obs);
    }

    @Override
    public void notifyObservers() {
        for (Observer obs : observateurs) {
            obs.update();
        }
    }

    public void notifierDisponibilite() {
        
        notifyObservers();
    }
    
    public void afficherDetails() {
        System.out.println("Livre : " + Titre + ", Année : " + Annee_Publication);
    }
}
