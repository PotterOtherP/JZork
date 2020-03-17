import java.util.ArrayList;

class Feature extends GameObject {


	public Feature(String name, Location loc)
	{
		super(name, loc);
		type = ObjectType.FEATURE;

		altLocations = new ArrayList<Location>();
		altLocations.add(loc);
	}

	@Override
	public void open(GameState state)
	{
		switch(name)
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
		switch(name)
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

}

