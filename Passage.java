class Passage {
	

	public final String name;
	public final String lockFail;
	public final String closedFail;
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

	public Passage(String name, String exit, String closed, Location locA, Location locB)
	{
		this.name = name;
		this.lockFail = exit;
		this.closedFail = closed;
		this.locationA = locA;
		this.locationB = locB;
		this.locked = false;
		this.open = true;
	}

	
	public void unlock() { locked = false; }
	public void lock() { locked = true; }

	public void open() { open = true; }
	public void close() { open = false; }

	public boolean isLocked() { return locked; }
	public boolean isOpen() { return open; }




}