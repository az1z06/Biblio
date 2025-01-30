package command;

import dao.EmpruntDAO;
import model.Emprunt;
import java.sql.Date;

public class ReturnCommand implements Command {

    private Emprunt emprunt;
    private EmpruntDAO empruntDAO;
    private boolean executed = false;
    private Date ancienneDateRetour;

    public ReturnCommand(Emprunt emprunt, EmpruntDAO empruntDAO) {
        this.emprunt = emprunt;
        this.empruntDAO = empruntDAO;
    }

    @Override
    public void execute() {
        if (!executed) {
            ancienneDateRetour = emprunt.getDate_Retour_Effective();
            Date now = new Date(System.currentTimeMillis());
            emprunt.setDate_Retour_Effective(now);
            empruntDAO.modifierEmprunt(emprunt);
            executed = true;
        }
    }

    @Override
    public void annule() {
        if (executed) {
            emprunt.setDate_Retour_Effective(ancienneDateRetour);
            empruntDAO.modifierEmprunt(emprunt);
            executed = false;
        }
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }
    
    @Override
    public String getDescription() {
        return "Retour du livre avec ID " + emprunt.getID_Livre() + " par le membre " + emprunt.getID_Membre();
    }
    
    @Override
    public Emprunt getEmprunt() {
        return emprunt;
    }


}
