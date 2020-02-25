import java.util.ArrayList;

class Feature extends GameObject {

	/* A feature is an object that exists in one or more locations, but doesn't move.


	*/

	

	public Feature(String name, Location loc)
	{
		super(name, loc);
		this.type = ObjectType.FEATURE;
		altLocations = new ArrayList<Location>();
		altLocations.add(loc);
	}

	

}

