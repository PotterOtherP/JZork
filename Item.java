class Item extends GameObject{

	// Items can be picked up and moved to other locations, including the player's inventory.

	public final int pointValue;
	public final int weight;

	// For items that can be turned on and expire, like the lamp
	public boolean activated;
	public int lifespan;
	public int life;
	public boolean movedFromStart;


	public Item(String name, Location loc, int value, int wt)
	{
		super(name, loc);
		pointValue = value;
		weight = wt;
		activated = false;
		lifespan = 0;
		life = 0;
		type = ObjectType.ITEM;
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


	public void setLocation(Location loc) { location = loc; }
	public Location getLocation() { return location; }
	public int getValue() { return pointValue; }
	public int getWeight() { return weight; }
	public void activateItem() { activated = true; }
	public void deactivateItem() { activated = false; }
	public boolean isActivated() { return activated; }
	public void setLifespan(int n) { lifespan = n; }
	public void lifespanTick() { --lifespan; }
	public boolean isAlive() { return (lifespan > 0); }

	


}