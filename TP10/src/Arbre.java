import java.util.ArrayList;
import java.util.Random;

public class Arbre
{	
	ArrayList<Arbre> children;

	char value;
	
	public Arbre(char _value)
	{
		value = _value;
		
		children = new ArrayList<Arbre>();
	}
	
	public void add(String _text)
	{		
		Arbre next = getDefault(_text.charAt(0));
		
		if (_text.length() == 1)
			return;
			
		next.add(_text.substring(1));
	}
	
	public Arbre get(char _value)
	{
		for (int i=0; i < children.size(); i++)
		{
			if (children.get(i).value == _value)
				return children.get(i);
		}
		
		return null;
	}
	
	public Arbre getDefault(char _value)
	{
		Arbre newChild = get(_value);
		
		if (newChild == null)
		{
			newChild = new Arbre(_value);
			children.add( newChild );
		}
		
		
		return newChild;
	}
	
	public boolean contains(String _text)
	{
		Arbre next = get(_text.charAt(0));
		
		if (next == null)
			return false;

		if (_text.length() == 1)
			return true;
			
		return next.contains(_text.substring(1));
	}
	
	public static String getString(Arbre _root)
	{
		StringBuilder b = new StringBuilder();
		
		_root.getRandom(new Random(), b);
		
		return b.toString();
	}
	
	public void getRandom(Random rand, StringBuilder b)
	{
		if (value != '_')
			b.append(value);
		
		if (children.size() == 0)
			return;
		
		int index = rand.nextInt(children.size());
		children.get(index).getRandom(rand, b);
	}
	
	public String toString()
	{
		if (value == '_')
			return "root";
		
		StringBuilder s = new StringBuilder();
		s.append(value);
		
		return s.toString();
	}
}
