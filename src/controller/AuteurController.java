package controller;

import dao.AuteurDAO;
import model.Auteur;
import observateur.Subject;

import java.util.List;

public class AuteurController extends Subject {
    private final AuteurDAO auteurDAO;

    public AuteurController(AuteurDAO auteurDAO) {
        this.auteurDAO = auteurDAO;
    }

    public List<Auteur> getAllAuteurs() {
        return auteurDAO.getAuteurs();
    }

    public boolean ajouterAuteur(Auteur auteur) {
        boolean success = auteurDAO.ajouterAuteur(auteur);
        if (success) {
            notifyObservers(); 
        }
        return success;
    }

    public boolean supprimerAuteur(int idAuteur) {
        boolean success = auteurDAO.supprimerAuteur(idAuteur);
        if (success) {
            notifyObservers(); 
        }
        return success;
    }

    public boolean mettreAJourAuteur(Auteur auteur) {
        boolean success = auteurDAO.mettreAJourAuteur(auteur);
        if (success) {
            notifyObservers();
        }
        return success;
    }
}
