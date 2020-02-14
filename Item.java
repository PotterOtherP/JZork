class Item extends GameObject{

	// Items can be picked up and moved to other locations, including the player's inventory.

	private final Location startLocation;
	private Location location;


	public Item()
	{
		super();
		this.startLocation = super.location;
		this.location = this.startLocation;
	}

	public Item(String name, Location loc)
	{
		super(name, loc);
		this.startLocation = super.location;
		this.location = this.startLocation;
	}


	public void setLocation(Location loc) { this.location = loc; }
	public Location getLocation() { return this.location; }

	


}