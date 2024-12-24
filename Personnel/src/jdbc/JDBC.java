package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import personnel.*;

public class JDBC implements Passerelle 
{
	 Connection connection;

	public JDBC()
	{
		try
		{
			Class.forName(Credentials.getDriverClassName());
			connection = DriverManager.getConnection(Credentials.getUrl(), Credentials.getUser(), Credentials.getPassword());
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Pilote JDBC non installé.");
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	@Override
	public GestionPersonnel getGestionPersonnel() throws SauvegardeImpossible
	{
		GestionPersonnel gestionPersonnel = new GestionPersonnel();
		try 
		{
			
			
			PreparedStatement instruction1;
			instruction1 = connection.prepareStatement("select id_employe , nomEmploye , passwd from employe where id_ligue is null");
			ResultSet resultat = instruction1.executeQuery();
			
			//for( int i = 0 ; i<=3 ; i++){System.out.println(resultat.getString(i));}
			
			
			if(resultat != null && resultat.next()) {
			gestionPersonnel.addRoot(gestionPersonnel, resultat.getString(2), resultat.getString(3), resultat.getInt(1));
			//System.out.println("Debug");
			}
			else {
				gestionPersonnel.addRoot();
				//System.out.println("test debug");
			}
			
		
			
			
			
			String requete = "select * from ligue";
			Statement instruction = connection.createStatement();
			ResultSet ligues = instruction.executeQuery(requete);
			while (ligues.next()) {
				gestionPersonnel.addLigue(ligues.getInt(1), ligues.getString(2));}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return gestionPersonnel;
		
	
		
	}

	@Override
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible 
	{
		close();
	}
	
	public void close() throws SauvegardeImpossible
	{
		try
		{
			if (connection != null)
				connection.close();
		}
		catch (SQLException e)
		{
			throw new SauvegardeImpossible(e);
		}
	}
	
	public int insert(Employe employe)throws SauvegardeImpossible{
		

		try 
		{
			PreparedStatement instruction1;
			instruction1 = connection.prepareStatement("select * from employe where id_employe = 1");
			ResultSet t = instruction1.executeQuery();
			

			
			if(employe.getLigue() == null && !t.next()) {
				
				
				PreparedStatement instruction;
				instruction = connection.prepareStatement("insert into employe (ID_Employe , prenomEmploye , nomEmploye , mail , passwd , datearv , datedepart , Admin ) values(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				instruction.setString(2, null);	
				instruction.setString(3, employe.getNom());
				instruction.setString(4, null);
				instruction.setString(5, employe.getPassword());
				instruction.setString(6,null);
				instruction.setString(7, null);
				instruction.setBoolean(8, true);
				instruction.setInt(1, 1);
				instruction.executeUpdate();
				ResultSet id = instruction.getGeneratedKeys();
				id.next();
				
				return id.getInt(1);
				
			}
			
			else if (employe.getLigue() != null){
			
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into employe (prenomEmploye , nomEmploye , mail , passwd , datearv , datedepart , Admin , ID_Ligue ) values(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, employe.getPrenom());	
			instruction.setString(2, employe.getNom());
			instruction.setString(3, employe.getMail());
			instruction.setString(4, employe.getPassword());
			instruction.setString(5,employe.getDateArrivee().toString());
			instruction.setString(6, employe.getDateDepart().toString());
			instruction.setBoolean(7, employe.getAdmin());
			instruction.setInt(8, employe.getLigue().getId());
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			
			
			return id.getInt(1);}
			return 0;
			
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
		
	}
	
	
	
	@Override
	public int insert(Ligue ligue) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into ligue (nom) values(?)", Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, ligue.getNom());		
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}
}
