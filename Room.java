import java.util.HashMap;

class Room {
	
	public final String name;
	public final String description;
	public final Location roomID;

	public boolean firstVisit;


	public final Passage northExit;
	public final Passage southExit;
	public final Passage eastExit;
	public final Passage westExit;
	public final Passage northEastExit;
	public final Passage northWestExit;
	public final Passage southEastExit;
	public final Passage southWestExit;
	public final Passage upExit;
	public final Passage downExit;

	private HashMap exits;
	private HashMap failMessages;



	public Room()
	{
		this.name = "";
		this.description = "";
		this.roomID = Location.NULL_LOCATION;
		this.northExit = null;
		this.southExit = null;
		this.eastExit = null;
		this.westExit = null;
		this.northEastExit = null;
		this.northWestExit = null;
		this.southEastExit = null;
		this.southWestExit = null;
		this.upExit = null;
		this.downExit = null;

		this.firstVisit = true;
		Passage p = new Passage();


	}

	public Room(String name, String desc, Location loc)
	{
		this.name = name;
		this.description = desc;
		this.roomID = loc;
		this.exits = new HashMap<Action, Passage>();
		this.failMessages = new HashMap<Action, String>();
	}

	public Room(String name, String desc, Location loc, Passage north, Passage south, Passage east, Passage west,
				Passage nEast, Passage nWest, Passage sEast, Passage sWest, Passage up, Passage down)
	{
		this.name = name;
		this.description = desc;
		this.roomID = loc;
		this.northExit = north;
		this.southExit = south;
		this.eastExit = east;
		this.westExit = west;
		this.northEastExit = nEast;
		this.northWestExit = nWest;
		this.southEastExit = sEast;
		this.southWestExit = sWest;
		this.upExit = up;
		this.downExit = down;

		this.firstVisit = true;

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
		switch(act)
		{
			case EXIT_NORTH: { psg = northExit; } break;
			case EXIT_SOUTH: { psg = southExit; } break;
			case EXIT_EAST:  { psg = eastExit;  } break;
			case EXIT_WEST:  { psg = westExit;  } break;
			case EXIT_NORTHEAST:  { psg = northEastExit;  } break;
			case EXIT_NORTHWEST:  { psg = northWestExit;  } break;
			case EXIT_SOUTHEAST:  { psg = southEastExit;  } break;
			case EXIT_SOUTHWEST:  { psg = southWestExit;  } break;
			case EXIT_UP:  { psg = upExit;  } break;
			case EXIT_DOWN:  { psg = downExit;  } break;
			default: {} break;
		}


		// If there's no exit in that direction, print the room's particular message (for that direction.)
		if (psg.name.equals("null"))
		{
			result = false;
			Game.output(GameStrings.CANT_GO);
		}


		else
		{
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
			
		}

		return result;
	}

}