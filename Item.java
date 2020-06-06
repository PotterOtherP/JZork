class Item extends GameObject{

	// Items can be picked up and moved to other locations, including the player's inventory.

	public int weight;
	public int acquirePointValue;
	public int trophyCaseValue;

	public int capacity;
    public boolean open;
    public boolean locked;
	

	// For items that can be turned on and expire, like the lamp
	public boolean activated;
	public int lifespan;


	public Item(String name, Location loc)
	{
		super(name, loc);
		type = ObjectType.ITEM;

		acquirePointValue = 0;
		trophyCaseValue = 0;
		weight = 0;
		activated = false;
		lifespan = 0;
		movedFromStart = false;

		capacity = 0;
		open = false;
		locked = false;
	}

	@Override
	public boolean isContainer() { return inventoryID != Location.NULL_INVENTORY; }

	@Override
	public void take(GameState state)
	{

		if ((state.playerCarryWeight + weight) >= state.playerMaxCarryWeight)
        {
            Game.output(GameStrings.OVERBURDENED);
            return;
        }

		state.playerCarryWeight += weight;
		location = Location.PLAYER_INVENTORY;
		Game.output("Taken.");

		movedFromStart = true;

	}

	@Override
	public void drop(GameState state)
	{
		state.playerCarryWeight -= weight;
		location = state.playerLocation;
		Game.output("Dropped.");
	}

	@Override
    public void open(GameState state)
    {
    	if (!isContainer())
    	{
    		Game.output(openString);
    		return;
    	}

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
                String str = "Opening the " + name + " reveals ";

                for (int i = 0; i < inventory.size(); ++i)
                {
                    Item it = inventory.get(i);

                    if (inventory.size() > 1 && i == inventory.size() - 1) str  += " and ";
                    str += it.articleName;
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
    	if (!isContainer())
    	{
    		Game.output(closeString);
    		return;
    	} 

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


    @Override
    public void place(GameState state, Item it)
    {

    	if (!isContainer())
    	{
    		Game.output(putString);
    		return;
    	}

        if (open)
        {
            inventory.add(it);
            it.location = inventoryID;
            Game.output("Done.");
        }
        else
        {
            Game.output("The " + name + " isn't open.");
        }
    }

    @Override
    public void remove(GameState state, Item it)
    {

    	if (!isContainer())
    	{
    		Game.output("You can't remove that from the " + name);
    		return;
    	}

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
    public void examine(GameState state)
    {
    	if (!isContainer())
    	{
    		Game.output(examineString);
    		return;
    	}

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
    public boolean isOpen() { return open; }

	@Override
	public void light(GameState state)
	{
		switch (name)
		{
			case "brass lantern":
			{
				if (!activated && lifespan > 0)
                {
                    activated = true;
                    state.lightActivated = true;
                    Game.output("The brass lantern is now on.");
                    Room rm = state.worldMap.get(state.playerLocation);
                    if (rm.isDark()) rm.lookAround(state);
                    examineString = "The lamp is on.";
                }

                else if (!activated && lifespan <= 0)
                {
                    Game.output("A burned-out lamp won't light.");
                }

                else
                {
                    Game.output("It is already on.");
                }

			} break;

			default:
			{
				Game.output(lightString);
			} break;
		}
	}


	@Override
	public void extinguish(GameState state)
	{
		switch (name)
		{
			case "lantern":
			{
				if (activated)
                {
                    activated = false;
                    Game.output("The brass lantern is now off.");

                    Room rm = state.worldMap.get(state.playerLocation);
                    if (rm.isDark())
                    {
                        Game.output("It is now pitch black.");
                    }
                }

                else
                {
                    Game.output("It is already off.");
                }
			} break;

			default:
			{
				Game.output(extinguishString);
			} break;
		}
	}


    public String getItemDescription()
    {
        if (movedFromStart || initialPresenceString.isEmpty())
        {
            return presenceString;
        }

        else
        {
            return initialPresenceString;
        }
    }


	@Override
	public boolean isAlive() { return lifespan > 0; }

	@Override
	public void tick() { --lifespan; }

	public String toString() { return name; }
	
}