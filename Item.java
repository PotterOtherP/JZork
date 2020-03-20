class Item extends GameObject{

	// Items can be picked up and moved to other locations, including the player's inventory.

	public int weight;
	public int acquirePointValue;
	public int trophyCaseValue;
	

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
	}

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
	public void light(GameState state)
	{
		switch (name)
		{
			case "lantern":
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




	@Override
	public boolean isAlive() { return lifespan > 0; }

	@Override
	public void tick() { --lifespan; }
	
}