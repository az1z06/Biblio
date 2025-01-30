package composite;

import java.util.ArrayList;
import java.util.List;

public class CategorieComposite implements ComposantCategorie {

    private int idCategorie;
    private String nom;
    private CategorieComposite parent; // Référence au parent
    private List<ComposantCategorie> enfants = new ArrayList<>();

    // Constructeur sans parent
    public CategorieComposite(int idCategorie, String nom) {
    	if (nom == null || nom.isEmpty()) {
            throw new IllegalArgumentException("Le nom de la catégorie ne peut pas être vide.");
        }
        this.idCategorie = idCategorie;
        this.nom = nom;
        this.parent = null;
    }

    // Constructeur avec parentId (parent sera défini plus tard)
    public CategorieComposite(int idCategorie, String nom, Integer parentId) {
        this.idCategorie = idCategorie;
        this.nom = nom;
        this.parent = null; // Parent réel à lier dans les méthodes du DAO
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public CategorieComposite getParent() {
        return parent;
    }

    public void setParent(CategorieComposite parent) {
        this.parent = parent;
    }

    /**
     * Méthode pour récupérer l'ID du parent.
     * Si le parent est null, retourne null.
     */
    public Integer getParentId() {
        return (parent != null) ? parent.getIdCategorie() : null;
    }

    @Override
    public void afficherStructure(String prefix) {
        System.out.println(prefix + "- " + nom); // Affichage de la catégorie

        for (ComposantCategorie enfant : enfants) {
            if (enfant instanceof CategorieComposite) {
                enfant.afficherStructure(prefix + "   "); // Ajoute l'indentation pour une sous-catégorie
            } else {
                enfant.afficherStructure(prefix + "      "); // Ajoute une indentation plus large pour un livre
            }
        }
    }


  
    @Override
    public void ajouter(ComposantCategorie composant) {
        if (composant instanceof CategorieComposite) {
            CategorieComposite categorie = (CategorieComposite) composant;
            if (this.equals(categorie) || categorie.contient(this)) { // Vérification améliorée
                throw new IllegalArgumentException("Impossible d'ajouter : Cela créerait une boucle dans l'arbre.");
            }
            categorie.setParent(this);
        }
        enfants.add(composant);
    }

    private boolean contient(CategorieComposite categorie) {
        if (this.equals(categorie)) {
            return true;
        }
        for (ComposantCategorie enfant : enfants) {
            if (enfant instanceof CategorieComposite && ((CategorieComposite) enfant).contient(categorie)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void supprimer(ComposantCategorie composant) {
        enfants.remove(composant);
    }

    @Override
    public List<ComposantCategorie> getEnfants() {
        return enfants;
    }

    @Override
    public String toString() {
        return idCategorie + " - " + nom;
    }
}
