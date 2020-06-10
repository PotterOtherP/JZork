import java.util.ArrayList;
import java.util.Random;


public class Actor extends GameObject {
	
	public boolean alive;

	public static final int SONGBIRD_CHIRP_PERCENT = 15;


	public Actor(String name, Location loc)
	{
		super(name, loc);
		type = ObjectType.ACTOR;

		alive = true;

	}
    

	public void cyclopsTurn(GameState state)
	{
		if (state.playerLocation == Location.CELLAR && state.playerPreviousLocation == Location.LIVING_ROOM)
        {
            Room rm = state.worldMap.get(Location.CELLAR);
            Passage p = rm.exits.get(Action.UP);
            p.close();
        }
	}

	public void floodTurn(GameState state)
    {
    	
    }

    public void gustOfWindTurn(GameState state)
    {
    	
    }

    public void riverCurrentTurn(GameState state)
    {

    }

	public void songbirdTurn(GameState state)
	{
		if (altLocations.contains(state.playerLocation))
		{
			Random rand = new Random();
			if (rand.nextInt(100) < SONGBIRD_CHIRP_PERCENT)
				Game.output(GameStrings.SONGBIRD);
		}

		
	}

	public void spiritsTurn(GameState state)
    {

    }

    public void thiefTurn(GameState state)
    {

    }

    public void trollTurn(GameState state)
    {

    }

    public void vampireBatTurn(GameState state)
    {

    }

	@Override
	public boolean isAlive() { return alive; }

	public String toString() { return name; }


}