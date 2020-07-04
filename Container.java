import java.util.ArrayList;

class Container extends GameObject {

    public int capacity;
    public boolean open;
    public boolean locked;


    public Container(String name, Location loc)
    {
        super(name, loc);
        type = ObjectType.CONTAINER;

        capacity = 0;
        open = false;
        
    }


    @Override
    public void close(GameState state)
    { 
        switch (name)
        {
            default:
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
            } break;
        }
        

    }


    @Override
    public void examine(GameState state)
    {
        if (open)
        {
            if (inventory.size() == 0)
                Game.output("The " + name + " is empty.");
            else
            {
                Game.output("The " + name + " contains:");

                for (int i = 0; i < inventory.size(); ++i)
                {
                    Item it = inventory.get(i);
                    Game.output("  " + it.capArticleName);
                }
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
    public void put(GameState state)
    {
        if (open)
        {
            int currentWeight = 0;
            for (Item it : inventory)
            {
                currentWeight += it.weight;
            }

            Item obj = (Item)(state.indirectObject);

            if (currentWeight + obj.weight <= capacity)
            {
                inventory.add(obj);
                obj.location = inventoryID;
                Game.output("Done.");
            }

            else
                Game.output("There's no more room.");
        }
        
        else
        {
            Game.output("The " + name + " isn't open.");
        }

    }


    @Override
    public void remove(GameState state)
    {
        Item it = (Item)(state.indirectObject);
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
                Game.output("There's no " + it.name + " in the " + name);
            }
        }

        else
        {
            Game.output("The " + name + " is closed.");
        }
        
    }


    @Override
    public boolean isContainer() { return true; }

    @Override
    public boolean isOpen() { return open; }

    public String toString() { return name; }

}