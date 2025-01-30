package main;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import model.Livre;

public class LivreTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private List<Livre> livres;
    private String[] columnNames = {"ID Livre", "Titre", "Année Publication", "ISBN", "ID Editeur", "ID Catégorie"};

    public LivreTableModel() {
        this.livres = new ArrayList<>();
    }

    public void setLivres(List<Livre> livres) {
        this.livres = livres;
        fireTableDataChanged();
    }

    public void addLivre(Livre livre) {
        livres.add(livre);
        fireTableRowsInserted(livres.size() - 1, livres.size() - 1);
    }

    public Livre getLivreAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < livres.size()) {
            return livres.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return livres.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Livre livre = livres.get(rowIndex);
        switch (columnIndex) {
            case 0: return livre.getID_Livre();
            case 1: return livre.getTitre();
            case 2: return livre.getAnnee_Publication();
            case 3: return livre.getISBN();
            case 4: return livre.getID_Editeur();
            case 5: return livre.getID_Categorie();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
