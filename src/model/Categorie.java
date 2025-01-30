package model;

public class Categorie {
    private int ID_Categorie;
    private String Nom;
    private Integer ID_Categorie_Parent;

    public Categorie(int idCategorie, String nom, Integer idCategorieParent) {
        this.ID_Categorie = idCategorie;
        this.Nom = nom;
        this.ID_Categorie_Parent = idCategorieParent;
    }

    public int getID_Categorie() {
        return ID_Categorie;
    }

    public void setID_Categorie(int iD_Categorie) {
        ID_Categorie = iD_Categorie;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public Integer getID_Categorie_Parent() {
        return ID_Categorie_Parent;
    }

    public void setID_Categorie_Parent(Integer iD_Categorie_Parent) {
        ID_Categorie_Parent = iD_Categorie_Parent;
    }
}
