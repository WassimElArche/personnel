package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import personnel.*;

class TestNumeroSecuriteSociale {
    
    private GestionPersonnel gestionPersonnel;
    
    @BeforeEach
    void setUp() throws Exception {
        gestionPersonnel = new GestionPersonnel();
    }

    @Test
    void testNumeroSecuriteSocialeFormatValide() throws SauvegardeImpossible, Erreurdate {
        Ligue ligue = gestionPersonnel.addLigue("Football");
        
        // Test avec un numéro de sécurité sociale valide
        Employe employe = ligue.addEmploye(
            "Durant", 
            "Pierre", 
            "p.durant@example.com", 
            "password", 
            LocalDate.of(2023, 1, 1), 
            LocalDate.of(2024, 1, 1), 
            false,
            "123456789012345"
        );
        
        assertEquals("123456789012345", employe.getNumeroSecuriteSociale());
    }

    @Test
    void testNumeroSecuriteSocialeFormatInvalide() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("Basketball");
        
        // Test avec format invalide (trop court)
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            ligue.addEmploye(
                "Martin", 
                "Julie", 
                "j.martin@example.com", 
                "password", 
                LocalDate.of(2023, 1, 1), 
                LocalDate.of(2024, 1, 1), 
                false,
                "12345"
            );
        });
        assertEquals("Format de numéro de sécurité sociale invalide", exception1.getMessage());
    }

    @Test
    void testSetNumeroSecuriteSocialeInvalide() throws SauvegardeImpossible, Erreurdate {
        Ligue ligue = gestionPersonnel.addLigue("Tennis");
        
        Employe employe = ligue.addEmploye(
            "Leroy", 
            "Emma", 
            "e.leroy@example.com", 
            "password", 
            LocalDate.of(2023, 1, 1), 
            LocalDate.of(2024, 1, 1), 
            false,
            "123456789012345"
        );
        
        // Test avec une valeur invalide lors de la modification
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employe.setNumeroSecuriteSociale("123");
        });
        assertEquals("Format de numéro de sécurité sociale invalide", exception.getMessage());
    }

    @Test
    void testNumeroSecuriteSocialeNull() throws SauvegardeImpossible, Erreurdate {
        Ligue ligue = gestionPersonnel.addLigue("Handball");
        
        // Test avec une valeur null
        Employe employe = ligue.addEmploye(
            "Bernard", 
            "Thomas", 
            "t.bernard@example.com", 
            "password", 
            LocalDate.of(2023, 1, 1), 
            LocalDate.of(2024, 1, 1), 
            false,
            null
        );
        
        assertNull(employe.getNumeroSecuriteSociale());
    }
} 