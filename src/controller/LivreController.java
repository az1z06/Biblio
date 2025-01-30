package controller;

import dao.CategorieDAO;
import dao.EditeurDAO;
import dao.LivreDAO;
import model.Livre;
import java.util.List;
import observateur.Subject;

public class LivreController extends Subject{
	private LivreDAO livreDAO;
    private CategorieDAO categorieDAO;
    private EditeurDAO editeurDAO;

    public LivreController(LivreDAO livreDAO, CategorieDAO categorieDAO, EditeurDAO editeurDAO) {
        this.livreDAO = livreDAO;
        this.categorieDAO = categorieDAO;
        this.editeurDAO = editeurDAO;
    }

    public boolean ajouterLivre(Livre livre) {
        if (livre.getTitre() == null || livre.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide.");
        }
        boolean success = livreDAO.ajouterLivre(livre);
        if (success) {
            notifyObservers();
        }
        return success;
    }

    public List<Livre> getAllLivres() {
        return livreDAO.getLivres();
    }

    public List<Livre> rechercherLivres(String query) {
        return livreDAO.rechercherLivres(query);
    }

    public boolean mettreAJourLivre(Livre livre) {
    	boolean success = livreDAO.mettreAJourLivre(livre);
        if (success) {
            notifyObservers();
        }
        return success;    }

    public boolean supprimerLivre(int idLivre) {
    	 boolean success = livreDAO.supprimerLivre(idLivre);
         if (success) {
             notifyObservers();
         }
         return success;
    }

    public Livre getLivreById(int idLivre) {
        return livreDAO.getLivreById(idLivre);
    }
    
    public void afficherDetailsLivre(int idLivre) {
        Livre livre = livreDAO.getLivreById(idLivre);
        if (livre != null) {
            livre.afficherDetails();
        } else {
            System.out.println("Livre introuvable.");
        }
    }
 // Nouvelle méthode pour récupérer toutes les catégories
    public List<String> getAllCategories() {
        return categorieDAO.getAllCategorieNames(); // Ajoute cette méthode dans CategorieDAO
    }

    // Nouvelle méthode pour récupérer tous les éditeurs
    public List<String> getAllEditeurs() {
        return editeurDAO.getAllEditeurNames(); // Ajoute cette méthode dans EditeurDAO
    }
}
