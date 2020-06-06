import java.util.HashMap;

class Room {
	
	public final String name;
	public String description;
	public final Location roomID;

	public boolean firstVisit;

	public boolean darkness;
	public boolean gas;
	public boolean height;
    public int danger;
    public int loudness;


	public HashMap<Action, Passage> exits;
	public HashMap<Action, String> failMessages;



	public Room()
	{
		name = "";
		description = "";
		roomID = Location.NULL_LOCATION;		
		exits = new HashMap<Action, Passage>();
		failMessages = new HashMap<Action, String>();
		firstVisit = true;


	}

	public Room(String nm, String desc, Location loc)
	{
		name = nm;
		description = desc;
		roomID = loc;
		firstVisit = true;
		exits = new HashMap<Action, Passage>();
		failMessages = new HashMap<Action, String>();
	}

	public void addExit(Action act, Passage psg)
	{
		exits.put(act, psg);
	}

	public void addFailMessage(Action act, String msg)
	{
		failMessages.put(act, msg);
	}

	public void removeFailMessage(Action act)
	{
		failMessages.remove(act);
	}

	public void setDark() { darkness = true; }
	public void setLight() { darkness = false; }
	public boolean isDark() { return darkness; }
	public void setGas() { gas = true; }
	public boolean hasGas() { return gas; }

	public void setDescription(String s)
	{
		description = s;
	}




	public void lookAround(GameState state)
	{
		Game.outputLine();
		state.refreshInventories();

        if (darkness && !state.lightActivated)
        {
            Game.output(GameStrings.DARKNESS);
            return;
        }

		Game.output(getDescription(state));


		for (GameObject g : state.objectList.values())
		{
			if (g.location != roomID) continue;
			
			if (g.isActor())
			{
				Game.output(g.presenceString);
			}

			if (g.isItem())
			{
				Item it = (Item)(g);
				Game.output(it.getItemDescription());
			}

			if (g.isSurface())
			{
				if (!g.inventory.isEmpty())
				{
					boolean check = false;

					for (Item it : g.inventory)
					{
						if (!it.initialPresenceString.isEmpty() && !it.movedFromStart)
						{
							Game.output(it.initialPresenceString);
							check = true;
						}
					}

					if (!check)
					{
						Game.output("Sitting on the " + g.name + " is:");
						for (Item it : g.inventory)
							Game.output(it.capArticleName);
					}

				}
			}

			if (g.isContainer() && g.isOpen())
			{
				if (!g.inventory.isEmpty())
				{
					boolean check = false;

					for (Item it : g.inventory)
					{
						if (!it.initialPresenceString.isEmpty() && !it.movedFromStart)
						{
							Game.output(it.initialPresenceString);
							check = true;
						}
					}

					if (!check)
					{
						Game.output("The " + g.name + " contains:");
						for (Item it : g.inventory)
							Game.output(it.capArticleName);
					}

				}
			}

			// An annoying exception: the glass bottle, in which the water can be seen
			// even though it's closed.
				
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

		// Check here for baggage limit passages.
		if (state.playerCarryWeight > psg.weightLimit)
		{
			Game.output(psg.weightFail);
			return false;
		}


		// Figure out which side of the Passage the player is on.
		if (psg.locationA == roomID) { dest = psg.locationB; }
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
			if (!psg.message.isEmpty()) Game.output(psg.message);

			state.playerPreviousLocation = state.playerLocation;
			state.playerLocation = dest;
			result = true;
		}

		else
		{	
			Game.output(psg.closedFail);
		}
		

		return result;
	}


	public String getDescription(GameState state)
	{
		String result = description;



		switch (roomID)
		{
			case BEHIND_HOUSE:
            {
                if (exits.get(Action.WEST).isOpen())
                    result = MapStrings.DESC_BEHIND_HOUSE_WINDOW_OPEN;
            } break;

            case KITCHEN:
            {
                if (exits.get(Action.EAST).isOpen())
                    result = MapStrings.DESC_KITCHEN_WINDOW_OPEN;
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