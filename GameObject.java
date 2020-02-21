import java.util.ArrayList;

interface ActivateMethod {

    public void run(GameState state, Action act);
}

abstract class GameObject {
    
    public final String name;
    public final Location location;
    public String takeFail;
    private ActivateMethod method;
    public ObjectType type;
    public ArrayList<Item> inventory;


    // Constructors
    public GameObject(String name, Location loc)
    {
        this.name = name;
        this.location = loc;
        this.takeFail = ("That's not something you can take, really.");
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

    // Common methods to all objects, which will be overridden in the child classes.

    public void open(GameState state) {}
    public void close(GameState state) {}
    public void unlock(GameState state) {}
    public void lock(GameState state) {}

    public boolean isOpen() { return false; }
    public boolean isAlive() { return false; }

    public void take(GameState state)
    {
        Game.output(takeFail);
    }

    public void drop(GameState state) {}

    

}