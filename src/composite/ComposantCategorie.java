package composite;

import java.util.List;

public interface ComposantCategorie {
    String getNom();
    void afficherStructure(String prefix);


    // Méthodes de gestion d’enfants (ajout, suppression) :
    void ajouter(ComposantCategorie composant);
    void supprimer(ComposantCategorie composant);
    List<ComposantCategorie> getEnfants(); // Pour pouvoir parcourir l’arbre

}
