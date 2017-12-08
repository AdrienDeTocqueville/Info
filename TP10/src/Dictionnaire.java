import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Dictionnaire
{
	Path path;
	
	Arbre root = new Arbre('_');
	
	public Dictionnaire(String _fileName)
	{
		path = Paths.get(_fileName);
		
		Charset charset = Charset.forName("UTF8");
		try (BufferedReader reader = Files.newBufferedReader(path, charset))
		{
			String line = null;
			
			while ((line = reader.readLine()) != null)
				root.add(line);
		}
		
		catch (IOException x)
		{
			System.err.format("IOException: %s%n", x);
		}
	}
	
	public String get()
	{
		return Arbre.getString(root);
	}
	
	public boolean find(String _word)
	{		
		return root.contains(_word);
	}
}
