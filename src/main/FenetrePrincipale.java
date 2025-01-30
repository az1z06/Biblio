package main;

import command.CommandManager;
import controller.*;
import dao.*;

import javax.swing.*;
import java.awt.*;

public class FenetrePrincipale extends JFrame {

    private static final long serialVersionUID = 1L; // Champ serialVersionUID ajouté

    private CardLayout cardLayout;
    private JPanel panneauPrincipal;

    private JButton boutonLivres;
    private JButton boutonMembres;
    private JButton boutonEmprunts;
    private JButton boutonReservations;
    private JButton boutonCategories;

    public FenetrePrincipale(LivreController livreController, MembreController membreController, EmpruntController empruntController,
            ReservationController reservationController, CategorieController categorieController,
            HistoriqueCommandeDAO historiqueCommandeDAO, LivreDAO livreDAO, MembreDAO membreDAO, CommandManager commandManager) {
setTitle("Gestion de la Bibliothèque");
setSize(1000, 600);
setLocationRelativeTo(null);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

// CardLayout pour les panneaux
cardLayout = new CardLayout();
panneauPrincipal = new JPanel(cardLayout);

// Ajouter les panneaux dans l'ordre spécifié
panneauPrincipal.add(new PanneauLivres(livreController), "Livres");
panneauPrincipal.add(new PanneauMembres(membreController), "Membres");
panneauPrincipal.add(new PanneauEmprunts(empruntController), "Emprunts");
panneauPrincipal.add(new PanneauReservations(reservationController, livreDAO, membreDAO), "Reservations");
panneauPrincipal.add(new PanneauCategories(categorieController), "Categories");

// Historique des commandes
PanneauHistorique panneauHistorique = new PanneauHistorique(historiqueCommandeDAO, commandManager);
commandManager.addObserver(panneauHistorique); // Ajouter l'observateur

// Barre de menu
JMenuBar menuBar = new JMenuBar();

// Menu Gestion
JMenu menuGestion = new JMenu("Gestion");

JMenuItem itemEditeurs = new JMenuItem("Gestion des Éditeurs");
itemEditeurs.addActionListener(e -> {
EditeurController editeurController = new EditeurController(new EditeurDAO());
JFrame frameEditeurs = new JFrame("Gestion des Éditeurs");
frameEditeurs.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
frameEditeurs.setSize(600, 400);
frameEditeurs.setLocationRelativeTo(null);
frameEditeurs.add(new PanneauEditeur(editeurController));
frameEditeurs.setVisible(true);
});

JMenuItem itemAuteurs = new JMenuItem("Gestion des Auteurs");
itemAuteurs.addActionListener(e -> {
AuteurController auteurController = new AuteurController(new AuteurDAO());
JFrame frameAuteurs = new JFrame("Gestion des Auteurs");
frameAuteurs.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
frameAuteurs.setSize(600, 400);
frameAuteurs.setLocationRelativeTo(null);
frameAuteurs.add(new PanneauAuteur(auteurController));
frameAuteurs.setVisible(true);
});

menuGestion.add(itemEditeurs);
menuGestion.add(itemAuteurs);
menuBar.add(menuGestion);

// Menu Historique
JMenu menuHistorique = new JMenu("Historique");

JMenuItem itemHistorique = new JMenuItem("Historique des Commandes");
itemHistorique.addActionListener(e -> {
JFrame frameHistorique = new JFrame("Historique des Commandes");
frameHistorique.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
frameHistorique.setSize(800, 600);
frameHistorique.setLocationRelativeTo(null);
frameHistorique.add(panneauHistorique);
frameHistorique.setVisible(true);
});

menuHistorique.add(itemHistorique);
menuBar.add(menuHistorique);

setJMenuBar(menuBar);

// Options de navigation
JPanel panneauOptions = new JPanel(new GridLayout(1, 5, 10, 10));
panneauOptions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

boutonLivres = createStyledButton("Livres");
boutonMembres = createStyledButton("Membres");
boutonEmprunts = createStyledButton("Emprunts");
boutonReservations = createStyledButton("Réservations");
boutonCategories = createStyledButton("Catégories");

boutonLivres.addActionListener(e -> cardLayout.show(panneauPrincipal, "Livres"));
boutonMembres.addActionListener(e -> cardLayout.show(panneauPrincipal, "Membres"));
boutonEmprunts.addActionListener(e -> cardLayout.show(panneauPrincipal, "Emprunts"));
boutonReservations.addActionListener(e -> cardLayout.show(panneauPrincipal, "Reservations"));
boutonCategories.addActionListener(e -> cardLayout.show(panneauPrincipal, "Categories"));

panneauOptions.add(boutonLivres);
panneauOptions.add(boutonMembres);
panneauOptions.add(boutonEmprunts);
panneauOptions.add(boutonReservations);
panneauOptions.add(boutonCategories);

setLayout(new BorderLayout());
add(panneauOptions, BorderLayout.NORTH);
add(panneauPrincipal, BorderLayout.CENTER);
}


    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(168, 230, 207));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                LivreDAO livreDAO = new LivreDAO();
                CategorieDAO categorieDAO = new CategorieDAO();
                MembreDAO membreDAO = new MembreDAO();
                EmpruntDAO empruntDAO = new EmpruntDAO();
                HistoriqueCommandeDAO historiqueCommandeDAO = new HistoriqueCommandeDAO(empruntDAO);

                LivreController livreController = new LivreController(livreDAO, categorieDAO, new EditeurDAO());
                MembreController membreController = new MembreController(membreDAO);
                CommandManager commandManager = new CommandManager(historiqueCommandeDAO);

                EmpruntController empruntController = new EmpruntController(empruntDAO, livreDAO, membreDAO, commandManager);
                ReservationController reservationController = new ReservationController(new ReservationDAO());
                CategorieController categorieController = new CategorieController(categorieDAO, livreDAO);

                FenetrePrincipale fenetre = new FenetrePrincipale(
                        livreController, membreController, empruntController, reservationController,
                        categorieController, historiqueCommandeDAO, livreDAO, membreDAO, commandManager);
                fenetre.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors du démarrage de l'application : " + e.getMessage());
            }
        });
    }
}
