import java.util.HashMap;

class Room {
	
	public final String name;
	public String description;
	public final Location roomID;

	public boolean firstVisit;

	public boolean darkness;

	public HashMap<Action, Passage> exits;
	public HashMap<Action, String> failMessages;



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
		this.firstVisit = true;
		this.exits = new HashMap<Action, Passage>();
		this.failMessages = new HashMap<Action, String>();
	}

	public void addExit(Action act, Passage psg)
	{
		this.exits.put(act, psg);
	}

	public void addFailMessage(Action act, String msg)
	{
		this.failMessages.put(act, msg);
	}

	public void setDark() { darkness = true; }
	public boolean isDark() { return darkness; }

	public void setDescription(String s)
	{
		this.description = s;
	}




	public void lookAround(GameState state)
	{
		Game.outputLine();

        if (darkness)
        {
            Game.output(GameStrings.DARKNESS);
            return;
        }

		Game.output(getDescription(state));

		for (GameObject g : state.objectList.values())
		{
			if (g.location == roomID)
			{
				if ( (g.isItem() || g.isActor()) && !g.presence.isEmpty() )
				{
					Game.output(g.presence);		
				}

				if (g.isContainer() && g.isOpen() && !g.inventory.isEmpty())
				{
					Game.output("The " + g.name + " contains: ");
					for (GameObject it : g.inventory)
						Game.output(it.name);
				}
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
			// Darkness check
			if (isDark() && !state.lightActivated)
			{
				Game.output(GameStrings.GRUE_DEATH_1);
				state.playerAlive = false;
				return false;
			}

			if (failMessages.containsKey(act))
				Game.output(failMessages.get(act));
			else
				Game.output(GameStrings.CANT_GO);

			return false;
		}



		// Figure out which side of the Passage the player is on.
		if (psg.locationA == this.roomID) { dest = psg.locationB; }
		else { dest = psg.locationA; }

		// Darkness check. If the room is in darkness, and the destination
		// is not the previous room, player dies by grue.
		if (isDark() && !state.lightActivated && (dest != state.playerPreviousLocation))
		{
			Game.output(GameStrings.GRUE_DEATH_1);
			state.playerAlive = false;
			return false;

		}

		// If the Passage is open... success
		if (psg.isOpen())
		{
			state.playerPreviousLocation = state.playerLocation;
			state.playerLocation = dest;
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


	public String getDescription(GameState state)
	{
		String result = this.description;



		switch (roomID)
		{
			case BEHIND_HOUSE:
            {
                if (exits.get(Action.WEST).isOpen())
                    result = GameStrings.DESC_BEHIND_HOUSE_WINDOW_OPEN;
            } break;

            case KITCHEN:
            {
                if (exits.get(Action.EAST).isOpen())
                    result = GameStrings.DESC_KITCHEN_WINDOW_OPEN;
            } break;

            case LIVING_ROOM:
            {

            } break;


			default:
			{
				
			} break;
		}

		return result;
	}

}