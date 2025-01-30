package command;

import java.util.ArrayList;
import java.util.List;

import dao.HistoriqueCommandeDAO;
import observateur.Subject;

public class CommandManager extends Subject {

    private List<Command> historique;
    private HistoriqueCommandeDAO historiqueCommandeDAO;

    public CommandManager(HistoriqueCommandeDAO historiqueCommandeDAO) { 
        this.historiqueCommandeDAO = historiqueCommandeDAO;
        historique = new ArrayList<>();
    }

    public void executeCommand(Command cmd) {
        if (!cmd.isExecuted()) {
            cmd.execute();
            historique.add(cmd);
            
            // Ajouter à l'historique en base (évite NullPointerException)
            if (historiqueCommandeDAO != null) {
                historiqueCommandeDAO.ajouterHistorique(cmd);
            }
        
            // Notifier les observateurs
            notifyObservers();
        }
    }

    public void undoLastCommand() {
        if (!historique.isEmpty()) {
            Command lastCommand = historique.get(historique.size() - 1);
            if (lastCommand.isExecuted()) {
                lastCommand.annule();
                historique.remove(lastCommand);
            
             // Notifier les observateurs
                notifyObservers();
            }
        }
    }

    public void undoSpecificCommand(int index) {
        if (index >= 0 && index < historique.size()) {
            Command cmd = historique.get(index);
            if (cmd.isExecuted()) {
                cmd.annule();
                historique.remove(cmd);
            
             // Notifier les observateurs
                notifyObservers();
            }
        }
    }

    public void redoCommand(Command cmd) {
        if (!cmd.isExecuted()) {
            cmd.execute();
            if (!historique.contains(cmd)) {
                historique.add(cmd);
            }
        }
    }

    public List<Command> getHistorique() {
        return historique;
    }
    public List<Command> rechercherCommandes(String keyword) {
        List<Command> resultats = new ArrayList<>();
        for (Command cmd : historique) {
            if (cmd.toString().toLowerCase().contains(keyword.toLowerCase())) {
                resultats.add(cmd);
            }
        }
        return resultats;
    }
}
