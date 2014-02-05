package projeto;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import dominios.Entrada;

public class Principal {

	public static void main (String [] args) throws FileNotFoundException {
		
		HashSet<String> conjuntoVariaveis = new HashSet<String>();
		HashSet<String> conjuntoTerminais = new HashSet<String>();
		
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

		imprimirTodasVariaveis(conjuntoVariaveis);
		imprimirTodosTerminais(conjuntoTerminais);
		
		
		//algum for{};		for cada não-terminal A do Primeiro(A) := {};
		//algum while{		while houver alterações em algum Primeiro(A) do {
		for(Entrada listaEntrada :Lista){
			int n = listaEntrada.getDireita().length();
			int k =1; boolean Continue = true;
			while(Continue && k<=n){
				//Escreva alguma besteira aki
//				acrescente Primeiro(X_k)-{epsilon} a Primeiro(A);
//				if epsilon não pertencer a Primeiro(X_k) then Continue := false;
				k = k+1;
				if(Continue == true){
					//Escreva alguma besteira aki
//					then acrescente epsilon a Primeiro(A);
				}
			}
		}
		//};
				
	}



	public static void imprimirTodasVariaveis(HashSet<String> conjuntoVariaveis) {
		System.out.println("-------Variaveis--------");
		
		for(String conjV :conjuntoVariaveis){
			System.out.println(conjV);
		}
	}
	
	public static void imprimirTodosTerminais(HashSet<String> conjuntoTerminais) {
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
				if(temp.add(lista.getDireita().charAt(x)+"") && !(lista.getDireita().charAt(x)+"").equals("E"))
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
