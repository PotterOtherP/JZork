import java.util.ArrayList;

class Feature extends GameObject {

	/* A feature is an object that exists in one or more locations, but doesn't move.


	*/

	

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
					Game.output("The window is already open.");
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

