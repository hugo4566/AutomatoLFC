package projeto;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Principal {

	public static void main (String [] args) throws FileNotFoundException {
		
		ArrayList<String> Lista = new ArrayList<String>();
		
		// Parser
		Scanner scanner = new Scanner(new FileReader("arquivo.txt"));
		scanner.useDelimiter(",");
		while (scanner.hasNext()) {
			Lista.add(scanner.next());
		}
		
		for(String lista :Lista){
			System.out.println(lista);
		}
		
		
	}
}
