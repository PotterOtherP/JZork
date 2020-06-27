import java.util.ArrayList;

class Surface extends GameObject {
    
    public int capacity;
    private GameState state;
    
    public Surface(String name, Location loc)
    {
        super(name, loc);
        type = ObjectType.SURFACE;
    
        capacity = 0;
        state = Game.gameState;
    }


    @Override
    public void put(GameState state)
    {
        Item it = (Item)(state.indirectObject);
        if (inventory.size() < capacity)
        {
            Game.output("Done.");
            it.location = inventoryID;
        }

        else
        {
            Game.output("There's no more room.");
        }

    }


    @Override
    public void remove(GameState state)
    {
        Item it = (Item)(state.indirectObject);
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