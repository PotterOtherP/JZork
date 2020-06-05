import java.util.ArrayList;

class Surface extends GameObject {
    


    public int capacity;
    
    public Surface(String name, Location loc)
    {
        super(name, loc);
        type = ObjectType.SURFACE;
        
        capacity = 0;
    }

    @Override
    public void place(GameState state, Item it)
    {
        if (inventory.size() < capacity)
        {
            it.location = inventoryID;
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

    public String toString() { return name; }

}