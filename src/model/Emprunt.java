package model;

import java.sql.Date;

public class Emprunt {
    private int ID_Emprunt;
    private int ID_Livre;
    private int ID_Membre;
    private Date Date_Emprunt;
    private Date Date_Retour_Prevue;
    private Date Date_Retour_Effective;

    private Livre livre; // Référence au livre associé
    private Membre membre; // Référence au membre associé

    public Emprunt(int idEmprunt, int idLivre, int idMembre, Date dateEmprunt, Date dateRetourPrevue, Date dateRetourEffective) {
        this.ID_Emprunt = idEmprunt;
        this.ID_Livre = idLivre;
        this.ID_Membre = idMembre;
        this.Date_Emprunt = dateEmprunt;
        this.Date_Retour_Prevue = dateRetourPrevue;
        this.Date_Retour_Effective = dateRetourEffective;
    }

 // Nouveau constructeur pour inclure Livre et Membre
    public Emprunt(int idEmprunt, int idLivre, int idMembre, Date dateEmprunt, Date dateRetourPrevue, Date dateRetourEffective, Livre livre, Membre membre) {
        this.ID_Emprunt = idEmprunt;
        this.ID_Livre = idLivre;
        this.ID_Membre = idMembre;
        this.Date_Emprunt = dateEmprunt;
        this.Date_Retour_Prevue = dateRetourPrevue;
        this.Date_Retour_Effective = dateRetourEffective;
        this.livre = livre;
        this.membre = membre;
    }
    public int getID_Emprunt() {
        return ID_Emprunt;
    }

    public int getID_Livre() {
        return ID_Livre;
    }

    public int getID_Membre() {
        return ID_Membre;
    }

    public Date getDate_Emprunt() {
        return Date_Emprunt;
    }

    public Date getDate_Retour_Prevue() {
        return Date_Retour_Prevue;
    }

    public Date getDate_Retour_Effective() {
        return Date_Retour_Effective;
    }

    public void setDate_Retour_Effective(Date date_Retour_Effective) {
        Date_Retour_Effective = date_Retour_Effective;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

	public void setID_Emprunt(int iD_Emprunt) {
		ID_Emprunt = iD_Emprunt;
	}

	public void setID_Livre(int iD_Livre) {
		ID_Livre = iD_Livre;
	}

	public void setID_Membre(int iD_Membre) {
		ID_Membre = iD_Membre;
	}

	public void setDate_Emprunt(Date date_Emprunt) {
		Date_Emprunt = date_Emprunt;
	}

	public void setDate_Retour_Prevue(Date date_Retour_Prevue) {
		Date_Retour_Prevue = date_Retour_Prevue;
	}
}
