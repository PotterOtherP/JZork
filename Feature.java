import java.util.ArrayList;

class Feature extends GameObject {


	public Feature(String name, Location loc)
	{
		super(name, loc);
		type = ObjectType.FEATURE;

		
	}

	@Override
	public void open(GameState state)
	{
		switch (name)
		{
			case "window":
			case "house window":
			case "kitchen window":
			{
				Room r = state.worldMap.get(Location.BEHIND_HOUSE);
				Passage p = r.exits.get(Action.WEST);
				if (!p.isOpen())
				{
					Game.output(GameStrings.WINDOW_OPENS);
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
				Game.output("That's not something you can open.");
			} break;
		}
	}

	@Override
	public void close(GameState state)
	{
		switch (name)
		{
			case "window":
			case "house window":
			case "kitchen window":
			{
				Room r = state.worldMap.get(Location.BEHIND_HOUSE);
				Passage p = r.exits.get(Action.WEST);
				if (p.isOpen())
				{
					Game.output(GameStrings.WINDOW_CLOSES);
					p.close();
				}
				else
					Game.output("The window is already closed.");
			} break;

			default:
			{
				Game.output("That's not something you can close.");
			} break;
		}
	}

	@Override
	public void move(GameState state)
	{
		switch (name)
		{
			case "carpet":
			{
				if (!state.carpetMoved)
                {
                    state.carpetMoved = true;
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

			case "pile":
			{

			} break;

			default:
			{
				Game.output(moveString);
			} break;
		}
	}

	public String toString() { return name; }

}

