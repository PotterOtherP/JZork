import java.util.ArrayList;

interface ActivateMethod {

    public void run(GameState state, Action act);
}

abstract class GameObject {
    
    public final String name;
    public String article;
    public Location location;
    public String takeFail;
    public String presence;
    public String examineString;
    public boolean movedFromStart;
    public ActivateMethod method;
    public ObjectType type;
    public ArrayList<Item> inventory;
    public ArrayList<Location> altLocations;


    // Constructors
    public GameObject(String name, Location loc)
    {
        this.name = name;
        this.article = vowelStart() ? "an" : "a";
        this.location = loc;
        this.movedFromStart = false;
        this.takeFail = "That's not something you can take, really.";
        this.presence = "There is " + article + " " + name + " here.";
        this.examineString = "There's nothing special about the " + name + ".";
        this.method = (GameState state, Action act) -> {};
    }

    public void setMethod(ActivateMethod am)
    {
        this.method = am;
    }

    public Location getLocation() { return location; }

    public void activate(GameState state, Action act)
    {
        method.run(state, act);
    }

    public void actorTurn() {}

    public boolean vowelStart()
    {
        // Exceptions can go here
        if (false)
        {
            return true;
        }

        boolean result = false;

        String str = this.name.toLowerCase();
        char c  = str.charAt(0);

        switch(c)
        {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
            {
                result = true;
            } break;

            default:
            {
                result = false;
            } break;
        }

        return result;
    }

    public boolean isActor() { return this.type == ObjectType.ACTOR; }
    public boolean isContainer() { return this.type == ObjectType.CONTAINER; }
    public boolean isItem() { return this.type == ObjectType.ITEM; }
    public boolean isFeature() { return this.type == ObjectType.FEATURE; }
    public boolean playerHasObject() { return this.location == Location.PLAYER_INVENTORY; }

    // Common methods to all objects, which will be overridden in the child classes.

    public void open(GameState state) { Game.output("You can't open that."); }
    public void close(GameState state) { Game.output("You can't close that."); }
    public void unlock(GameState state) { Game.output("You can't unlock that."); }
    public void lock(GameState state) { Game.output("You can't lock that."); }

    public boolean isOpen() { return false; }
    public boolean isAlive() { return false; }

    public void take(GameState state)
    {
        Game.output(takeFail);
    }

    public void drop(GameState state) { Game.output("You can't drop that."); }

    public void place(GameState state, Item it) { Game.output("You can't place that."); }
    public void remove(GameState state, Item it) { Game.output("You can't do that."); }

    public void examine(GameState state) { Game.output(examineString); }

    

}