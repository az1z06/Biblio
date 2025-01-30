package test.command;

import dao.EmpruntDAO;
import model.Emprunt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import command.ReturnCommand;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReturnCommandTest {

    private EmpruntDAO empruntDAO;
    private ReturnCommand returnCommand;
    private Emprunt emprunt;

    @BeforeEach
    void setUp() {
        empruntDAO = mock(EmpruntDAO.class);
        emprunt = new Emprunt(1, 100, 200, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 86400000), null);
        returnCommand = new ReturnCommand(emprunt, empruntDAO);
    }

    @Test
    void testExecute() {
        returnCommand.execute();
        assertTrue(returnCommand.isExecuted());
        assertNotNull(emprunt.getDate_Retour_Effective());
        verify(empruntDAO, times(1)).modifierEmprunt(emprunt);
    }

    @Test
    void testAnnule() {
        returnCommand.execute();
        returnCommand.annule();
        assertFalse(returnCommand.isExecuted());
        verify(empruntDAO, times(2)).modifierEmprunt(emprunt);
    }

    @Test
    void testGetDescription() {
        assertEquals("Retour du livre avec ID 100 par le membre 200", returnCommand.getDescription());
    }
}
