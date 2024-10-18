package personnel;

class ErreurDate extends Exception
{
	public String getMessage()
	{
		return "La datee de départ ne peut pas être avant la date d'arrivée.";
	}
} 
