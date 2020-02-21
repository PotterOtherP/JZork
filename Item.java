class Item extends GameObject{

	// Items can be picked up and moved to other locations, including the player's inventory.

	private final Location startLocation;
	public Location location;
	private final int pointValue;
	private final int weight;

	// For items that can be turned on and expire, like the lamp
	private boolean activated;
	private int lifespan;


	public Item(String name, Location loc, int value, int weight)
	{
		super(name, loc);
		this.startLocation = super.location;
		this.location = this.startLocation;
		this.pointValue = value;
		this.weight = weight;
		this.activated = false;
		this.lifespan = 0;
		this.type = ObjectType.ITEM;
	}


	public void take(GameState state)
	{

		if ((state.playerCarryWeight + weight) >= state.playerMaxCarryWeight)
        {
            Game.output(GameStrings.OVERBURDENED);
            return;
        }

		state.playerCarryWeight += weight;
		setLocation(Location.PLAYER_INVENTORY);
		Game.output("Taken.");

	}

	public void drop(GameState state)
	{
		state.playerCarryWeight -= weight;
		setLocation(state.playerLocation);
		Game.output("Dropped.");
	}


	public void setLocation(Location loc) { this.location = loc; }
	public Location getLocation() { return this.location; }
	public int getValue() { return pointValue; }
	public int getWeight() { return weight; }
	public void activateItem() { activated = true; }
	public void deactivateItem() { activated = false; }
	public boolean isActivated() { return activated; }
	public void setLifespan(int n) { lifespan = n; }
	public void lifespanTick() { --lifespan; }
	public boolean isAlive() { return (lifespan > 0); }

	


}