import java.util.HashMap;

class Room {
	
	public final String name;
	public final String description;
	public final Location roomID;

	public boolean firstVisit;


	private HashMap<Action, Passage> exits;
	private HashMap<Action, String> failMessages;



	public Room()
	{
		this.name = "";
		this.description = "";
		this.roomID = Location.NULL_LOCATION;		
		this.exits = new HashMap<Action, Passage>();
		this.failMessages = new HashMap<Action, String>();
		this.firstVisit = true;


	}

	public Room(String name, String desc, Location loc)
	{
		this.name = name;
		this.description = desc;
		this.roomID = loc;
		this.exits = new HashMap<Action, Passage>();
		this.failMessages = new HashMap<Action, String>();
	}

	public void addExit(Action act, Passage psg)
	{
		this.exits.put(act, psg);
	}



	public void lookAround(GameState state)
	{
		Game.output(description);
		for (Item it : state.itemList.values())
		{
			if (it.getLocation() == this.roomID)
			{
				String word = (it.vowelStart()? "an " : "a ");
				Game.output("There is " + word + it.name + " here.");
			}
				
		}

		for (Actor a : state.actorList.values())
		{
			if (a.getLocation() == this.roomID)
			{
				String word = (a.vowelStart()? "an " : "a ");
				Game.output("There is " + word + a.name + " here.");
			}
		}
	}


	public boolean exit(GameState state, Action act)
	{
		Passage psg = null;
		boolean result = false;
		Location dest = Location.NULL_LOCATION;


		// Identify which direction the player is trying to go.
		if (exits.containsKey(act))
		{
			psg = exits.get(act);
		}

		else
		{
			if (failMessages.containsKey(act))
				Game.output(failMessages.get(act));
			else
				Game.output(GameStrings.CANT_GO);

			return false;
		}



		// Figure out which side of the Passage the player is on.
		if (psg.locationA == this.roomID) { dest = psg.locationB; }
		else { dest = psg.locationA; }

		// If the Passage is open... success
		if (psg.isOpen())
		{
			state.setPreviousLocation(state.getPlayerLocation());
			state.setPlayerLocation(dest);
			result = true;
		}

		else
		{
			// If the Passage is locked, print the Passage's locked message.
			if (psg.isLocked())
			{
				Game.output(psg.lockFail);
			}

			// If the Passage is closed, but not locked.
			else
			{
				Game.output(psg.closedFail);
			}
		}
		

		return result;
	}

}