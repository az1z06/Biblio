package composite;

import model.Livre;
import java.util.Collections;
import java.util.List;

public class LivreFeuille implements ComposantCategorie {

    private Livre livre;

    public LivreFeuille(Livre livre) {
        this.livre = livre;
    }

    @Override
    public String getNom() {
        return livre.getTitre();
    }

    @Override
    public void ajouter(ComposantCategorie composant) {
        // Non applicable pour les feuilles
    }

    @Override
    public void supprimer(ComposantCategorie composant) {
        // Non applicable pour les feuilles
    }

    @Override
    public List<ComposantCategorie> getEnfants() {
        return Collections.emptyList();
    }

    @Override
    public void afficherStructure(String prefix) {
        System.out.println((prefix.trim() + "Livre: " + livre.getTitre()).trim());
    }

    @Override
    public String toString() {
        return livre.getTitre();
    }
}
