package projeto;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import dominios.Entrada;

public class Principal {

	static HashSet<String> conjuntoVariaveis;
	static HashSet<String> conjuntoTerminais;
	
	public static void main (String [] args) throws FileNotFoundException {
		
		conjuntoVariaveis = new HashSet<String>();
		conjuntoTerminais = new HashSet<String>();
		
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
		
		System.out.println("Variavel Inicial: "+getVariavelInicial(Lista)+"\n");
		
		conjuntoVariaveis = todasVariaveis(conjuntoVariaveis, Lista);
		conjuntoTerminais = todosTerminais(conjuntoTerminais,conjuntoVariaveis, Lista);

		imprimirTodasVariaveis();
		imprimirTodosTerminais();
	}
	
	public void Primeiro(){
		
//		switch
	}

	public static boolean isTerminal(String simbolo){
		HashSet<String> temp = new HashSet<String>(); 
		temp.addAll(conjuntoVariaveis);
		if(temp.add(simbolo) && !simbolo.equals("E"))
			return true;
		else
			return false;
	}
	
	public static boolean isVariavel(String simbolo){
		HashSet<String> temp = new HashSet<String>(); 
		temp.addAll(conjuntoVariaveis);
		if(!temp.add(simbolo))
			return true;
		else
			return false;
	}

	public static void imprimirTodasVariaveis() {
		System.out.println("-------Variaveis--------");
		
		for(String conjV :conjuntoVariaveis){
			System.out.println(conjV);
		}
	}
	
	public static void imprimirTodosTerminais() {
		System.out.println("-------Terminais--------");
		
		for(String conjT :conjuntoTerminais){
			System.out.println(conjT);
		}
	}
	
	
	
	private static HashSet<String> todosTerminais(HashSet<String> conjT,HashSet<String> conjV, ArrayList<Entrada> Lista) {
		HashSet<String> temp = new HashSet<String>(); 
		temp.addAll(conjV);
		for(Entrada lista :Lista){
			for(int x=0; x<lista.getDireita().length();x++){
				if(isTerminal(lista.getDireita().charAt(x)+""))
					conjT.add(lista.getDireita().charAt(x)+"");
			}
		}
		return conjT;
	}

	public static HashSet<String> todasVariaveis (HashSet<String> conjV,ArrayList<Entrada> Lista){
		for(Entrada lista :Lista){
			conjV.add(lista.getEsquerda());
		}
		return conjV;
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
