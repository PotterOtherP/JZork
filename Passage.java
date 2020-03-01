class Passage {
	

	public final String name;
	public String lockFail;
	public String closedFail;
	public final Location locationA;
	public final Location locationB;

	public boolean open;
	public boolean locked;

	// Constructors
	public Passage()
	{
		this.name = "null";
		this.lockFail = GameStrings.CANT_GO;
		this.closedFail = GameStrings.CANT_GO;
		this.locationA = Location.NULL_LOCATION;
		this.locationB = Location.NULL_LOCATION;
		this.locked = true;
		this.open = false;
	}

	public Passage(Location locA, Location locB)
	{
		this.locationA = locA;
		this.locationB = locB;

		this.name = "";
		this.lockFail = "";
		this.closedFail = "";
		this.open = true;
		this.locked = false;

	}

	
	public void unlock() { locked = false; }
	public void lock() { locked = true; }

	public void open() { open = true; }
	public void close() { open = false; }

	public boolean isLocked() { return locked; }
	public boolean isOpen() { return open; }




}