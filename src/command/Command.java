package command;

import model.Emprunt;

public interface Command {
    void execute(); // Exécuter la commande
    void annule(); // Annuler la commande
    boolean isExecuted(); // Vérifier si la commande a été exécutée
    String getDescription();
    Emprunt getEmprunt();

}
