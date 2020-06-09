import java.util.ArrayList;

// Method for an actor's "turn"
interface ActorMethod {

	public void actorUpdate();
}

public class Actor extends GameObject {

	public ActorMethod actorMethod;
	
	public boolean alive;


	public Actor(String name, Location loc)
	{
		super(name, loc);
		type = ObjectType.ACTOR;

		alive = true;

	}


	public void actorTurn()
	{
		actorMethod.actorUpdate();
	}


	public void setActorMethod(ActorMethod am)
	{
		actorMethod = am;
	}

	@Override
	public boolean isAlive() { return alive; }

	public String toString() { return name; }


}