import java.util.ArrayList;

class Container extends GameObject {

    public int capacity;
    public boolean open;
    public boolean locked;
    private GameState state;


    public Container(String name, Location loc)
    {
        super(name, loc);
        type = ObjectType.CONTAINER;

        capacity = 0;
        open = false;
        state = super.state;
        
    }


    @Override
    public void close()
    { 
        switch (name)
        {
            case "basket":
            {
                Game.output("There is no way to close the basket.");

            } break;

            case "machine":
            {
                if (open)
                {
                    open = false;
                    Game.output("The lid closes.");
                }

                else
                    Game.output("The lid is already closed.");
            } break;

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
    public void examine()
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
    public void lower()
    {
        switch (name)
        {
            case "basket":
            {
                if (!state.shaftBasketLowered)
                {
                    state.shaftBasketLowered = true;
                    location = Location.DRAFTY_ROOM;
                    altLocations.clear();
                    altLocations.add(Location.SHAFT_ROOM);
                    Game.output("The basket is lowered to the bottom of the shaft.");
                }

                else
                    Game.output(GameStrings.getHardSarcasm());

            } break;

            default:
            {
                super.lower();
            } break;
        }
    }


    @Override
    public void open()
    {
        switch (name)
        {
            case "machine":
            {
                if (!open)
                {
                    open = true;

                    if (!inventory.isEmpty())
                        Game.output("The lid opens, revealing " + inventory.get(0).articleName + ".");
                    else
                        Game.output("The lid opens.");
                }

                else
                    Game.output("The lid is already open.");
            } break;

            default:
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

            } break;

        }

    }


    @Override
    public void put()
    {
        if (name.equals("machine") && !inventory.isEmpty())
        {
            Game.output("There's no more room.");
            return;
        }

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
    public void raise()
    {
        switch (name)
        {
            case "basket":
            {
                if (state.shaftBasketLowered)
                {
                    state.shaftBasketLowered = false;
                    location = Location.SHAFT_ROOM;
                    altLocations.clear();
                    altLocations.add(Location.DRAFTY_ROOM);
                    Game.output("The basket is raised to the top of the shaft.");
                }

                else
                    Game.output(GameStrings.getHardSarcasm());

            } break;

            default:
            {
                super.raise();
            } break;
        }
    }


    @Override
    public void remove()
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