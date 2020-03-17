import java.util.ArrayList;

interface ActivateMethod {

    public void run(GameState state, Action act);
}

abstract class GameObject {
    
    public final String name;
    public Location location;

    public ObjectType type;
    public String articleName;
    public String capArticleName;
    public boolean visible;
    public boolean movedFromStart;
    public String initialPresenceString;
    public String presenceString;

    // responses to player actions, mostly taking the place of the lambda methods.
    public String takeString;
    public String examineString;
    public String readString;
    public String boardString;
    
    public ActivateMethod method;
    public ArrayList<Item> inventory;
    public ArrayList<Location> altLocations;
    public ArrayList<String> altNames;


    // Constructors
    public GameObject(String nm, Location loc)
    {
        name = nm;
        location = loc;

        articleName = (vowelStart() ? "an " : "a ") + name;
        capArticleName = (vowelStart() ? "An " : "A ") + name;
        visible = true;
        initialPresenceString = "";
        takeString = "That's not something you can take, really.";
        presenceString = "There is " + articleName + " here.";
        examineString = "There's nothing special about the " + name + ".";
        readString = "You can't read that!";
        boardString = "You have a theory on how to board " + articleName + ", perhaps?";

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

    public void take(GameState state) { Game.output(takeString); }
    public void drop(GameState state) { Game.output("You can't drop that."); }
    public void place(GameState state, Item it) { Game.output("You can't place that."); }
    public void remove(GameState state, Item it) { Game.output("You can't do that."); }
    public void read(GameState state) { Game.output(readString); }
    public void board(GameState state) { Game.output(boardString); }
    public void tick() {}

    public void examine(GameState state) { Game.output(examineString); }

    public boolean isOpen() { return false; }
    public boolean isAlive() { return false; }
    public boolean isVisible() { return visible; }


    

}