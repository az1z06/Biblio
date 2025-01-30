package test.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import composite.CategorieComposite;
import composite.ComposantCategorie;
import composite.LivreFeuille;
import dao.CategorieDAO;
import dao.DBConnection;
import dao.LivreDAO;
import model.Livre;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

class CategorieDAOTest {

    private CategorieDAO categorieDAO;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private DBConnection mockDBConnection;

    @Mock
    private LivreDAO mockLivreDAO;

    @Mock
    private Livre mockLivre;

    private AutoCloseable closeable; // Pour fermer les mocks après les tests
    private MockedStatic<DBConnection> mockedStaticDBConnection;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialiser les mocks
        closeable = MockitoAnnotations.openMocks(this);

        // Mock de la méthode statique DBConnection.getInstance()
        mockedStaticDBConnection = mockStatic(DBConnection.class);
        mockedStaticDBConnection.when(DBConnection::getInstance).thenReturn(mockDBConnection);

        // Mock de DBConnection.getInstance().getConnection()
        when(mockDBConnection.getConnection()).thenReturn(mockConnection);

        // Initialiser l'instance de CategorieDAO
        categorieDAO = new CategorieDAO();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Fermer les mocks
        closeable.close();
        mockedStaticDBConnection.close();
    }

    @Test
    void testAjouterCategorieSansParent() throws SQLException {
        CategorieComposite categorie = new CategorieComposite(0, "Science-Fiction");

        // Configuration des mocks
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        // Appel de la méthode à tester
        categorieDAO.ajouterCategorie(categorie);

        // Vérifications
        verify(mockPreparedStatement).setString(1, "Science-Fiction");
        verify(mockPreparedStatement).setNull(2, Types.INTEGER);
        verify(mockPreparedStatement).executeUpdate();
        verify(mockPreparedStatement).getGeneratedKeys();

        assertEquals(1, categorie.getIdCategorie());
    }

    @Test
    void testAjouterCategorieAvecParent() throws SQLException {
        // Création de la catégorie parente
        CategorieComposite parentCategorie = new CategorieComposite(2, "Littérature");

        // Création de la catégorie enfant
        CategorieComposite categorie = new CategorieComposite(0, "Fantasy");
        categorie.setParent(parentCategorie);

        // Configuration des mocks
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(3);

        // Appel de la méthode à tester
        categorieDAO.ajouterCategorie(categorie);

        // Vérifications
        verify(mockPreparedStatement).setString(1, "Fantasy");
        verify(mockPreparedStatement).setInt(2, 2);
        verify(mockPreparedStatement).executeUpdate();
        verify(mockPreparedStatement).getGeneratedKeys();

        assertEquals(3, categorie.getIdCategorie());
    }

    @Test
    void testSupprimerCategorie() throws SQLException {
        int idCategorie = 1;

        // Création de mocks pour les différentes requêtes de suppression
        PreparedStatement mockDeleteLivresStmt = mock(PreparedStatement.class);
        PreparedStatement mockDeleteSubStmt = mock(PreparedStatement.class);
        PreparedStatement mockDeleteCatStmt = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement("DELETE FROM livre WHERE ID_Categorie = ?"))
                .thenReturn(mockDeleteLivresStmt);
        when(mockConnection.prepareStatement("DELETE FROM categorie WHERE ID_Categorie_Parent = ?"))
                .thenReturn(mockDeleteSubStmt);
        when(mockConnection.prepareStatement("DELETE FROM categorie WHERE ID_Categorie = ?"))
                .thenReturn(mockDeleteCatStmt);

        // Appel de la méthode à tester
        categorieDAO.supprimerCategorie(idCategorie);

        // Vérifications des interactions
        verify(mockConnection).setAutoCommit(false);

        verify(mockDeleteLivresStmt).setInt(1, idCategorie);
        verify(mockDeleteLivresStmt).executeUpdate();

        verify(mockDeleteSubStmt).setInt(1, idCategorie);
        verify(mockDeleteSubStmt).executeUpdate();

        verify(mockDeleteCatStmt).setInt(1, idCategorie);
        verify(mockDeleteCatStmt).executeUpdate();

        verify(mockConnection).commit();
    }

    @Test
    void testGetAllCategorieNames() throws SQLException {
        // Configuration des mocks
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, true, false);
        when(mockResultSet.getInt("ID_Categorie")).thenReturn(1, 2, 3);
        when(mockResultSet.getString("Nom")).thenReturn("Romans", "Policier", "Science-Fiction");

        // Appel de la méthode à tester
        List<String> categories = categorieDAO.getAllCategorieNames();

        // Vérifications
        verify(mockPreparedStatement).executeQuery();
        assertEquals(Arrays.asList("1 - Romans", "2 - Policier", "3 - Science-Fiction"), categories);
    }

    @Test
    void testAjouterCategorieSQLException() throws SQLException {
        CategorieComposite categorie = new CategorieComposite(0, "Horreur");

        // Configuration des mocks pour lancer une SQLException
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenThrow(new SQLException("Erreur SQL"));

        // Appel de la méthode et vérification de l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categorieDAO.ajouterCategorie(categorie);
        });

        assertTrue(exception.getMessage().contains("Erreur lors de l'ajout de la catégorie."));
    }

    // ==========================
    // Tests pour construireArbreCategories()
    // ==========================

    @Test
    void testConstruireArbreCategories() throws SQLException {
        // Configuration des mocks pour la requête SELECT * FROM categorie
        when(mockConnection.prepareStatement("SELECT * FROM categorie")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Simuler les résultats de la requête
        // Supposons qu'il y ait deux catégories : 1 - Romans (parentId=0), 2 - Policier (parentId=1)
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("ID_Categorie")).thenReturn(1, 2);
        when(mockResultSet.getString("Nom")).thenReturn("Romans", "Policier");
        when(mockResultSet.getInt("ID_Categorie_Parent")).thenReturn(0, 1);
        when(mockResultSet.wasNull()).thenReturn(false, false);

        // Appel de la méthode à tester
        CategorieComposite arbre = categorieDAO.construireArbreCategories();

        // Vérifications
        assertNotNull(arbre);
        assertEquals("Racine", arbre.getNom());
        assertEquals(1, arbre.getEnfants().size());

        CategorieComposite romans = (CategorieComposite) arbre.getEnfants().get(0);
        assertEquals("Romans", romans.getNom());
        assertEquals(arbre, romans.getParent()); // Vérifie que "Romans" a "Racine" comme parent

        assertEquals(1, romans.getEnfants().size());

        CategorieComposite policier = (CategorieComposite) romans.getEnfants().get(0);
        assertEquals("Policier", policier.getNom());
        assertEquals(romans, policier.getParent());
    }

    // ==========================
    // Tests pour construireArbreCategoriesAvecLivres(LivreDAO livreDAO)
    // ==========================

    @Test
    void testConstruireArbreCategoriesAvecLivres() throws SQLException {
        // Configuration des mocks pour la requête SELECT * FROM categorie
        when(mockConnection.prepareStatement("SELECT * FROM categorie")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Simuler les résultats de la requête
        // Supposons qu'il y ait deux catégories : 1 - Romans (parentId=0), 2 - Policier (parentId=1)
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("ID_Categorie")).thenReturn(1, 2);
        when(mockResultSet.getString("Nom")).thenReturn("Romans", "Policier");
        when(mockResultSet.getInt("ID_Categorie_Parent")).thenReturn(0, 1);
        when(mockResultSet.wasNull()).thenReturn(false, false);

        // Configuration du mock LivreDAO pour retourner deux livres
        Livre livre1 = mock(Livre.class);
        when(livre1.getID_Categorie()).thenReturn(1);
        Livre livre2 = mock(Livre.class);
        when(livre2.getID_Categorie()).thenReturn(2);
        List<Livre> livres = Arrays.asList(livre1, livre2);

        when(mockLivreDAO.getLivres()).thenReturn(livres);

        // Appel de la méthode à tester
        CategorieComposite arbre = categorieDAO.construireArbreCategoriesAvecLivres(mockLivreDAO);

        // Vérifications
        assertNotNull(arbre);
        assertEquals("Racine", arbre.getNom());
        assertEquals(1, arbre.getEnfants().size());

        CategorieComposite romans = (CategorieComposite) arbre.getEnfants().get(0);
        assertEquals("Romans", romans.getNom());
        assertEquals(arbre, romans.getParent());

        // Vérifie que la catégorie "Romans" a deux enfants : "Policier" et "Livre1"
        assertEquals(2, romans.getEnfants().size());

        CategorieComposite policier = null;
        LivreFeuille livre1Feuille = null;

        for (ComposantCategorie enfant : romans.getEnfants()) {
            if (enfant instanceof CategorieComposite) {
                CategorieComposite cat = (CategorieComposite) enfant;
                if (cat.getNom().equals("Policier")) {
                    policier = cat;
                    assertEquals(romans, cat.getParent());
                }
            } else if (enfant instanceof LivreFeuille) {
                livre1Feuille = (LivreFeuille) enfant;
                // Optionnel : vérifier les détails du livre
                // assertEquals(livre1, livre1Feuille.getLivre());
            }
        }

        assertNotNull(policier, "La catégorie 'Policier' n'a pas été trouvée en tant qu'enfant de 'Romans'.");
        assertNotNull(livre1Feuille, "Le Livre1 n'a pas été trouvé en tant qu'enfant de 'Romans'.");

        // Vérifie que la catégorie "Policier" a un enfant : "Livre2"
        assertEquals(1, policier.getEnfants().size());
        assertTrue(policier.getEnfants().get(0) instanceof LivreFeuille, "L'enfant de 'Policier' n'est pas un LivreFeuille.");

      
    }


    @Test
    void testConstruireArbreCategoriesSQLException() throws SQLException {
        // Configuration des mocks pour lancer une SQLException lors de la préparation de la requête
        when(mockConnection.prepareStatement("SELECT * FROM categorie")).thenThrow(new SQLException("Erreur SQL"));

        // Appel de la méthode et vérification de l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categorieDAO.construireArbreCategories();
        });

        assertTrue(exception.getMessage().contains("Erreur lors de la récupération des catégories."));
    }

    @Test
    void testConstruireArbreCategoriesAvecLivresSQLException() throws SQLException {
        // Configuration des mocks pour la requête SELECT * FROM categorie
        when(mockConnection.prepareStatement("SELECT * FROM categorie")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Erreur SQL"));

        // Appel de la méthode et vérification de l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categorieDAO.construireArbreCategoriesAvecLivres(mockLivreDAO);
        });

        assertTrue(exception.getMessage().contains("Erreur lors de la récupération des catégories et des livres."));
    }
}
