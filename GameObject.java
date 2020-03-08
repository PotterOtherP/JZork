import java.util.ArrayList;

interface ActivateMethod {

    public void run(GameState state, Action act);
}

abstract class GameObject {
    
    public final String name;
    public ObjectType type;
    public String article;  // "a" or "an"
    public Location location;
    public String takeFail;
    public String presence;
    public String examineString;
    
    public ActivateMethod method;
    public ArrayList<Item> inventory;
    public ArrayList<Location> altLocations;
    public ArrayList<String> altNames;


    // Constructors
    public GameObject(String nm, Location loc)
    {
        name = nm;
        article = vowelStart() ? "an" : "a";
        location = loc;
        takeFail = "That's not something you can take, really.";
        presence = "There is " + article + " " + name + " here.";
        examineString = "There's nothing special about the " + name + ".";
        method = (GameState state, Action act) -> {};
    }

    public void setMethod(ActivateMethod am)
    {
        method = am;
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

        String str = name.toLowerCase();
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

    public boolean isActor() { return type == ObjectType.ACTOR; }
    public boolean isContainer() { return type == ObjectType.CONTAINER; }
    public boolean isItem() { return type == ObjectType.ITEM; }
    public boolean isFeature() { return type == ObjectType.FEATURE; }
    public boolean isSurface() { return type == ObjectType.SURFACE; }
    public boolean playerHasObject() { return location == Location.PLAYER_INVENTORY; }

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