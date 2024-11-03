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
	void Suppression() throws SauvegardeImpossible, Erreurdate {

		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe;
		
			employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty",LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
			Employe employe1 = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty",LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)); 
			Employe employe2 = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty",LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)); 
			
			employe.remove();
			assertFalse(ligue.getEmployes().contains(employe));
			
			ligue.remove();
			assertFalse(gestionPersonnel.getLigues().contains(ligue));
			
		
		
		
	}
	
	@Test
	void changementetSuppAdmin() throws SauvegardeImpossible, Erreurdate{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		
		
		Employe test;
			 
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
			
		
		
		
		
	}
	
	@Test
	void Employe() throws SauvegardeImpossible, Erreurdate 
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
	void addEmploye() throws SauvegardeImpossible, Erreurdate
	{
		
		
			Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
			Employe employe;
			employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty"  , LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
			assertEquals(employe, ligue.getEmployes().first());
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
    
    
    // TEST POUR LES DATES NULL , SEULEMENT LA DATE DE DEPART PEUT ETRE NULL
    
    @Test
    void testDateArriveNull() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("Football");
    	
    
     Exception exception1 = assertThrows(Erreurdate.class, () -> {
            ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty"  , null , LocalDate.of(2023, 1, 1));
        });
        assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", exception1.getMessage());
        
     }
    
    
    @Test
    void setDateArriveNull()throws SauvegardeImpossible, Erreurdate {
    	Ligue ligue = gestionPersonnel.addLigue("Football");
    
    		Employe employe = ligue.addEmploye("a", "a", "a", "a", LocalDate.of(2023, 12, 31), LocalDate.of(2024, 1, 1));
    		Exception exception1 = assertThrows(Erreurdate.class, () -> {
                employe.setDateArrivee(null);
            });
    		
 
    }
    
    
    

    @Test
     void testSetDateDepartInvalid() throws SauvegardeImpossible , Erreurdate {
    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        	
        		
			Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty"  , LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
			
			Exception exception = assertThrows(Erreurdate.class , () -> employe.setDateDepart(LocalDate.of(2022, 1, 1)));
	        assertEquals("La date de départ ne peut pas être avant la date d'arrivée." , exception.getMessage());    
	        
			
			
        	
		}
    
    
    @Test
     void testSetDateArriveInvalid() throws SauvegardeImpossible , Erreurdate {
    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        
        		
			Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty"  , LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
			Exception erreur = assertThrows(Erreurdate.class, () -> employe.setDateArrivee(LocalDate.of(2024, 12, 2)));
			assertEquals("La date de départ ne peut pas être avant la date d'arrivée." , erreur.getMessage());
        	
        		
        	
		}
    
    
    @Test
    void testGetDate() throws SauvegardeImpossible ,  Erreurdate {
    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
    
  
    		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty"  , LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
			assertEquals( employe.getDateArrivee() , LocalDate.of(2023, 1, 1)); 
			assertEquals( employe.getDateDepart() , LocalDate.of(2023, 12, 31)); 
    		
    		
    	
    
        
    
}
    }


