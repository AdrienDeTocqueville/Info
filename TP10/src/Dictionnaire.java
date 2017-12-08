import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class Dictionnaire
{
	Path path;
	ArrayList<String> words = new ArrayList<String>();
	
	Dictionnaire(String _fileName)
	{
		path = Paths.get(_fileName);
		
		Charset charset = Charset.forName("UTF8");
		try (BufferedReader reader = Files.newBufferedReader(path, charset))
		{
			String line = null;
			
			while ((line = reader.readLine()) != null)
				words.add(line);
		}
		
		catch (IOException x)
		{
			System.err.format("IOException: %s%n", x);
		}
	}
	
	String get()
	{
		Random rand = new Random();
		int index = rand.nextInt(words.size());
		
		return words.get(index);
	}
	
	boolean find(String _word)
	{
		for (int i = 0; i < words.size(); i++)
		{
			if (words.get(i).equals(_word))
				return true;
		}
		
		return false;
	}
}
