package testsUnitaires;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import personnel.*;



class testLigue 
{
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
	
	@Test
	void createLigue() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		assertEquals("Fléchettes", ligue.getNom());
	}
	
	void setLigue() throws SauvegardeImpossible {
		
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		ligue.setNom("Test");
	}
	
	
	@Test 
	void Suppression() throws SauvegardeImpossible {

		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe;
		try {
			employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty",LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
			Employe employe1 = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty",LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)); 
			Employe employe2 = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty",LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)); 
			
			employe.remove();
			assertFalse(ligue.getEmployes().contains(employe));
			
			ligue.remove();
			assertFalse(gestionPersonnel.getLigues().contains(ligue));
			
		} catch (Erreurdate e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	/*
	@Test
	void changementetSuppAdmin() throws SauvegardeImpossible{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		
		
		Employe test = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty",LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)); 
		
		
		ligue.setAdministrateur(test);
		assertEquals(test, ligue.getAdministrateur());
		
		test.remove();
		
		assertFalse(ligue.getEmployes().contains(test));
	}
	
	@Test
	void Employe() throws SauvegardeImpossible 
	{
		//GETTEUR
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe test = ligue.addEmploye("El Arche", "Wassim", "mail", "azerty",LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)); 
		assertEquals("Wassim" , test.getPrenom());
		assertEquals("mail" , test.getMail());
		assertEquals(ligue,test.getLigue());
		
		// SETTEUR
		test.setMail("nouveaumail");
		assertEquals("nouveaumail" , test.getMail());
		test.setNom("NvNom");
		assertEquals("NvNom" , test.getNom());
		test.setPassword("nvmdp");
		assertTrue(test.checkPassword("nvmdp"));
		test.setPrenom("Nvprenom");
		assertEquals("Nvprenom" , test.getPrenom());
		
		
	}
	
	@Test
	void addEmploye() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty"  , LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)); 
		assertEquals(employe, ligue.getEmployes().first());
	}


    @Test
    public void testValidDates() throws SauvegardeImpossible {
    	
    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        assertDoesNotThrow(() -> ligue.addEmploye("El Arche", "Wassim", "wsmsevran", "mdp" ,LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31) ));
        
    }
    /*
    @Test
    public void testInvalidDates() {
        Exception exception = assertThrows(ErreurDate.class, () -> {
            new Employe(LocalDate.of(2023, 12, 31), LocalDate.of(2023, 1, 1));
        });
        assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", exception.getMessage());
    }

    @Test
    public void testSetDateDepartInvalid() {
        Employe employe = new Employe(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        Exception exception = assertThrows(ErreurDate.class, () -> {
            employe.setDateDepart(LocalDate.of(2022, 12, 31));
        });
        assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", exception.getMessage());
    }*/
}


