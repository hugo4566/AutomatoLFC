package projeto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import dominios.Entrada;
import dominios.Tabela;

public class Principal {

	static HashSet<String> conjuntoVariaveis;
	static HashSet<String> conjuntoTerminais;
	static HashSet<String> conjuntoSimbolos;
	static ArrayList<Entrada> Lista;
	
	public static void main (String [] args) throws IOException {
		
//        File arquivoEntrada = new File( args[0] ); 
//        fr = new FileReader( arquivoEntrada ); 
//        br = new BufferedReader( fr ); 
//
//        
//        File arquivoSaida = new File( args[1] );
//        FileWriter fw = new FileWriter( arquivoSaida ); 
//        BufferedWriter bw = new BufferedWriter( fw ); 
        
        
		
		conjuntoVariaveis = new HashSet<String>();
		conjuntoTerminais = new HashSet<String>();
		conjuntoSimbolos = new HashSet<String>();
		Lista = new ArrayList<Entrada>();
		
		ArrayList<String> listaDeTeste = new ArrayList<String>();
		ArrayList<Tabela> tabela = new ArrayList<Tabela>();
		
		HashMap<String, HashSet<String>> Primeiro = new HashMap<String, HashSet<String>>();
		HashMap<String, HashSet<String>> Sequencia = new HashMap<String, HashSet<String>>();
		
		// Parser
		Scanner scanner = new Scanner(new FileReader("arquivo.txt"));
		boolean primeiro = true;
		while (scanner.hasNext()) {
			String str = scanner.next();
			if(primeiro == true){
				String s[] = str.split(",");
				for(String entrada : s)
					Lista.add(setEntrada(entrada));
				primeiro = false;
			}else{
				listaDeTeste.add(str);
			}
		}

		scanner.close();
		
		conjuntoVariaveis = todasVariaveis(conjuntoVariaveis, Lista);
		conjuntoTerminais = todosTerminais(conjuntoTerminais,conjuntoVariaveis, Lista);
		conjuntoSimbolos.addAll(conjuntoVariaveis);
		conjuntoSimbolos.addAll(conjuntoTerminais);

//		imprimirTodasVariaveis();
//		imprimirTodosTerminais();
		
		// Primeiro Epsilon
		HashSet<String> cj = new HashSet<String>();
		cj.add("E");
		Primeiro.put("E", cj);	
		
		
		// Caso 1 -- faço Primeiro(a) = {a}
		doCaso1(Primeiro);
		
		// Caso 2 -- faco Primeiro(A):
		doCaso2(Primeiro);
		
		// Caso 3 -- faco Primeiro(w), onde w = X_1X_2...X_n
		doCaso3(Primeiro);
		
		doPasso4(Sequencia,Primeiro);
		
		criadorDeTabela(Primeiro, Sequencia, tabela);
		
		System.out.println(Primeiro.toString());
		System.out.println(Sequencia.toString());
		
		for(Tabela tab: tabela){
			System.out.println("Coluna : "+tab.getColuna()+" .. Linha : "+tab.getLinha()+" .. Dado : "+tab.getDado());
		}
	}

	private static void criadorDeTabela(HashMap<String, HashSet<String>> Primeiro, HashMap<String, HashSet<String>> Sequencia, ArrayList<Tabela> tabela) {
		for(Entrada producao: Lista){
			if(Primeiro.get(producao.getDireita()).contains("E")){
				HashSet<String> sequenciaA = Sequencia.get(producao.getEsquerda());
				for(String a : sequenciaA){
					Tabela tab = new Tabela(producao.getEsquerda(),a,producao.getCompleto());
					tabela.add(tab);
				}
			}else{
				HashSet<String> primeiroW = Primeiro.get(producao.getDireita());
				for(String a : primeiroW){
					Tabela tab = new Tabela(producao.getEsquerda(),a,producao.getCompleto());
					tabela.add(tab);
				}
			}
		}
	}

	private static void doPasso4(HashMap<String, HashSet<String>> Sequencia,HashMap<String, HashSet<String>> Primeiro) {
		

		String VARIAVEL_INICIAL = getVariavelInicial(Lista); 
		
		// Sequencia(variável inicial) := {$};
		HashSet<String> cj = new HashSet<String>();
		cj.add("$");
		Sequencia.put(VARIAVEL_INICIAL, cj);
		
		// for cada não-terminal A <> variável inicial do Sequencia(A) := {};
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
							HashSet<String> conjuntoTesteK = new HashSet<String>();
							
							// Testa se contem ou nao Epsilon
							HashSet<String> conjuntoTeste = new HashSet<String>();
							
							if(i==n){
								conjuntoTesteK.addAll(Primeiro.get("E"));
								conjuntoTesteK.remove("E");
								Sequencia.get(simbolo).addAll(conjuntoTesteK);
								
								conjuntoTeste.addAll(Primeiro.get("E"));
							}else{
//							adicione Primeiro(X_i+1 X_i+2 ... X_n) - {epsilon} a Sequencia(X_i);
								conjuntoTesteK.addAll(Primeiro.get(producao.getDireita().charAt(i)+""));
								conjuntoTesteK.remove("E");
								Sequencia.get(simbolo).addAll(conjuntoTesteK);
								
								conjuntoTeste.addAll(Primeiro.get(producao.getDireita().charAt(i)+""));
							}
							
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
//					Primeiro.put(w, cjI);
				}
			}
			
			if(vazio == true){
				Primeiro.get(w).add("E");
			}
		}
	}
	
	public static String getVariavelInicial(ArrayList<Entrada> lista){
		if(lista.size()>0){
			return lista.get(0).getEsquerda();
		}
		else
			return null;
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
