import java.util.HashMap;

class GameState {

	// gameplay information
	public int turns;
    public int darknessTurns;
    public boolean lightActivated;

	// game events
	public boolean leafPileMoved;


	// player attributes
	public Location playerLocation;
	public Location playerPreviousLocation;
	public int playerHitPoints;
	public int playerScore;
	public int playerCarryWeight;
	public int playerMaxCarryWeight;
	public boolean playerAlive;


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
		dummyObject = new Feature("dummy_feature", Location.NULL_LOCATION);
		resetInput();

		turns = 0;
		playerLocation = Location.NULL_LOCATION;
		playerPreviousLocation = Location.NULL_LOCATION;
		lightActivated = false;
		playerAlive = true;
		playerCarryWeight = 0;
		playerScore = 0;
		playerMaxCarryWeight = Game.CARRY_WEIGHT_LIMIT;

		

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
		first = "";
		second = "";
		third = "";
		phrase = "";

		playerAction = Action.NULL_ACTION;
		actionType = ActionType.NULL_TYPE;
		directObject = dummyObject;
		indirectObject = dummyObject;
	}




	// It should not be possible to alter the number of turns except by adding 1.
	public void addTurn() { ++turns; }
	public int getTurns() { return turns; }



}