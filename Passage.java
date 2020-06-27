class Passage {
    

    public String closedFail;
    public String weightFail;
    public String message;
    public final Location locationA;
    public final Location locationB;

    public boolean open;

    public int weightLimit;

    public Passage(Location locA, Location locB)
    {
        locationA = locA;
        locationB = locB;

        closedFail = GameStrings.CANT_GO;
        weightFail = GameStrings.PASSAGE_OVERBURDENED;
        message = "";
        open = true;

    }

    

    public void open() { open = true; }
    public void close() { open = false; }

    public boolean isOpen() { return open; }




}
