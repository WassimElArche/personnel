package testsUnitaires;
import static org.junit.Assert.assertThrows;
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
	
	@Test
	void changementetSuppAdmin() throws SauvegardeImpossible{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		
		
		Employe test;
		try {
			
			
			 
			test = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty",LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
			
			
			//teste personnel 
			assertEquals(gestionPersonnel.getRoot() , ligue.getAdministrateur());
			
			
			//MODIF ADMIN
			ligue.setAdministrateur(test);
			assertEquals(test, ligue.getAdministrateur());
			
			
			//SUPP ADMIN 
			test.remove();
			assertFalse(ligue.getEmployes().contains(test));
		
			//VERIFIE QUE ROOT EST BIEN ADMIN 
			assertFalse(ligue.getEmployes().contains(test));
			assertEquals(gestionPersonnel.getRoot() , ligue.getAdministrateur());
			
		} catch (Erreurdate e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		
	}
	
	@Test
	void Employe() throws SauvegardeImpossible 
	{
		//GETTEUR
		try {
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
		catch(Erreurdate e) {
			e.printStackTrace();
			}
		
	}
	
	
	@Test
	void addEmploye() throws SauvegardeImpossible
	{
		
		try {
			Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
			Employe employe;
			employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty"  , LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
			assertEquals(employe, ligue.getEmployes().first());
		} catch (Erreurdate e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}


    @Test
     void testValidDates() throws SauvegardeImpossible {
    	
    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        assertDoesNotThrow(() -> ligue.addEmploye("El Arche", "Wassim", "wsmsevran", "mdp" ,LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31) ));
        
    }
    
    
    @Test
     void testInvalidDates() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Exception exception = assertThrows(Erreurdate.class, () -> {
            ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty"  , LocalDate.of(2023, 12, 31), LocalDate.of(2023, 1, 1));
        });
        assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", exception.getMessage());
    }

    @Test
     void testSetDateDepartInvalid() throws SauvegardeImpossible {
    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        	try {
        		
			Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty"  , LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
			Exception exception = assertThrows(Erreurdate.class, () -> {
	            employe.setDate_depart(LocalDate.of(2020, 12, 31));
			});
			
        	}catch (Erreurdate e ) {
        		assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", e.getMessage());
        	}
		}
    @Test
     void testSetDateArriveInvalid() throws SauvegardeImpossible {
    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        	try {
        		
			Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty"  , LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
			Exception exception = assertThrows(Erreurdate.class, () -> {
	            employe.setDate_arrivee(LocalDate.of(2024, 12, 31));
			});
			
        	}catch (Erreurdate e ) {
        		assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", e.getMessage());
        	}
		}
    
    
    @Test
    void testGetteurDate() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
    
    	try {
    		
			
    		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty"  , LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
			assertEquals( employe.getDate_arrivee() , LocalDate.of(2023, 1, 1)); 
			assertEquals( employe.getDate_depart() , LocalDate.of(2023, 12, 31)); 
    		
    		
    	}catch (Erreurdate e ) {
    		
    }
    
        
    
}
    }


