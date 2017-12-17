import java.util.ArrayList;

public class Ferry
{
    private String name;
    private int maxPassengers;
    private ArrayList<Bridge> bridges;

    private int nbPassengers;
    private boolean boarding;

    Ferry(String[] args)
    {
        // TODO: check args size

        name = args[0];
        maxPassengers = Integer.parseInt(args[1]);
        bridges = new ArrayList<>();

        for (int i = 2; i < args.length; i++)
            bridges.add( new Bridge(Integer.parseInt(args[i])) );

        nbPassengers = 0;
        boarding = true;

        print();
    }

    public void embark(ArrayList<Vehicle> vehicles)
    {
        if (!boarding)
        {
            // TODO: throw
        }

        for (Vehicle vehicle: vehicles)
            if (embark(vehicle))
                System.out.println("Vehicule embarqué pour un prix de: " + getTicketOf(vehicle).price);

        boarding = false;
    }

    private boolean embark(Vehicle vehicle)
    {
        for (Bridge bridge: bridges)
        {
            if ((vehicle.getPassengerCount() + nbPassengers <= maxPassengers) && bridge.embark(vehicle))
            {
                nbPassengers += vehicle.getPassengerCount();
                return true;
            }
        }

        return false;
    }

    public Ticket getTicketOf(Vehicle vehicle)
    {
        for (Bridge bridge: bridges)
        {
            if (bridge.contains(vehicle))
            {
                int price = 75 * vehicle.getSize() + 15 * vehicle.getPassengerCount();
                return new Ticket(name, price);
            }
        }

        return null;
    }

    public void print()
    {
        System.out.print("Ferry named \"" + name + "\" with " + maxPassengers + " passengers max and bridges of size:");

        for (Bridge bridge: bridges)
            System.out.print(" " + bridge.maxSize);

        System.out.println("");
    }

    class Bridge
    {
        int maxSize;
        int sizeLeft;

        ArrayList<Vehicle> embarkedVehicles;

        Bridge(int _maxSize)
        {
            maxSize = _maxSize;
            sizeLeft = maxSize;
            embarkedVehicles = new ArrayList<>();
        }

        boolean embark(Vehicle v)
        {
            if (v.getSize() <= sizeLeft)
            {
                sizeLeft -= v.getSize();
                embarkedVehicles.add(v);

                return true;
            }

            return false;
        }

        boolean contains(Vehicle v)
        {
            for (Vehicle vehicle: embarkedVehicles)
            {
                if (vehicle == v)
                    return true;
            }

            return false;
        }
    }

    class Ticket
    {
        String ferryName;
        int price;

        Ticket(String _ferryName, int _price)
        {
            ferryName = _ferryName;
            price = _price;
        }
    }
}
