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
	static HashSet<String> conjuntoSimbolos;
	static ArrayList<Entrada> Lista;
	
	public static void main (String [] args) throws FileNotFoundException {
		
		conjuntoVariaveis = new HashSet<String>();
		conjuntoTerminais = new HashSet<String>();
		conjuntoSimbolos = new HashSet<String>();
		
		Lista = new ArrayList<Entrada>();
		
		// Parser
		Scanner scanner = new Scanner(new FileReader("arquivo.txt")).useDelimiter(",");
		while (scanner.hasNext()) {
			Lista.add(setEntrada(scanner.next()));
		}
		
		conjuntoVariaveis = todasVariaveis(conjuntoVariaveis, Lista);
		conjuntoTerminais = todosTerminais(conjuntoTerminais,conjuntoVariaveis, Lista);
		conjuntoSimbolos.addAll(conjuntoVariaveis);
		conjuntoSimbolos.addAll(conjuntoTerminais);

//		imprimirTodasVariaveis();
//		imprimirTodosTerminais();
		
		HashMap<String, HashSet<String>> Primeiro = new HashMap<String, HashSet<String>>();
		HashMap<String, HashSet<String>> Sequencia = new HashMap<String, HashSet<String>>();
		
		// Primeiro Epsilon
		HashSet<String> cj = new HashSet<String>();
		cj.add("E");
		Primeiro.put("E", cj);	
		
		
		// Caso 1 -- fa�o Primeiro(a) = {a}
		doCaso1(Primeiro);
		
		// Caso 2 -- faco Primeiro(A):
		doCaso2(Primeiro);
		
		// Caso 3 -- faco Primeiro(w), onde w = X_1X_2...X_n
//		doCaso3(Primeiro);
		
//		doPasso4(Sequencia,Primeiro);
		
		for(String variavel:conjuntoVariaveis){
			imprimaConjunto(Primeiro.get(variavel));
		}
	}

	private static void doPasso4(HashMap<String, HashSet<String>> Sequencia,HashMap<String, HashSet<String>> Primeiro) {
		
		// Sequencia(vari�vel inicial) := {$};
		HashSet<String> cj = new HashSet<String>();
		cj.add("$");
		Sequencia.put("�", cj);
		
		// for cada n�o-terminal A <> vari�vel inicial do Sequencia(A) := {};
		for(String variavel: conjuntoVariaveis){
			HashSet<String> vazio = new HashSet<String>();
			Sequencia.put(variavel, vazio);
		}
		
		int somaAnterior = somaDosConjuntosSequencia(Sequencia);
		
		boolean alteracao = true;
		while(alteracao){
			alteracao = false;
			
			for(String variavel:conjuntoVariaveis){
				String simbolo = variavel;
				ArrayList<Entrada> listaProducoesSimbolo = new ArrayList<Entrada>();
				listaProducoesSimbolo.addAll(producoesDoSimbolo(simbolo));
				
				for(Entrada producao : listaProducoesSimbolo){
					int n = producao.getDireita().length();
					
					for(int i=1;i<=n;i++){
						String simboloI = producao.getDireita().charAt(i-1)+"";
						if(isVariavel(simboloI)){
							String cadeia = getCadeia(producao.getDireita(),i-1);
							System.out.println(cadeia);
							
//							adicione Primeiro(X_i+1 X_i+2 ... X_n) - {epsilon} a Sequencia(X_i);
							HashSet<String> conjuntoTesteK = new HashSet<String>();
							conjuntoTesteK.addAll(Primeiro.get(cadeia));
							conjuntoTesteK.remove("E");

							Sequencia.get(simbolo).addAll(conjuntoTesteK);

							// Testa se contem ou nao Epsilon
							HashSet<String> conjuntoTeste = new HashSet<String>();
							conjuntoTeste.addAll(Primeiro.get(cadeia));
							
							
							
//							if epsilon estiver em Primeiro(X_i+1 X_i+2 ... X_n) then
//							adicione Sequencia(A) a Sequencia(X_i)
							if(!conjuntoTeste.add("E")){		// se tem Epsilon
								HashSet<String> conjuntoA = new HashSet<String>();
								conjuntoA.addAll(Sequencia.get(variavel));
								Sequencia.get(simboloI).addAll(conjuntoA);
							}
						}
						
						if(somaAnterior != somaDosConjuntosSequencia(Sequencia)){
							alteracao = true;
						}

						somaAnterior = somaDosConjuntosSequencia(Sequencia);
					}
				}
			}
		}
	}

	private static String getCadeia(String direita, int posicao) {
//		(* Nota: se i = n, ent�o X_i+1 X_i+2 ... X_n = epsilon *)
		int tamanhoCadeia = direita.length();
		String cadeia = "";
		if(posicao == tamanhoCadeia-1){
			return "E";	//
		}else{
			for(int i = posicao;i<tamanhoCadeia;i++){
				if(i==posicao){}
				else{
					cadeia = cadeia+direita.charAt(i);
				}
			}
			return cadeia;
		}
	}

	private static void doCaso1(HashMap<String, HashSet<String>> Primeiro) {
		for(String variavel:conjuntoTerminais){
			HashSet<String> cjT = new HashSet<String>();
			cjT.add(variavel);
			Primeiro.put(variavel, cjT);
		}
	}

	private static void doCaso2(HashMap<String, HashSet<String>> Primeiro) {
		
		// Iniciei Primeiro(variavel) = {}
		for(String variavel:conjuntoVariaveis){
			HashSet<String> cjV = new HashSet<String>();
			Primeiro.put(variavel, cjV);
		}
				
		int somaAnterior = somaDosConjuntosPrimeiros(Primeiro);
		
		boolean alteracao = true;
		while(alteracao){
			alteracao = false;
			
			for(String variavel:conjuntoVariaveis){
				String simbolo = variavel;
				ArrayList<Entrada> listaProducoesSimbolo = new ArrayList<Entrada>();
				listaProducoesSimbolo.addAll(producoesDoSimbolo(simbolo));

				for(Entrada producao : listaProducoesSimbolo){
					int n = producao.getDireita().length();

					int k =1; boolean Continue = true;
					while(Continue && k<=n){
						String simboloK = producao.getDireita().charAt(k-1)+"";

						HashSet<String> conjuntoTesteK = new HashSet<String>();
						conjuntoTesteK.addAll(Primeiro.get(simboloK));
						conjuntoTesteK.remove("E");

						Primeiro.get(simbolo).addAll(conjuntoTesteK);

						// Testa se contem ou nao Epsilon
						HashSet<String> conjuntoTeste = new HashSet<String>();
						conjuntoTeste.addAll(Primeiro.get(simboloK));
						if(conjuntoTeste.add("E")){		// nao tem Epsilon
							Continue = false;
						}
						k++;
					}

					if(Continue){
						Primeiro.get(simbolo).add("E");
					}

					
					if(somaAnterior != somaDosConjuntosPrimeiros(Primeiro)){
						alteracao = true;
					}

					somaAnterior = somaDosConjuntosPrimeiros(Primeiro);
				}
			}
		}
	}
	
	private static void doCaso3(HashMap<String, HashSet<String>> Primeiro) {
		
		ArrayList<Entrada> listaCadeias = getListaCadeidas();
		
		for(Entrada cadeia: listaCadeias){
			String w = cadeia.getDireita();
			String X_1 = cadeia.getDireita().charAt(0)+"";
			
			// Cria o Primeiro(w) = Primeiro(X_1) - {E}
			HashSet<String> cj1 = new HashSet<String>();
			cj1.addAll(Primeiro.get(X_1));
			cj1.remove("E");
			
			Primeiro.put(w, cj1);
			
			// Verificacao do Epsilon
			boolean vazio = false;
			HashSet<String> cjX_1Teste = new HashSet<String>();
			cjX_1Teste.addAll(Primeiro.get(X_1));
			if(!cjX_1Teste.add("E")){	// se Primeiro(X_1) tiver Epsilon 
				vazio = true;
			}
			
			int n = cadeia.getDireita().length();
			
			for(int i=2;i<=n;i++){
				
				if(vazio == false){
					break;
				}
				
				for(int k=1;k<=i-1;k++){
					
				}
			}
			
		}

		
		
		
		
//		Primeiro(w) := Primeiro(X_1) - {epsilon};
//		if Primeiro(X_1) contiver epsilon then
//			vazio := true;
//		for cada i = 2 at� n do
//			if vazio = false then break;
//			for cada k = 1 at� i-1 do
//				if Primeiro(X_k) n�o contiver epsilon then vazio := false;
//			if vazio = true then adicione Primeiro(X_i)- {epsilon} a Primeiro(w);
//		if vazio = true then adicione {epsilon} a Primeiro(w);
	}
	
	public static ArrayList<Entrada> getListaCadeidas(){		// essa funcao retorna as producoes que tem cadeia, ou seja, tem tamanho>1
		ArrayList<Entrada> listaCadeias = new ArrayList<Entrada>();
		for(Entrada producao: Lista){
			if(producao.getDireita().length()>1){
				listaCadeias.add(producao);
			}
		}
		return listaCadeias;
	}
	
	private static int somaDosConjuntosPrimeiros(HashMap<String, HashSet<String>> Primeiro) {
		int soma = 0;
		for(String variavel:conjuntoSimbolos){
			soma = soma + Primeiro.get(variavel).size();
		}
		soma++;	// para o epsilon
		return soma;
	}
	
	private static int somaDosConjuntosSequencia(HashMap<String, HashSet<String>> Sequencia) {
		int soma = 0;
		for(String variavel:conjuntoVariaveis){
			soma = soma + Sequencia.get(variavel).size();
		}
		soma++;	// para o epsilon
		return soma;
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
			System.out.println("N�o tem conjunto! N�o eh que esteja vazia, s� n�o existe!");
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
