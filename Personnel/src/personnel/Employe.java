package personnel;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.TreeSet;

/**
 * Employé d'une ligue hébergée par la M2L. Certains peuvent
 * être administrateurs des employés de leur ligue.
 * Un seul employé, rattaché à aucune ligue, est le root.
 * Il est impossible d'instancier directement un employé,
 * il faut passer la méthode {@link Ligue#addEmploye addEmploye}.
 */

public class Employe implements Serializable, Comparable<Employe>
{
    private static final long serialVersionUID = 4795721718037994734L;
    private int id;
    private String nom, prenom, password, mail;
    private Ligue ligue;
    private GestionPersonnel gestionPersonnel;
    private boolean admin;
    private LocalDate dateArrive;
    private LocalDate dateDepart;

    Employe(GestionPersonnel gestionPersonnel, Ligue ligue, String nom, String prenom, String mail, String password, LocalDate dateArrive, LocalDate dateDepart , boolean admin )
            throws Erreurdate, SauvegardeImpossible 
    {
        this.gestionPersonnel = gestionPersonnel;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.mail = mail;
        this.ligue = ligue;

        if (dateArrive == null || dateDepart == null || dateDepart.isBefore(dateArrive) ) {
            throw new Erreurdate();
        }

        this.dateArrive = dateArrive;
        this.dateDepart = dateDepart;
        this.id = gestionPersonnel.insert(this);
        this.admin = admin;
        
    }
    
    
    
    
    // 
    public Employe(GestionPersonnel gestion , String nom , String password) throws SauvegardeImpossible{
    	this.gestionPersonnel = gestion;
    	this.nom = nom;
    	this.password = password;
    	this.id = gestion.insert(this);
    }
    
    
    
    
    //Pr la lecture d'un employé
    Employe(GestionPersonnel gestionPersonnel, Ligue ligue, String nom, String prenom, String mail, String password, LocalDate dateArrive, LocalDate dateDepart , boolean admin , int id ) throws Erreurdate, SauvegardeImpossible
	{
    	 this.gestionPersonnel = gestionPersonnel;
         this.nom = nom;
         this.prenom = prenom;
         this.password = password;
         this.mail = mail;
         this.ligue = ligue;

         if (dateArrive == null || dateDepart == null || dateDepart.isBefore(dateArrive) ) {
             throw new Erreurdate();
         }

         this.dateArrive = dateArrive;
         this.dateDepart = dateDepart;
         this.id = id;
         this.admin = admin;
	}
    
    
    
    
    
    public boolean getAdmin() {
    	return this.admin;
    }




    //Pour la lectur du root
    public Employe(GestionPersonnel gestionPersonnel, String nom, String password, int id) throws SauvegardeImpossible {
    	
		this.gestionPersonnel = gestionPersonnel;
		this.nom = nom;
		this.password = password;
		this.id = id;
		this.ligue = null;
		gestionPersonnel.insert(this);
	}

    public int getID() {
    	return this.id;
    }
    
	/**
     * Retourne vrai ssi l'employé est administrateur de la ligue
     * passée en paramètre.
     * @return vrai ssi l'employé est administrateur de la ligue
     * passée en paramètre.
     * @param ligue la ligue pour laquelle on souhaite vérifier si this
     * est l'admininstrateur.
     */

    public boolean estAdmin(Ligue ligue)
    {
        return ligue.getAdministrateur() == this;
    }

    /**
     * Retourne vrai ssi l'employé est le root.
     * @return vrai ssi l'employé est le root.
     */

    public boolean estRoot()
    {
        return gestionPersonnel.getRoot() == this;
    }

    /**
     * Retourne le nom de l'employé.
     * @return le nom de l'employé.
     */

    public String getNom()
    {
        return nom;
    }

    /**
     * Change le nom de l'employé.
     * @param nom le nouveau nom.
     * @throws SauvegardeImpossible 
     */

    public void setNom(String nom) throws SauvegardeImpossible
    {
        this.nom = nom;
        gestionPersonnel.update(this);
    }

    /**
     * Retourne le prénom de l'employé.
     * @return le prénom de l'employé.
     */

    public String getPrenom()
    {
        return this.prenom;
    }

    /**
     * Change le prénom de l'employé.
     * @param prenom le nouveau prénom de l'employé.
     * @throws SauvegardeImpossible 
     */

    public void setPrenom(String prenom) throws SauvegardeImpossible
    {
        this.prenom = prenom;
        gestionPersonnel.update(this);
    }

    /**
     * Retourne le mail de l'employé.
     * @return le mail de l'employé.
     */

    public String getMail()
    {
        return this.mail;
    }

    /**
     * Change le mail de l'employé.
     * @param mail le nouveau mail de l'employé.
     * @throws SauvegardeImpossible 
     */

    public void setMail(String mail) throws SauvegardeImpossible
    {
        this.mail = mail;
        gestionPersonnel.update(this);
    }
    /**
     * Retourne la date d'arrivée de l'employé.
     * @return la date d'arrivée de l'employé.
     */

    public LocalDate getDateArrivee()
    {
        return this.dateArrive;
    }

    /**
     * Retourne la date d'arrivée de l'employé.
     * @return la date d'arrivée de l'employé.
     * @throws SauvegardeImpossible 
     */

    public void setDateArrivee(LocalDate dateArrive)
            throws Erreurdate, SauvegardeImpossible
    {
        if (dateArrive == null  || dateDepart.isBefore(dateArrive)) {
            throw new Erreurdate();
        }
        else {

            this.dateArrive = dateArrive;}
        gestionPersonnel.update(this);
    }

    /**
     * Modifie la date de départ de l'employé.
     * @return la date de départ de l'employé.
     */

    public LocalDate getDateDepart()
    {
        return this.dateDepart;
    }

    /**
     * Modifie la date de départ de l'employé.
     * @return la date de départ de l'employé.
     * @throws SauvegardeImpossible 
     */

    public void setDateDepart(LocalDate dateDepart)
            throws Erreurdate, SauvegardeImpossible
    {
        if (dateDepart == null || dateDepart.isBefore(dateArrive)) {
            throw new Erreurdate();
        }
        else {
            this.dateDepart = dateDepart;}
        gestionPersonnel.update(this);
    }


    /**
     * Retourne vrai ssi le password passé en paramètre est bien celui
     * de l'employé.
     * @return vrai ssi le password passé en paramètre est bien celui
     * de l'employé.
     * @param password le password auquel comparer celui de l'employé.
     */

    public boolean checkPassword(String password)
    {
        return this.password.equals(password);
    }

    /**
     * Change le password de l'employé.
     * @param password le nouveau password de l'employé.
     * @throws SauvegardeImpossible 
     */

    public void setPassword(String password) throws SauvegardeImpossible
    {
        this.password= password;
        gestionPersonnel.update(this);
    }

    /**
     * Retourne la ligue à laquelle l'employé est affecté.
     * @return la ligue à laquelle l'employé est affecté.
     */

    public Ligue getLigue()
    {
        return this.ligue;
    }

    /**
     * Supprime l'employé. Si celui-ci est un administrateur, le root
     * récupère les droits d'administration sur sa ligue.
     * @throws SauvegardeImpossible 
     */

    public void remove() throws SauvegardeImpossible
    {
        Employe root = gestionPersonnel.getRoot();
        if (this != root)
        {
            if (estAdmin(getLigue()))
                getLigue().setAdministrateur(root);
            getLigue().remove(this);
        }
        else
            throw new ImpossibleDeSupprimerRoot();
    }
    
    public String getPassword() {
    	return this.password;
    }

    @Override
    public int compareTo(Employe autre)
    {
        int cmp = getNom().compareTo(autre.getNom());
        if (cmp != 0)
            return cmp;
        return getPrenom().compareTo(autre.getPrenom());
    }

    @Override
    public String toString()
    {
        String res = nom + " " + prenom + " " + mail + " (";
        if (estRoot())
            res += "super-utilisateur";
        else
            res += ligue.toString();
        return res + ")";
    }
}