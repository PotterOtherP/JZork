// Method for an actor's "turn"
interface ActorMethod {

	public void actorUpdate();
}

class Actor extends GameObject {
	
	public final Location startLocation;
	public Location currentLocation;
	public Location previousLocation;


	public ActorMethod actorMethod;
	
	public boolean alive;
	public boolean encountered;




	public Actor(String name, Location loc)
	{
		super(name, loc);
		setVariables();
		startLocation = super.location;
		currentLocation = super.location;
		previousLocation = super.location;
		type = ObjectType.ACTOR;
	}


	public void actorTurn()
	{
		actorMethod.actorUpdate();
	}

	public void setVariables()
	{
		alive = true;
		encountered = false;
	}

	public void setActorMethod(ActorMethod am)
	{
		actorMethod = am;
	}



	public void setAlive(boolean b) { alive = b; }
	public boolean isAlive() { return alive; }
	public void setEncountered(boolean b ) { encountered = b; }
	public boolean playerHasEncountered() { return encountered; }

	public void setLocation(Location loc) { currentLocation = loc; }
	public Location getLocation() { return currentLocation; }

	public void setPreviousLocation(Location loc) { previousLocation = loc; }
	public Location getPreviousLocation() { return previousLocation; }


}