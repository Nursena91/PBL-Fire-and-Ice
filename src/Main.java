import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import enigma.console.TextAttributes;
import enigma.core.Enigma;
import java.util.Random;

public class Main {

	public static void main(String[] args) throws Exception {
		enigma.console.Console cn = Enigma.getConsole("FrostBurn", 150, 40, 13);
		Game myGame = new Game(cn);
	}

}
