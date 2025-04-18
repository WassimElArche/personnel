package personnel;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Gestion du personnel. Un seul objet de cette classe existe.
 * Il n'est pas possible d'instancier directement cette classe, 
 * la méthode {@link #getGestionPersonnel getGestionPersonnel} 
 * le fait automatiquement et retourne toujours le même objet.
 * Dans le cas où {@link #sauvegarder()} a été appelé lors 
 * d'une exécution précédente, c'est l'objet sauvegardé qui est
 * retourné.
 */

public class GestionPersonnel implements Serializable
{
	private static final long serialVersionUID = -105283113987886425L;
	private static GestionPersonnel gestionPersonnel = null;
	private SortedSet<Ligue> ligues;
	private Employe root;
	public final static int SERIALIZATION = 1, JDBC = 2, 
			TYPE_PASSERELLE = JDBC;  
	private static Passerelle passerelle = TYPE_PASSERELLE == JDBC ? new jdbc.JDBC() : new serialisation.Serialization();	
	
	/**
	 * Retourne l'unique instance de cette classe.
	 * Crée cet objet s'il n'existe déjà.
	 * @return l'unique objet de type {@link GestionPersonnel}.
	 * @throws SauvegardeImpossible 
	 */
	
	public static GestionPersonnel getGestionPersonnel() throws SauvegardeImpossible
	{
		if (gestionPersonnel == null)
		{
			gestionPersonnel = passerelle.getGestionPersonnel();
			if (gestionPersonnel == null)
				gestionPersonnel = new GestionPersonnel();
		}
		return gestionPersonnel;
	}
	
	
	public void addRoot(GestionPersonnel gestion , String nom, String password , int id) throws SauvegardeImpossible{
		
	 Employe employe = new Employe(this , nom  , password, id);
	 this.root = employe;
	 
	}
	
	
	
	public void addRoot() throws SauvegardeImpossible {
		
		Employe employe = new Employe(this , "root" , "toor");
		this.root = employe;
	}
	
	

	
	
	

	public GestionPersonnel() throws SauvegardeImpossible
	{
		if (gestionPersonnel != null)
			throw new RuntimeException("Vous ne pouvez créer qu'une seuls instance de cet objet.");
		ligues = new TreeSet<>();
		gestionPersonnel = this;
		if (this.getRoot() == null) {
			this.addRoot();
		}
		
	}
	
	
	
	
	public void sauvegarder() throws SauvegardeImpossible
	{
		passerelle.sauvegarderGestionPersonnel(this);
	}
	
	/**
	 * Retourne la ligue dont administrateur est l'administrateur,
	 * null s'il n'est pas un administrateur.
	 * @param administrateur l'administrateur de la ligue recherchée.
	 * @return la ligue dont administrateur est l'administrateur.
	 */
	
	public Ligue getLigue(Employe administrateur)
	{
		if (administrateur.estAdmin(administrateur.getLigue()))
			return administrateur.getLigue();
		else
			return null;
	}

	/**
	 * Retourne toutes les ligues enregistrées.
	 * @return toutes les ligues enregistrées.
	 */
	
	public SortedSet<Ligue> getLigues()
	{
		return Collections.unmodifiableSortedSet(ligues);
	}

	public Ligue addLigue(String nom) throws SauvegardeImpossible
	{
		Ligue ligue = new Ligue(this, nom); 
		ligues.add(ligue);
		return ligue;
	}
	
	public Ligue addLigue(int id, String nom)
	{
		Ligue ligue = new Ligue(this, id, nom);
		ligues.add(ligue);
		return ligue;
	}

	void remove(Ligue ligue)
	{
		ligues.remove(ligue);
	}
	
	/*int addRoot() throws SauvegardeImpossible {
		return passerelle.insert(root);
	}*/
	
	int insert(Ligue ligue) throws SauvegardeImpossible
	{
		return passerelle.insert(ligue);
	}
	
	void update(Ligue ligue) throws SauvegardeImpossible {
		passerelle.update(ligue);
	}
	
	
	
	int insert(Employe employe) throws SauvegardeImpossible{
		return passerelle.insert(employe);
	}

	
	void update(Employe employe) throws SauvegardeImpossible {
		passerelle.update(employe);
	}
	/**
	 * Retourne le root (super-utilisateur).
	 * @return le root.
	 */
	
	public Employe getRoot()
	{
		return root;
	}
	
	public void delete(Employe employe) throws SauvegardeImpossible {
		 passerelle.delete(employe);
	}
	
	public void delete(Ligue ligue) throws SauvegardeImpossible{
		
		Iterator<Employe> iterator = ligue.getEmployes().iterator();
		while (iterator.hasNext()) {
		    Employe employe = iterator.next();
		    employe.remove();
		   }
		passerelle.delete(ligue);
	}
	
	
	public void setAdmin(Employe employe) throws SauvegardeImpossible {

		passerelle.update(employe);
	}
}
