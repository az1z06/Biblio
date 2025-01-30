package controller;

import dao.EmpruntDAO;
import dao.LivreDAO;
import dao.MembreDAO;
import model.Emprunt;
import model.Livre;
import model.Membre;
import observateur.Subject;
import command.BorrowCommand;
import command.ReturnCommand;
import command.CommandManager;

import java.util.List;

public class EmpruntController extends Subject {
    private EmpruntDAO empruntDAO;
    private LivreDAO livreDAO;
    private MembreDAO membreDAO;
    private CommandManager cmdManager;

    public EmpruntController(EmpruntDAO empruntDAO, LivreDAO livreDAO, MembreDAO membreDAO, CommandManager cmdManager) {
        this.empruntDAO = empruntDAO;
        this.livreDAO = livreDAO;
        this.membreDAO = membreDAO;
        this.cmdManager = cmdManager;
    }

    public void emprunter(Emprunt e) {
        Livre livre = livreDAO.getLivreById(e.getID_Livre());
        Membre membre = membreDAO.getMembreById(e.getID_Membre());

        if (livre == null) {
            throw new IllegalArgumentException("Livre introuvable avec l'ID : " + e.getID_Livre());
        }
        if (membre == null) {
            throw new IllegalArgumentException("Membre introuvable avec l'ID : " + e.getID_Membre());
        }

        e.setLivre(livre);
        e.setMembre(membre);

        BorrowCommand cmd = new BorrowCommand(e, empruntDAO);
        cmdManager.executeCommand(cmd);

        // Ajouter le membre comme observateur du livre
        livre.addObserver(membre);
        // Notification après l'ajout d'un emprunt
        notifyObservers();
    }


    public void retourner(Emprunt e) {
        ReturnCommand cmd = new ReturnCommand(e, empruntDAO);
        cmdManager.executeCommand(cmd);

        Livre livre = livreDAO.getLivreById(e.getID_Livre());
        if (livre != null) {
            livre.notifierDisponibilite();
        }
        
        notifyObservers(); // Notification après le retour d'un emprunt
    }


    public List<Emprunt> getAllEmprunts() {
        return empruntDAO.getEmprunts();
    }

    public List<Emprunt> rechercherEmprunts(String query) {
        return empruntDAO.rechercherEmprunts(query);
    }

    public boolean modifierEmprunt(Emprunt e) {
    	boolean success = empruntDAO.modifierEmprunt(e);
        if (success) {
            notifyObservers(); // Notification après modification
        }
        return success;
    }

    public boolean supprimerEmprunt(int idEmprunt) {
        boolean success = empruntDAO.supprimerEmprunt(idEmprunt);
        if (success) {
            notifyObservers(); // Notification après suppression
        }
        return success;
    }
    
    public CommandManager getCommandManager() {
        return cmdManager;
    }

    public Emprunt getEmpruntById(int idEmprunt) {
        return empruntDAO.getEmpruntById(idEmprunt);
    }

    public void mettreAJourEmprunt(Emprunt emprunt) {
        empruntDAO.modifierEmprunt(emprunt);
    }
    
    public void mettreDateRetourEffective(int idEmprunt) {
        Emprunt emprunt = empruntDAO.getEmpruntById(idEmprunt);
        if (emprunt != null) {
            // Charger le livre et le membre associés
            Livre livre = livreDAO.getLivreById(emprunt.getID_Livre());
            Membre membre = membreDAO.getMembreById(emprunt.getID_Membre());

            if (livre == null) {
                throw new IllegalArgumentException("Livre introuvable avec l'ID : " + emprunt.getID_Livre());
            }
            if (membre == null) {
                throw new IllegalArgumentException("Membre introuvable avec l'ID : " + emprunt.getID_Membre());
            }

            emprunt.setLivre(livre);
            emprunt.setMembre(membre);

            // Mettre la date actuelle comme date de retour effective
            java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
            emprunt.setDate_Retour_Effective(now);

            // Mise à jour dans la base de données
            empruntDAO.modifierEmprunt(emprunt);
        } else {
            throw new IllegalArgumentException("Aucun emprunt trouvé avec l'ID : " + idEmprunt);
        }
    }


}
