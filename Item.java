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
	public boolean isAlive() { return lifespan > 0; }

	public void tick() { --lifespan; }
	
}