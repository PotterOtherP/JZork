public class InputParseTest {
	
	public static void main(String[] args)
	{
		String[] testPhrases = {

			
			"attack house sword"

		};

		GameState gs = new GameState();

		new GameSetup(gs, true, true);

		String alpha9 = "123456789";
		String alpha10 = "abcdefghij";
		String alpha20 = "abcdefghijklmnopqrst";
		String alpha26 = "abcdefghijklmnopqrstuvwxyz";
		String alpha50 = alpha9 + " " + alpha9 + " " + alpha9 + " " + alpha9 + " " + alpha9;
		String alpha100 = alpha50 + " " + alpha50;


		System.out.println("Substring(0, 10) of alphabet is " + alpha26.substring(0, 10));
		System.out.println("Substring(10) of alphabet is " + alpha26.substring(10));

		Game.output(alpha50);
		Game.output(alpha100);




	}
}