import java.util.ArrayList;

class Feature extends GameObject {


	public Feature(String name, Location loc)
	{
		super(name, loc);
		type = ObjectType.FEATURE;

		
	}

	@Override
	public void close(GameState state)
	{
		switch (name)
		{
			case "kitchen window":
			{
				Room r = state.worldMap.get(Location.BEHIND_HOUSE);
				Passage p = r.exits.get(Action.WEST);
				if (p.isOpen())
				{
					Game.output(GameStrings.WINDOW_CLOSES);
					examineString = ObjectStrings.WINDOW_EXAMINE_CLOSED;
					p.close();
				}
				else
					Game.output("The window is already closed.");
			} break;

			default:
			{
				Game.output(closeString);
			} break;
		}
	}

	@Override
	public void lookIn(GameState state)
	{
		switch (name)
		{
			case "kitchen window":
			{
				if (state.playerLocation == Location.BEHIND_HOUSE)
					Game.output(ObjectStrings.WINDOW_LOOK_IN);
				else
					Game.output("You are inside.");
			} break;

			default: { super.lookIn(state); } break;
		}
	}

	@Override
	public void lookOut(GameState state)
	{
		switch (name)
		{
			case "kitchen window":
			{
				if (state.playerLocation == Location.KITCHEN)
					Game.output(ObjectStrings.WINDOW_LOOK_OUT);
				else
					Game.output("You are outside.");
			} break;

			default: { super.lookOut(state); } break;
		}
	}

	

	@Override
	public void move(GameState state)
	{
		switch (name)
		{
			case "oriental rug":
			{
				if (!state.carpetMoved)
                {
                    state.carpetMoved = true;
                    boardString = ObjectStrings.CARPET_SIT_2;
                    lookUnderString = "There is nothing but dust there.";
                    GameObject trap = state.objectList.get("trap door");
                    trap.location = Location.LIVING_ROOM;
                    Game.output(GameStrings.MOVE_RUG);
                    Room rm = state.worldMap.get(Location.LIVING_ROOM);
                    rm.description = MapStrings.DESC_LIVING_ROOM_TRAPDOOR_CLOSED;
                    Passage p = rm.exits.get(Action.DOWN);
                    p.closedFail = "The trap door is closed.";
                }

                else
                {
                    Game.output(GameStrings.RUG_ALREADY_MOVED);
                }
			} break;

			default:
			{
				Game.output(moveString);
			} break;
		}
	}

	@Override
	public void open(GameState state)
	{
		switch (name)
		{
			case "kitchen window":
			{
				Room r = state.worldMap.get(Location.BEHIND_HOUSE);
				Passage p = r.exits.get(Action.WEST);
				if (!p.isOpen())
				{
					Game.output(GameStrings.WINDOW_OPENS);
					examineString = ObjectStrings.WINDOW_EXAMINE_OPEN;
					p.open();
				}
				else
					Game.output(GameStrings.getHardSarcasm());
			} break;

			case "trap door":
			{
				Room r = state.worldMap.get(Location.LIVING_ROOM);
				Passage p = r.exits.get(Action.DOWN);
				if (!p.isOpen())
				{
					Game.output(GameStrings.TRAP_DOOR_OPENS);
					r.description = MapStrings.DESC_LIVING_ROOM_TRAPDOOR_OPEN;
					p.open();
				}
				else
				{
					Game.output(GameStrings.getHardSarcasm());
				}
			} break;


			default:
			{
				Game.output(openString);
			} break;
		}
	}

	public String toString() { return name; }

}

