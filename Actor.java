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
		this.startLocation = super.location;
		this.currentLocation = super.location;
		this.previousLocation = super.location;
		this.type = ObjectType.ACTOR;
	}


	public void actorTurn()
	{
		actorMethod.actorUpdate();
	}

	private void setVariables()
	{
		this.alive = true;
		this.encountered = false;
	}

	public void setActorMethod(ActorMethod am)
	{
		this.actorMethod = am;
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