import java.util.HashMap;

class GameState {

	// gameplay information
	public int turns;
    public int darknessTurns;
    public boolean lightActivated;

	// game events


	// player attributes
	public Location playerLocation;
	public Location playerPreviousLocation;
	public int playerHitPoints;
	public int playerCarryWeight;
	public int playerMaxCarryWeight;


	// player action
	public String phrase;
	public String first;
	public String second;
	public String third;
	public ActionType actionType;


	public Action playerAction;
	public GameObject directObject;
	public GameObject indirectObject;
	public Feature dummyObject;

	// lists of game objects
	public HashMap<Location, Room> worldMap;
	public HashMap<String, GameObject> objectList;




	// constructor
	public GameState()
	{
		this.dummyObject = new Feature("dummy_feature", Location.NULL_LOCATION);
		this.resetInput();

		this.turns = 0;
		this.playerLocation = Location.NULL_LOCATION;
		this.playerPreviousLocation = Location.NULL_LOCATION;
		playerCarryWeight = 0;
		playerMaxCarryWeight = 100;

		

		worldMap = new HashMap<Location, Room>();
		objectList = new HashMap<String, GameObject>();
	}



	public void setPlayerLocation(Location loc) { playerLocation = loc; }
	public Location getPlayerLocation() { return playerLocation; }

	public void setPreviousLocation(Location loc) { playerPreviousLocation = loc; }
	public Location getPreviousLocation() { return playerPreviousLocation; }

	public void setPlayerAction(Action act) { playerAction = act; }
	public Action getPlayerAction() { return playerAction; }



	public void resetInput()
	{
		this.first = "";
		this.second = "";
		this.third = "";
		this.phrase = "";

		this.playerAction = Action.NULL_ACTION;
		this.actionType = ActionType.NULL_TYPE;
		this.directObject = dummyObject;
		this.indirectObject = dummyObject;
	}




	// It should not be possible to alter the number of turns except by adding 1.
	public void addTurn() { ++turns; }
	public int getTurns() { return turns; }



}