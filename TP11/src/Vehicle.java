public class Vehicle
{
    private int size;
    private int nbPassengers;

    Vehicle(String[] args)
    {
        // TODO: check args size

        nbPassengers = Integer.parseInt(args[1]);

        switch (args[0])
        {
            case "V":
                size = 1;
                break;

            case "B":
                size = Integer.parseInt(args[2]);
                break;

            default:
                // TODO
        }

        // Print data
        System.out.println("New " + args[0] + " of size: " + size + " with " + nbPassengers + " passengers");
    }

    public int getSize()
    {
        return size;
    }

    public int getPassengerCount()
    {
        return nbPassengers;
    }
}
