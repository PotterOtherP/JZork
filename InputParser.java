public class InputParser {

	private GameState state;
	
	public InputParser (GameState st)
	{
		state = st;
	}

	public boolean parsePlayerInput(String input)
	{
		return true;
	}
	
	
	public boolean validateAction()
	{
		
		return true;
	}
	
	public String inputTestResults(String input)
	{
		String result = input;
		
		
		return result;
	}

	public boolean isGameWord(String str)
	{
		return (state.dictionary.contains(str));
	}
	
}
