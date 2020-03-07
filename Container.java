import java.util.ArrayList;

class Container extends GameObject {

    public final Location containerID;


    public int capacity;
    public boolean open;
    public boolean locked;


    
    public Container(String name, Location loc, int cap, Location id)
    {
        super(name, loc);
        capacity = cap;
        containerID = id;
        open = false;
        inventory = new ArrayList<Item>();
        this.type = ObjectType.CONTAINER;    
    }

    @Override
    public void place(GameState state, Item it)
    {
        if (open)
        {
            inventory.add(it);
            it.location = containerID;
            Game.output("You put the " + it.name + " in the " + this.name + ".");
        }
        else
        {
            Game.output("The " + name + " is closed.");
        }
    }

    @Override
    public void remove(GameState state, Item it)
    {
        if (open)
        {
            if (inventory.contains(it))
            {
                inventory.remove(it);
                it.location = Location.PLAYER_INVENTORY;
                Game.output("Taken.");
            }

            else
            {
                Game.output("There's no " + it.name + " in the " + this.name);
            }
        }

        else
        {
            Game.output("The " + name + " is closed.");
        }
        
    }

    @Override
    public void open(GameState state)
    {
        if (open)
        {
            Game.output("It is already open.");
        }
        else
        {
            open = true;
            if (inventory.isEmpty())
                Game.output("Opened.");
            else
            {
                String str = "Opening the " + name + " reveals";

                for (int i = 0; i < inventory.size(); ++i)
                {
                    Item it = inventory.get(i);

                    String word = it.vowelStart() ? " an " : " a ";
                    if (inventory.size() > 1 && i == inventory.size() - 1) word = " and" + word;
                    str += word;
                    str += it.name;
                    if (inventory.size() > 2 && i < inventory.size() - 1)
                        str += ",";
                }
                
                str += ".";

                Game.output(str);
            }
        }
    }

    @Override
    public void close(GameState state)
    { 
        if (open)
        {
            open = false; 
            Game.output("Closed.");
        }
        else
        {
            Game.output("It is already closed.");
        }

    }
    public boolean isOpen() { return open; }
}