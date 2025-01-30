package command;

import dao.EmpruntDAO;
import model.Emprunt;

public class BorrowCommand implements Command {

    private Emprunt emprunt;
    private EmpruntDAO empruntDAO;
    private boolean executed = false;

    public BorrowCommand(Emprunt emprunt, EmpruntDAO empruntDAO) {
        this.emprunt = emprunt;
        this.empruntDAO = empruntDAO;
    }

    @Override
    public void execute() {
        if (!executed) {
            boolean success = empruntDAO.ajouterEmprunt(emprunt);
            if (success) {
                executed = true;
            }
        }
    }

    @Override
    public void annule() {
        if (executed) {
            empruntDAO.supprimerEmprunt(emprunt.getID_Emprunt());
            executed = false;
        }
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }
    
       
    @Override
    public String getDescription() {
        return "Emprunt du livre avec ID " + emprunt.getID_Livre() + " par le membre " + emprunt.getID_Membre();
    }
    
    @Override
    public Emprunt getEmprunt() {
        return emprunt;
    }

}