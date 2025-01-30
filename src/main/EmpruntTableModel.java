package main;

import model.Emprunt;
import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

public class EmpruntTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private List<Emprunt> emprunts;
    private final String[] columnNames = {
        "ID Emprunt",
        "Nom Livre",
        "Nom Membre",
        "Date Emprunt",
        "Date Retour Prévue",
        "Date Retour Effective"
    };

    public EmpruntTableModel(List<Emprunt> emprunts) {
        this.emprunts = emprunts;
    }

    public void setEmprunts(List<Emprunt> emprunts) {
    	emprunts.sort(Comparator.comparingInt(Emprunt::getID_Emprunt)); 
        this.emprunts = emprunts;
        fireTableDataChanged();
    }

    public void addEmprunt(Emprunt emprunt) {
        emprunts.add(emprunt);
        fireTableRowsInserted(emprunts.size() - 1, emprunts.size() - 1);
    }

    public void removeEmprunt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < emprunts.size()) {
            emprunts.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public Emprunt getEmpruntAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < emprunts.size()) {
            return emprunts.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return emprunts.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Emprunt emprunt = emprunts.get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        switch (columnIndex) {
            case 0: return emprunt.getID_Emprunt();
            case 1: return emprunt.getLivre() != null ? emprunt.getLivre().getTitre() : "Inconnu";
            case 2: return emprunt.getMembre() != null ? emprunt.getMembre().getNom() : "Inconnu";
            case 3: return sdf.format(emprunt.getDate_Emprunt());
            case 4: return sdf.format(emprunt.getDate_Retour_Prevue());
            case 5: return emprunt.getDate_Retour_Effective() != null ? sdf.format(emprunt.getDate_Retour_Effective()) : "";
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
