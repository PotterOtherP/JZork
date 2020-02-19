class Feature extends GameObject {

	/* A feature is an object that exists in a room. 


	*/

	public final ObjectType type = ObjectType.FEATURE;
	
	

	public Feature(String name, Location loc)
	{
		super(name, loc);
	}

	

}

