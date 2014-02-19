package projeto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

import dominios.Entrada;
import dominios.Tabela;

public class Principal {

	static HashSet<String> conjuntoVariaveis;
	static HashSet<String> conjuntoTerminais;
	static HashSet<String> conjuntoSimbolos;
	static ArrayList<Entrada> Lista;
	static ArrayList<Tabela> tabela;
	static Stack<String> pilha;
	static String VARIAVEL_INICIAL;
	
	public static void main (String [] args) throws IOException {
		
		conjuntoVariaveis = new HashSet<String>();													// crio conjunto de variaveis
		conjuntoTerminais = new HashSet<String>();													// crio conjunto de terminais
		conjuntoSimbolos = new HashSet<String>();													// crio conjunto de simbolos
		Lista = new ArrayList<Entrada>();															// crio lista de producoes da gramatica
		
		ArrayList<String> listaDeTeste = new ArrayList<String>();									// crio lista de dados de teste
		tabela = new ArrayList<Tabela>();															// crio minha tabela
		pilha = new Stack<String>();																// crio minha pilha
		
		HashMap<String, HashSet<String>> Primeiro = new HashMap<String, HashSet<String>>();			// crio meu HashMap para ser o Primeiro
		HashMap<String, HashSet<String>> Sequencia = new HashMap<String, HashSet<String>>();		// crio meu HashMap para ser o Sequencia
		
		Scanner scan = new Scanner(System.in);
        String line;
        boolean primeiro = true;
		while (!(line = scan.nextLine()).equals("")){
			if(primeiro == true){
				String s[] = line.split(",");
				for(String entrada : s)
					Lista.add(setEntrada(entrada));
				primeiro = false;
			}else{
				listaDeTeste.add(line+"$");
			}
		}

		conjuntoVariaveis = todasVariaveis(conjuntoVariaveis, Lista);							// preencho meu conjunto de variaveis
		conjuntoTerminais = todosTerminais(conjuntoTerminais,conjuntoVariaveis, Lista);			// preencho meu conjunto de terminais
		conjuntoSimbolos.addAll(conjuntoVariaveis);												// preencho meu conjunto de simbolos com o conjunto de variaveis
		conjuntoSimbolos.addAll(conjuntoTerminais);												// preencho meu conjunto de simbolos com o conjunto de terminais
		
		// Faço o Primeiro(Epsilon) = {E} , para caso seja chamado
		HashSet<String> cj = new HashSet<String>();
		cj.add("E");
		Primeiro.put("E", cj);	
		
		// Caso 1 -- faco Primeiro(a) = {a}
		doCaso1(Primeiro);
		
		// Caso 2 -- faco Primeiro(A):
		doCaso2(Primeiro);
		
		// Caso 3 -- faco Primeiro(w), onde w = X_1X_2...X_n
		doCaso3(Primeiro);
		
		// Passo 4 -- é reponsavel por preencher as Sequencias
		doPasso4(Sequencia,Primeiro);
		
		// Preencho a tabela com as informacoes
		criadorDeTabela(Primeiro, Sequencia, tabela);
		
//		System.out.println(Primeiro.toString());
//		System.out.println(Sequencia.toString());
//		
//		for(Tabela tab: tabela){
//			System.out.println("Coluna : "+tab.getColuna()+" .. Linha : "+tab.getLinha()+" .. Dado : "+tab.getDado());
//		}
		
		for(String teste : listaDeTeste){
//			System.out.println("\nExemplo de Teste : "+teste);
			pilha = new Stack<String>();					// inicializo a pilha
			pilha.add("$");									// boto $ na pilha
			pilha.add(VARIAVEL_INICIAL);					// boto a VARIAVEL_INICIAL na pilha
			
			int resultado = testarCadeia(teste);
			System.out.println(resultado);
		}
	}

	// Metodo para testar a cadeia de teste
	public static int testarCadeia(String teste) throws IOException {
		
		int n = teste.length();
		for(int i=0;i<n;i++){
			boolean naoProssiga = true;
			while(naoProssiga){
				
				String simboloPilha = pilha.get(pilha.size()-1);
				String simboloCadeia = teste.charAt(i)+"";
				
				String dado = pegaDadoTabela(simboloCadeia, simboloPilha);
				if(dado != null){
					empilhaDado(dado);							// se estiver na tabela
				}else{
					if(simboloCadeia.equals(simboloPilha)){		// se os simbolos forem iguais e nao estiverem na tabela
						pilha.remove(pilha.size()-1);
						naoProssiga = false;
					}
					else{
						return 0;								// se os simbolos forem diferentes e nao estiverem na tabela
					}
				}
			}
		}
		
		if(pilha.isEmpty()){
			return 1;											// se a pilha esvaziou é pq foi percorrida com sucesso
		}else{
			return 0;											// senao foi fracasso
		}
	}
	
	// Metodo para empilhar o dado
	public static void empilhaDado(String dado){
		String dadoSemSeta = dado.substring(3,dado.length());
		int n = dadoSemSeta.length();
		if(n==1){
			pilha.remove(pilha.size()-1);
			if(dadoSemSeta.charAt(0) != 'E'){
				pilha.add(dadoSemSeta.charAt(0)+"");
			}
		}else{
			pilha.remove(pilha.size()-1);
			for(int i=n;i>0;i--){
				pilha.add(dadoSemSeta.charAt(i-1)+"");
			}
		}
	}
	
	// Metodo para retorna o dado da Tabela correspondente a aquela linha e coluna
	public static String pegaDadoTabela(String coluna,String linha){
		for(Tabela tab: tabela){
			if(coluna.equals(tab.getColuna()) && linha.equals(tab.getLinha())){
				return tab.getDado();
			}
		}
		return null;
	}

	// Metodo para preencer a tabela de acordo com os 2 casos
	private static void criadorDeTabela(HashMap<String, HashSet<String>> Primeiro, HashMap<String, HashSet<String>> Sequencia, ArrayList<Tabela> tabela) {
		for(Entrada producao: Lista){
			if(Primeiro.get(producao.getDireita()).contains("E")){	// se contiver epsilon
				HashSet<String> sequenciaA = Sequencia.get(producao.getEsquerda());
				for(String a : sequenciaA){
					Tabela tab = new Tabela(producao.getEsquerda(),a,producao.getCompleto());
					tabela.add(tab);
				}
			}else{													// nao contiver epsilon
				HashSet<String> primeiroW = Primeiro.get(producao.getDireita());
				for(String a : primeiroW){
					Tabela tab = new Tabela(producao.getEsquerda(),a,producao.getCompleto());
					tabela.add(tab);
				}
			}
		}
	}

	// Metodo para preencher as sequencias
	private static void doPasso4(HashMap<String, HashSet<String>> Sequencia,HashMap<String, HashSet<String>> Primeiro) {

		VARIAVEL_INICIAL = getVariavelInicial(Lista); 
		
		// Sequencia(variavel inicial) := {$};
		HashSet<String> cj = new HashSet<String>();
		cj.add("$");
		Sequencia.put(VARIAVEL_INICIAL, cj);
		
		// for cada nao-terminal A <> variavel inicial do Sequencia(A) := {};
		for(String variavel: conjuntoVariaveis){
			if(!variavel.equals(VARIAVEL_INICIAL)){
				HashSet<String> vazio = new HashSet<String>();
				Sequencia.put(variavel, vazio);
			}
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
							
							String xi_xn = "";
							if (i==n){
								xi_xn = "E";
							} else {
								xi_xn = (producao.getDireita()).substring(i);
							}
							
							HashSet<String> conjunto_xi_xn = new HashSet<String>();
							conjunto_xi_xn.addAll(Primeiro.get(retornaCadeiaDoConjunto(xi_xn,Primeiro)));
							conjunto_xi_xn.remove("E");
							
							Sequencia.get(simbolo).addAll(conjunto_xi_xn);
							
							// Testa se contem ou nao Epsilon
							HashSet<String> conjuntoTeste = new HashSet<String>();
							conjuntoTeste.addAll(Primeiro.get(xi_xn+""));
							
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
	
	public static String retornaCadeiaDoConjunto(String cadeia,HashMap<String, HashSet<String>> Primeiro){

		if (isTerminal(cadeia) || isVariavel(cadeia) || cadeia.equals("E")){
			return cadeia;
		} else {
			String w = cadeia;

			String X1 = w.charAt(0)+"";

			HashSet<String> conjunto_X1 = new HashSet<String>();
			conjunto_X1.addAll(Primeiro.get(X1));
			conjunto_X1.remove("E");

			Primeiro.put(w, conjunto_X1);

			boolean vazio = false;
			if (Primeiro.get(X1).contains("E")){
				vazio = true;
			}

			int n = w.length();

			for(int i=2-1;i<=(n-1);i++){
				if(!vazio) break;
				for(int m=1-1;m<=i-1-1;m++){
					if(!(Primeiro.get(w.charAt(m)+"").contains("E"))) vazio = false;
					if (vazio) {
						HashSet<String> conjunto_Xi = new HashSet<String>();
						conjunto_Xi.addAll(Primeiro.get(w.charAt(i)+""));
						conjunto_Xi.remove("E");
						conjunto_Xi.addAll(Primeiro.get(w));

						Primeiro.put(w, conjunto_Xi);
					}
				}
			}
			if (vazio) {
				HashSet<String> temp = new HashSet<String>();
				temp.add("E");
				temp.addAll(Primeiro.get(w));
				Primeiro.put(w, temp);
			}
		}
		return cadeia;
	}

	// Metodo para preencher todos os Primeiros dos terminais
	private static void doCaso1(HashMap<String, HashSet<String>> Primeiro) {
		for(String variavel:conjuntoTerminais){
			HashSet<String> cjT = new HashSet<String>();
			cjT.add(variavel);
			Primeiro.put(variavel, cjT);
		}
	}

	// Metodo para preencher todos os Primeiros das variaveis 
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
	
	// Metodo para o caso3
	private static void doCaso3(HashMap<String, HashSet<String>> Primeiro) {
		
		ArrayList<Entrada> listaCadeias = getListaCadeias();
		
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
					String X_k = cadeia.getDireita().charAt(k-1)+"";
					
					HashSet<String> cjX_kTeste = new HashSet<String>();
					cjX_kTeste.addAll(Primeiro.get(X_k));
					
					if(cjX_kTeste.add("E")){		// se nao Primeiro(X_K) nao tiver epsilon
						vazio = false;
					}
				}
				
				if(vazio == true){
					String X_i = cadeia.getDireita().charAt(i-1)+"";
					
					// Cria o Primeiro(w) = Primeiro(X_1) - {E}
					HashSet<String> cjI = new HashSet<String>();
					cjI.addAll(Primeiro.get(X_i));
					cjI.remove("E");
					
					// Sem certeza qual seria, ver ao debugar
					Primeiro.get(w).addAll(cjI);
				}
			}
			
			if(vazio == true){
				Primeiro.get(w).add("E");
			}
		}
	}
	
	// Metodo para retorna a variavel inicial
	public static String getVariavelInicial(ArrayList<Entrada> lista){
		if(lista.size()>0){
			return lista.get(0).getEsquerda();
		}
		else
			return null;
  	}
	
	// Metodo para retorna a lista das producoes que tem cadeia, ou seja, tem tamanho>1
	public static ArrayList<Entrada> getListaCadeias(){
		ArrayList<Entrada> listaCadeias = new ArrayList<Entrada>();
		for(Entrada producao: Lista){
			if(producao.getDireita().length()>1){
				listaCadeias.add(producao);
			}
		}
		return listaCadeias;
	}
	
	// Retorna a soma de todos os conjuntos dos primeiros
	private static int somaDosConjuntosPrimeiros(HashMap<String, HashSet<String>> Primeiro) {
		int soma = 0;
		for(String variavel:conjuntoSimbolos){
			soma = soma + Primeiro.get(variavel).size();
		}
		soma++;	// para o epsilon
		return soma;
	}
	
	// Retorna a soma de todos os conjuntos das sequencias
	private static int somaDosConjuntosSequencia(HashMap<String, HashSet<String>> Sequencia) {
		int soma = 0;
		for(String variavel:conjuntoVariaveis){
			soma = soma + Sequencia.get(variavel).size();
		}
		soma++;	// para o epsilon
		return soma;
	}
	
	// Metodo para retorna a lista de producoes daquele simbolo
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
	
	public static boolean isTerminal(String simbolo){
		HashSet<String> temp = new HashSet<String>(); 
		temp.addAll(conjuntoVariaveis);
		if(simbolo.length() >1){
			return false;
		}else{
		if(temp.add(simbolo) && !simbolo.equals("E"))
			return true;
		else
			return false;
		}
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
