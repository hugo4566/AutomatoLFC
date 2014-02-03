package projeto;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Principal {

	public static void main (String [] args) throws FileNotFoundException {
		
		// Parser
		Scanner scanner = new Scanner(new FileReader("arquivo.txt"));
		scanner.useDelimiter(",");
		while (scanner.hasNext()) {
			String nome = scanner.next();
			System.out.println(nome);
		}
		
		
	}
}
