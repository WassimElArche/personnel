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
			boolean verif = employe.getLigue() == null && !t.next();

			
			String sql = "insert into employe (prenomEmploye , nomEmploye , mail , passwd , datearv , datedepart , Admin , ID_Ligue ) values(?,?,?,?,?,?,?,?)";
			if(verif) {
				sql = "insert into employe (ID_Employe, nomEmploye , passwd ) values(?,?,?)";
			}
			
			//System.out.println(sql);
			
			
			PreparedStatement instruction;
			instruction = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if(verif) {
				
				instruction.setInt(1,1);
				instruction.setString(2, employe.getNom());
				instruction.setString(3,employe.getPassword());
			}
			else if (employe.getLigue() != null){
			
			instruction.setString(1, employe.getPrenom());	
			instruction.setString(2, employe.getNom());
			instruction.setString(3, employe.getMail());
			instruction.setString(4, employe.getPassword());
			instruction.setString(5,employe.getDateArrivee().toString());
			instruction.setString(6, employe.getDateDepart().toString());
			instruction.setBoolean(7, employe.getAdmin());
			instruction.setInt(8, employe.getLigue().getId());}
			else return 0;
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			
			
			return id.getInt(1);
		
			
			
			
			
			
			
			
			
			
			
			
			
			/*	Brouillon a Supprimer si au dessus marche 
			 * 
			 * PreparedStatement instruction1;
			instruction1 = connection.prepareStatement("select * from employe where ID_Ligue is null");
			ResultSet t = instruction1.executeQuery();			
			boolean verifAdmin = employe.getLigue() == null && !t.next();
			
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into employe(prenomEmploye , nomEmploye , mail , passwd , datearv , datedepart , Admin , ID_Ligue) values(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			
			if (verifAdmin) {
			instruction.setString(2, employe.getNom());
			instruction.setString(4, employe.getPassword());
				for(int i = 1 ; i<=8 ; i++) {
					if (i != 4 && i != 2) {
						instruction.setString(i, null);	
					}
					
				}
				System.out.println("OK");
			}
			
			else if(!verifAdmin && employe.getDateArrivee() != null)
			{
			instruction.setString(2, employe.getNom());
			instruction.setString(4, employe.getPassword());
			instruction.setString(3, employe.getMail());
			instruction.setString(1, employe.getPrenom());
			instruction.setString(5,employe.getDateArrivee().toString());
			instruction.setString(6, employe.getDateDepart().toString());
			instruction.setBoolean(7, employe.getAdmin());
			instruction.setInt(8, employe.getLigue().getId());
			}
			
			System.out.println("KOKO");
			
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			
			
			return id.getInt(1);*/
			
			
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
		
	}
	
	
	public int update(Ligue ligue)throws SauvegardeImpossible {
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("update ligue set nomLigue = (?) WHERE ID_Ligue = (?)", Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, ligue.getNom());
			instruction.setInt(2, ligue.getId());
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			if(id.next())
			return id.getInt(1);
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
			instruction = connection.prepareStatement("insert into ligue (nomLigue) values(?)", Statement.RETURN_GENERATED_KEYS);
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
