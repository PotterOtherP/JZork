public class InputParseTest {
	
	public static void main(String[] args)
	{
		String[] testPhrases = {

			
			"attack house sword"

		};

		GameState gs = new GameState();

		new GameSetup(gs, true, true);

		InputParser parse = null;

		for (int i = 0; i < testPhrases.length; ++i)
		{
			gs.resetInput();
			gs.completePlayerInput = testPhrases[i];
			parse = new InputParser(gs);
			boolean success = parse.parsePlayerInput();
			Game.output(success ? "Action succeeded" : "Action failed");
			parse.inputTest();
			Game.outputLine();
		}


	}
}