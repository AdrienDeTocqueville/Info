package ex3;

public class Festival
{
	private static int GROUPES_MAX = 10;
	
	private String style;

	private Groupe[] groupes;
	private int nbGroupes;
	
	public Festival(String style)
	{
		this.style = style;

		groupes = new Groupe[GROUPES_MAX];
	}
	
	public boolean inviter(Groupe groupe)
	{
		if (groupe.style == style && nbGroupes < groupes.length)
		{
			groupes[nbGroupes++] = groupe;
			return true;
		}
		
		return false;
	}
}
