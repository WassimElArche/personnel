package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import commandLineMenus.ListOption;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import personnel.Employe;
import personnel.Erreurdate;
import personnel.SauvegardeImpossible;

public class EmployeConsole 
{
	private Option afficher(final Employe employe)
	{
		return new Option("Afficher l'employé", "l", () -> {System.out.println(employe);});
	}

	ListOption<Employe> editerEmploye()
	{
		return (employe) -> editerEmploye(employe);		
	}

	Option editerEmploye(Employe employe)
	{
			Menu menu = new Menu("Gérer le compte " + employe.getNom(), "c");
			menu.add(afficher(employe));
			menu.add(changerNom(employe));
			menu.add(changerPrenom(employe));
			menu.add(changerMail(employe));
			menu.add(changerPassword(employe));
			menu.add(changerDateArriver(employe));
			menu.add(changerDateDepart(employe));
			menu.addBack("q");
		
			return menu;
	}
	
	private Option changerDateArriver(Employe employe) {
		return new Option("Changer date d'arriver", "a",
				() -> 
				{
					try {
						employe.setDateArrivee(LocalDate.parse(getString("Nouvelle date")));
					} catch (Erreurdate e) {
						// TODO Auto-generated catch block
						System.out.println("Les dates ne sont pas coherente : La date de depart ne peut pas etre avant la date d'arriver ");
					}
					catch (DateTimeParseException s) {
						System.out.println("Veuillez fournir le bon format de date sous cette forme : AAAA-MM-JJ");			} catch (SauvegardeImpossible e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		);}
	
	private Option changerDateDepart(Employe employe) {
		return new Option("Changer date Depart", "d",
				() -> 
				{
					try {
						employe.setDateDepart(LocalDate.parse(getString("Nouvelle date")));
					} catch (Erreurdate e) {
						// TODO Auto-generated catch block
						System.out.println("Les dates ne sont pas coherente : La date de depart ne peut pas etre avant la date d'arriver ");
					}
					catch (DateTimeParseException s) {
						System.out.println("Veuillez fournir le bon format de date sous cette forme : AAAA-MM-JJ");			} catch (SauvegardeImpossible e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		);}

	
	//Veuillez saisir la date sous cette forme : AAAA-MM-JJ
	private Option changerNom(final Employe employe)
	{
		return new Option("Changer le nom", "n", 
				() -> {try {
					employe.setNom(getString("Nouveau nom : "));
				} catch (SauvegardeImpossible e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
			);
	}
	
	private Option changerPrenom(final Employe employe)
	{
		return new Option("Changer le prénom", "p", () -> {try {
			employe.setPrenom(getString("Nouveau prénom : "));
		} catch (SauvegardeImpossible e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}});
	}
	
	private Option changerMail(final Employe employe)
	{
		return new Option("Changer le mail", "e", () -> {try {
			employe.setMail(getString("Nouveau mail : "));
		} catch (SauvegardeImpossible e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}});
	}
	
	private Option changerPassword(final Employe employe)
	{
		return new Option("Changer le password", "x", () -> {try {
			employe.setPassword(getString("Nouveau password : "));
		} catch (SauvegardeImpossible e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}});
	}
	

}
