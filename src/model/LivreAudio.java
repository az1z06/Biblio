package model;

public class LivreAudio extends Livre {
    private double duree; // Durée en heures
    private String narrateur; // Nom du narrateur

    public LivreAudio(int idLivre, String titre, int anneePublication, String isbn, int idEditeur, int idCategorie, double duree, String narrateur) {
        super(idLivre, titre, anneePublication, isbn, idEditeur, idCategorie);
        this.duree = duree;
        this.narrateur = narrateur;
    }

    public double getDuree() {
        return duree;
    }

    public String getNarrateur() {
        return narrateur;
    }

    @Override
    public void afficherDetails() {
        super.afficherDetails();
        System.out.println("Durée : " + duree + " heures, Narrateur : " + narrateur);
    }
}
