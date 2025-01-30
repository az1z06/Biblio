package controller;

import dao.EditeurDAO;
import model.Editeur;
import observateur.Subject;

import java.util.List;

public class EditeurController extends Subject{

    private EditeurDAO editeurDAO;

    public EditeurController(EditeurDAO editeurDAO) {
        this.editeurDAO = editeurDAO;
    }

    public List<Editeur> getAllEditeurs() {
        return editeurDAO.getAllEditeurs();
    }

    public boolean ajouterEditeur(Editeur editeur) {
    	boolean success = editeurDAO.ajouterEditeur(editeur);
        if (success) {
            notifyObservers(); // Notifie les observateurs après l'ajout
        }
        return success;
    }

    public boolean modifierEditeur(Editeur editeur) {
    	boolean success = editeurDAO.modifierEditeur(editeur);
        if (success) {
            notifyObservers(); // Notifie les observateurs après la modification
        }
        return success;
    }

    public boolean supprimerEditeur(int id) {
    	boolean success = editeurDAO.supprimerEditeur(id);
        if (success) {
            notifyObservers(); // Notifie les observateurs après la suppression
        }
        return success;
    }
}
