class Passage {
	

	public String closedFail;
	public String weightFail;
	public final Location locationA;
	public final Location locationB;

	public boolean open;

	public int weightLimit;

	// Constructors
	public Passage()
	{
		closedFail = GameStrings.CANT_GO;
		weightFail = GameStrings.PASSAGE_OVERBURDENED;
		locationA = Location.NULL_LOCATION;
		locationB = Location.NULL_LOCATION;
		open = false;
		weightLimit = Game.CARRY_WEIGHT_LIMIT;
	}

	public Passage(Location locA, Location locB)
	{
		locationA = locA;
		locationB = locB;

		closedFail = GameStrings.CANT_GO;
		weightFail = GameStrings.PASSAGE_OVERBURDENED;
		open = true;

	}

	

	public void open() { open = true; }
	public void close() { open = false; }

	public boolean isOpen() { return open; }




}