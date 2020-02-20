import java.util.ArrayList;

class Container extends GameObject {

    public final Location containerID;
    public final ObjectType type = ObjectType.CONTAINER;
    public ArrayList<Item> inventory;


    private int capacity;
    private boolean open;
    private boolean locked;


    
    public Container(String name, Location loc, int cap, Location id)
    {
        super(name, loc);
        capacity = cap;
        containerID = id;
        inventory = new ArrayList<Item>();    
    }

    public void store(Item it)
    {
        inventory.add(it);
        it.setLocation(containerID);
    }

    public void remove(Item it)
    {
        if (inventory.contains(it))
        {
            inventory.remove(it);
            it.setLocation(Location.PLAYER_INVENTORY);
        }

        else
        {
            Game.output("There's no " + it.name + " in the " + this.name);
        }
    }

    public boolean isOpen() { return open; }
}