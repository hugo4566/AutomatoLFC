package projeto;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import dominios.Entrada;

public class Principal {

	public static void main (String [] args) throws FileNotFoundException {
		
		ArrayList<Entrada> Lista = new ArrayList<Entrada>();
		
		// Parser
		Scanner scanner = new Scanner(new FileReader("arquivo.txt"));
		scanner.useDelimiter(",");
		while (scanner.hasNext()) {
			Lista.add(setEntrada(scanner.next()));
		}
		
		for(Entrada lista :Lista){
			System.out.println("Completo "+lista.getCompleto()+" ** Esq "+lista.getEsquerda()+" ** Dir "+lista.getDireita());
		}
		
		System.out.println(getVariavelInicial(Lista)); 
	}
	
	public static String getVariavelInicial(ArrayList<Entrada> lista){
		if(lista.size()>0){
			return lista.get(0).getEsquerda();
		}
		else
			return null;
	}
	
	public static Entrada setEntrada(String str){
		String x = str.replaceAll("(.)->.*", "$1");
		String y = str.replaceAll(".->(.*)", "$1");
		Entrada entrada = new Entrada(str,x,y);
		return entrada;
	}
}
