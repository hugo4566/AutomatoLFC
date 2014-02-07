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
		
		HashMap<String, HashSet<String>> Primeiro = new HashMap<String, HashSet<String>>();
		
		HashSet<String> vazio = new HashSet<String>();		// conjunto vazio
		for(String variavel:conjuntoVariaveis){
			Primeiro.put(variavel, vazio);					// Primeiro(variavel,{})
		}
		
		for(String variavel:conjuntoTerminais){
			Primeiro.put(variavel, vazio);					// Primeiro(variavel,{})
		}
		
		String simbolo = "S";
		imprimaConjunto(Primeiro.get(simbolo));
		Casos(Primeiro,simbolo);
		imprimaConjunto(Primeiro.get(simbolo));
	}
	
	public static void Casos(HashMap<String, HashSet<String>> Primeiro, String simbolo){
		switch(distinguirCasos(simbolo)){
		case 1:
			System.out.println("É um terminal");
			HashSet<String> conjunto = new HashSet<String>();	// ele vem vazio
			conjunto.add(simbolo);								// adiciona o terminal ao conjunto
			Primeiro.put(simbolo, conjunto);
			break;
		case 2:
			System.out.println("É uma variavel");
			boolean alterou = true;
			
			ArrayList<Entrada> listaProducoesSimbolo = new ArrayList<Entrada>();
			listaProducoesSimbolo.addAll(producoesDoSimbolo(simbolo));
			while(alterou){
				for(Entrada producao : listaProducoesSimbolo){
					System.out.println("Producao : " + producao.getCompleto());
					
					
					HashSet<String> conjuntoAuxSimbolo = new HashSet<String>();		// Armazena o conjunto daquele simbolo
					conjuntoAuxSimbolo.addAll(Primeiro.get(simbolo));				// para ver se tem ou não alteracoes
					
					int k =1;
					boolean Continue = true;
					int n = producao.getDireita().length();
					while(Continue && k<=n){
						Continue = false;
						String simboloK = producao.getDireita().charAt(k-1)+"";
						if(isTerminal(simboloK)){
							Primeiro.get(simbolo).add(simboloK);
						}else
							Primeiro.get(simbolo).addAll(Primeiro.get(producao.getDireita().charAt(k-1)));
						Primeiro.get(simbolo).remove("E");
						
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
					
					
					if(conjuntoAuxSimbolo.equals(Primeiro.get("E"))){
						alterou = false;
					}
				}
			}
			
			break;
		case 3:
			System.out.println("É uma cadeia");
			break;
		case 0:
			System.out.println("É um epsilon");
			break;
		}
	}
	
	public static int distinguirCasos(String simbolo){
			if(simbolo.length() == 1){
				if(isTerminal(simbolo))
					return 1;
				else if(isVariavel(simbolo))
					return 2;
			}else{
				return 3;
			}
			return 0;
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
