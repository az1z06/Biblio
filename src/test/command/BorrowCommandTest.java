package test.command;

import dao.EmpruntDAO;
import model.Emprunt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import command.BorrowCommand;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowCommandTest {

    private EmpruntDAO empruntDAO;
    private BorrowCommand borrowCommand;
    private Emprunt emprunt;

    @BeforeEach
    void setUp() {
        empruntDAO = mock(EmpruntDAO.class);
        emprunt = new Emprunt(1, 100, 200, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 86400000), null);
        borrowCommand = new BorrowCommand(emprunt, empruntDAO);
    }

    @Test
    void testExecute() {
        when(empruntDAO.ajouterEmprunt(emprunt)).thenReturn(true);
        borrowCommand.execute();
        assertTrue(borrowCommand.isExecuted());
        verify(empruntDAO, times(1)).ajouterEmprunt(emprunt);
    }

    @Test
    void testAnnule() {
        when(empruntDAO.ajouterEmprunt(emprunt)).thenReturn(true);
        borrowCommand.execute();
        borrowCommand.annule();
        assertFalse(borrowCommand.isExecuted());
        verify(empruntDAO, times(1)).supprimerEmprunt(emprunt.getID_Emprunt());
    }

    @Test
    void testGetDescription() {
        assertEquals("Emprunt du livre avec ID 100 par le membre 200", borrowCommand.getDescription());
    }
}
