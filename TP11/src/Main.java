import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main
{
    static Ferry ferry = null;
    static ArrayList<Vehicle> vehicles = new ArrayList<>();

    public static void main(String[] args)
    {
        loadFile("data.txt");

        ferry.embark(vehicles);
    }

    public static void handleEntry(String _entry)
    {
        String[] args = _entry.split("\\s+");

        switch (args[0])
        {
            case "V":
            case "B":
                vehicles.add( new Vehicle(args) );
                break;

            default:
                ferry = new Ferry(args);
        }
    }

    public static void loadFile(String _fileName)
    {
        Path path = Paths.get(_fileName);

        Charset charset = Charset.forName("UTF8");
        try (BufferedReader reader = Files.newBufferedReader(path, charset))
        {
            String line;

            while ((line = reader.readLine()) != null)
                handleEntry(line);
        }

        catch (IOException x)
        {
            System.err.format("IOException: %s%n", x);
        }
    }
}
