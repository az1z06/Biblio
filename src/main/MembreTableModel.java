package main;

import model.Membre;
import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class MembreTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    
    private List<Membre> membres; // Liste des membres affichés
    private String[] colonnes = {"ID", "Nom", "Prénom", "Email", "Date d'Inscription"};

    // Constructeur
    public MembreTableModel(List<Membre> membres) {
        this.membres = membres;
    }

    // Définit la liste des membres et rafraîchit le tableau
    public void setMembres(List<Membre> membres) {
        this.membres = membres;
        fireTableDataChanged();
    }

    // Retourne un membre à l'index spécifié
    public Membre getMembreAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < membres.size()) {
            return membres.get(rowIndex);
        }
        return null; // Si l'indice est invalide
    }

    @Override
    public int getRowCount() {
        return membres.size(); // Nombre de membres
    }

    @Override
    public int getColumnCount() {
        return colonnes.length; // Nombre de colonnes
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colonnes[columnIndex]; // Nom de la colonne
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Membre membre = membres.get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        switch (columnIndex) {
            case 0: return membre.getID_Membre();
            case 1: return membre.getNom();
            case 2: return membre.getPrenom();
            case 3: return membre.getEmail();
            case 4: return sdf.format(membre.getDate_Inscription());
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Rendre toutes les cellules non éditables
    }
}
