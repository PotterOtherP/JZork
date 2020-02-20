interface ActivateMethod {

    public void run(GameState state, Action act);
}

abstract class GameObject {
    
    public final String name;
    public final Location location;
    public String takeFail;
    private ActivateMethod method;


    // Constructors
    public GameObject(String name, Location loc)
    {
        this.name = name;
        this.location = loc;
        this.takeFail = ("You can't take that.");
        this.method = (GameState state, Action act) -> {};
    }

    public void setMethod(ActivateMethod am)
    {
        this.method = am;
    }


    public void activate(GameState state, Action act)
    {
        method.run(state, act);
    }

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



    

}