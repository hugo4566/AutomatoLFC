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
			String dadoScan = scanner.next();
			String x = dadoScan.replaceAll("(.)->.*", "$1");
			String y = dadoScan.replaceAll(".->(.*)", "$1");
			Entrada entrada = new Entrada(dadoScan,x,y);
			Lista.add(entrada);
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
}
