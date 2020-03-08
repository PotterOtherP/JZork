import java.util.ArrayList;

class Surface extends GameObject {
    
    public final Location surfaceID;


    public int capacity;
    
    public Surface(String name, Location loc, int cap, Location id)
    {
        super(name, loc);
        capacity = cap;
        surfaceID = id;
        inventory = new ArrayList<Item>();
        type = ObjectType.SURFACE;    
    }

    @Override
    public void place(GameState state, Item it)
    {
        if (inventory.size() < capacity)
        {
            it.location = surfaceID;
        }

        else
        {
            Game.output("There's no more room.");
        }
    }

    @Override
    public void remove(GameState state, Item it)
    {
        
        if (inventory.contains(it))
        {
            inventory.remove(it);
            it.location = Location.PLAYER_INVENTORY;
            Game.output("Taken.");
        }

        else
        {
            Game.output("There's no " + it.name + " on the " + name);
        }
        
    }

}