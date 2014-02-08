package projeto;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import dominios.Entrada;

public class Principal {

	static HashSet<String> conjuntoVariaveis;
	static HashSet<String> conjuntoTerminais;
	static ArrayList<Entrada> Lista;
	
	public static void main (String [] args) throws FileNotFoundException {
		
		conjuntoVariaveis = new HashSet<String>();
		conjuntoTerminais = new HashSet<String>();
		
		Lista = new ArrayList<Entrada>();
		
		// Parser
		Scanner scanner = new Scanner(new FileReader("arquivo.txt")).useDelimiter(",");
		while (scanner.hasNext()) {
			Lista.add(setEntrada(scanner.next()));
		}
		
		for(Entrada lista :Lista){
//			System.out.println("Completo "+lista.getCompleto()+" ** Esq "+lista.getEsquerda()+" ** Dir "+lista.getDireita());
		}
		
		conjuntoVariaveis = todasVariaveis(conjuntoVariaveis, Lista);
		conjuntoTerminais = todosTerminais(conjuntoTerminais,conjuntoVariaveis, Lista);

		imprimirTodasVariaveis();
		imprimirTodosTerminais();
		
		HashMap<String, HashSet<String>> Primeiro = new HashMap<String, HashSet<String>>();
		
		// Primeiro Epsilon
		HashSet<String> cj = new HashSet<String>();
		cj.add("E");
		Primeiro.put("E", cj);	
		
		
		// Caso 1 -- faço Primeiro(a) = {a}
		doCaso1(Primeiro);
		
		// Caso 2 -- faco Primeiro(A):
		doCaso2(Primeiro);
	}

	private static void doCaso1(HashMap<String, HashSet<String>> Primeiro) {
		for(String variavel:conjuntoTerminais){
			HashSet<String> cjT = new HashSet<String>();
			cjT.add(variavel);
			Primeiro.put(variavel, cjT);
		}
	}

	private static void doCaso2(HashMap<String, HashSet<String>> Primeiro) {
		
		HashMap<String, HashSet<String>> PrimeiroAntigo = new HashMap<String, HashSet<String>>();
		
		// Iniciei Primeiro(variavel) = {}
		for(String variavel:conjuntoVariaveis){
			HashSet<String> cjV = new HashSet<String>();
			Primeiro.put(variavel, cjV);
		}
		
		// Cria copia do antigo
		PrimeiroAntigo.putAll(Primeiro);
		
		boolean alteracao = true;
		while(alteracao){
			String simbolo = "e";
			ArrayList<Entrada> listaProducoesSimbolo = new ArrayList<Entrada>();
			listaProducoesSimbolo.addAll(producoesDoSimbolo(simbolo));
			
			int k =1; boolean Continue = true;
			for(Entrada producao : listaProducoesSimbolo){
				int n = producao.getDireita().length();
				
				while(Continue && k<=n){
					String simboloK = producao.getDireita().charAt(k-1)+"";
					Primeiro.get(simbolo).addAll(Primeiro.get(simboloK)); Primeiro.get(simbolo).remove("E");
					
					// Testa se contem ou nao Epsilon
					HashSet<String> conjuntoTeste = new HashSet<String>();
					conjuntoTeste.addAll(Primeiro.get(simbolo));
					if(conjuntoTeste.add("E")){		// nao tem Epsilon
						Continue = false;
					}
					k++;
				}
				
				if(Continue){
					Primeiro.get(simbolo).add("E");
				}
				
				
				if(PrimeiroAntigo.equals(Primeiro)){
					alteracao = false;
					PrimeiroAntigo.putAll(Primeiro);
					break;
				}
				
				PrimeiroAntigo.putAll(Primeiro);
			}	
		}
	}
	
	public static ArrayList<Entrada> producoesDoSimbolo(String simbolo){
		ArrayList<Entrada> listaProducoes = new ArrayList<Entrada>();
		
		for(Entrada lista : Lista){
			if(lista.getEsquerda().equals(simbolo))
				listaProducoes.add(lista);
		}
		
		if(listaProducoes.size()>0)
			return listaProducoes;
		else
			return null;	
	}
	
	public static void imprimaConjunto(HashSet<String> conjunto){
		try {
			System.out.println("-------Conjunto-------");
			if(conjunto.size() == 0){
				System.out.println("Conjunto vazio");
			}
			else{
				for(String conj : conjunto){
					System.out.println(conj);
				}
			}
		} catch (Exception e) {
			System.out.println("Não tem conjunto! Não eh que esteja vazia, só não existe!");
		}
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
	
	public static Entrada setEntrada(String str){
		String x = str.replaceAll("(.)->.*", "$1");
		String y = str.replaceAll(".->(.*)", "$1");
		Entrada entrada = new Entrada(str,x,y);
		return entrada;
	}
}
