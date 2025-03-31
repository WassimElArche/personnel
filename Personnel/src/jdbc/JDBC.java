package jdbc; 
public class JDBC implements personnel.Passerelle { 
    public personnel.GestionPersonnel getGestionPersonnel() { return null; } 
    public void sauvegarderGestionPersonnel(personnel.GestionPersonnel gestionPersonnel) {} 
    public int insert(personnel.Ligue ligue) { return 0; } 
    public void update(personnel.Ligue ligue) {} 
    public int insert(personnel.Employe employe) { return 0; } 
    public void update(personnel.Employe employe) {} 
    public void delete(personnel.Employe employe) {} 
    public void delete(personnel.Ligue ligue) {} 
} 
