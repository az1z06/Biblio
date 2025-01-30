package controller;

import composite.CategorieComposite;
import dao.CategorieDAO;
import dao.LivreDAO;
import observateur.Subject;

public class CategorieController  extends Subject{

    private CategorieDAO categorieDAO;
    private LivreDAO livreDAO;

    public CategorieController(CategorieDAO categorieDAO, LivreDAO livreDAO) {
        this.categorieDAO = categorieDAO;
        this.livreDAO = livreDAO; // Si besoin d'utiliser des interactions avec Livre dans l'avenir
    }

    /**
     * Construit l'arbre des catégories à partir des données de la base.
     *
     * @return L'arbre des catégories sous forme d'un CategorieComposite.
     */
    public CategorieComposite construireArbreCategoriesAvecLivres() {
        return categorieDAO.construireArbreCategoriesAvecLivres(livreDAO);
    }


    /**
     * Construit l'arbre des catégories à partir des données de la base.
     *
     * @return L'arbre des catégories sous forme d'un CategorieComposite.
     */
    public CategorieComposite construireArbreCategories() {
        return categorieDAO.construireArbreCategories();
    }
    /**
     * Ajoute une nouvelle catégorie dans la base de données.
     *
     * @param nom      Le nom de la nouvelle catégorie.
     * @param parentId L'ID du parent de la catégorie, ou null si la catégorie est à la racine.
     */
    public void ajouterCategorie(String nom, Integer parentId) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la catégorie ne peut pas être vide.");
        }

        CategorieComposite nouvelleCategorie = new CategorieComposite(0, nom);
        
        // Vérifie si un parent est spécifié
        if (parentId != null && parentId > 0) {
            nouvelleCategorie.setParent(new CategorieComposite(parentId, "Parent Temporaire")); // Parent temporaire avec un nom valide
        }

        categorieDAO.ajouterCategorie(nouvelleCategorie);
        notifyObservers();
    }

 
    // Supprime une catégorie existante par son ID.
    public void supprimerCategorie(int idCategorie) {
        if (idCategorie <= 0) {
            throw new IllegalArgumentException("L'ID de la catégorie est invalide.");
        }
        categorieDAO.supprimerCategorie(idCategorie);
        notifyObservers(); // Notifie les vues après la suppression
    }
}
