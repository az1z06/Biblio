package model;

public class LivreNumerique extends Livre {
    private String format; // PDF, EPUB
    private double tailleFichier; // En Mo

    public LivreNumerique(int idLivre, String titre, int anneePublication, String isbn, int idEditeur, int idCategorie, String format, double tailleFichier) {
        super(idLivre, titre, anneePublication, isbn, idEditeur, idCategorie);
        this.format = format;
        this.tailleFichier = tailleFichier;
    }

    public String getFormat() {
        return format;
    }

    public double getTailleFichier() {
        return tailleFichier;
    }

    @Override
    public void afficherDetails() {
        super.afficherDetails();
        System.out.println("Format : " + format + ", Taille : " + tailleFichier + " Mo");
    }
}
