package projeto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Base {

	public static void main(String[] args) {
		ArrayList<String> listaA = new ArrayList<String>();
		ArrayList<String> listaE = new ArrayList<String>();
		ArrayList<String> listaVariaveis = new ArrayList<String>();
		ArrayList<String> listaW = new ArrayList<String>();
		HashMap<String, HashSet<String>> Primeiro = new HashMap<String, HashSet<String>>();
		HashMap<String, HashSet<String>> Sequencia = new HashMap<String, HashSet<String>>();

		HashSet<String> vazio = new HashSet<String>();		// conjunto vazio
		for(String variavel:listaVariaveis){
			Primeiro.put(variavel, vazio);					// Primeiro(variavel,{})
		}
		
		// Caso 1
		String terminal;
		HashSet<String> conjuntoDoTerminal = new HashSet<String>();
		conjuntoDoTerminal.add(terminal);
		Primeiro.put(terminal, conjuntoDoTerminal);
		
		
		// Caso 2
		boolean alterou = true;
		
		while(alterou){
			for(String producao: listaE){
				HashSet<String> conjuntoAuxE = new HashSet<String>();
				conjuntoAuxE.addAll(Primeiro.get("E"));
				
				
				int k=1;
				boolean Continue = true;
				int n = producao.length();
				while (Continue && k<=n){
					HashSet<String> conjunto = new HashSet<String>();
					conjunto.addAll(Primeiro.get("E"));
					conjunto.addAll("Xk");
					conjunto.remove("Epsilon");
					if(!conjunto.contains("Epsilon")){
						Continue = false;
					}
					k++;
				}
				if(Continue){
					HashSet<String> conjunto = new HashSet<String>();
					conjunto.addAll(Primeiro.get("E"));
					conjunto.add("Epsilon");
				}
				
				
				if(conjuntoAuxE.equals(Primeiro.get("E"))){
					alterou = false;
				}
			}
		}
		
		// Caso 3
		HashSet<String> conjunto = new HashSet<String>();
		conjunto.addAll(Primeiro.get("w"));
		conjunto.addAll("X1");
		conjunto.remove("Epsilon");
		
		if(Primeiro.get("X1").contains("Epsilon")){
			boolean vazio2 = true;
			for(int i=2;i<n;i++){
				
				if(vazio2 == false){
					break;
				}
				
				for(int k=1;k<i-1;k++){
					if(!Primeiro.get("Xk").contains("Epsilon")){
						vazio2 = false;
					}
				}
				
				if(vazio2 == true){
					Primeiro.get("w").addAll((Primeiro.get("Xi"));
					Primeiro.get("w").remove("Epsilon");
				}
				
				if(vazio2 == true){
					Primeiro.get("w").add("Epsilon");
				}
				
			}
		}
		
		
		// Sequencia
		HashSet<String> conjuntoSLinha = new HashSet<String>();
		conjuntoSLinha.add("$");
		String VARIAVEL_INICIAL = "S'";
		Sequencia.put(VARIAVEL_INICIAL, conjuntoSLinha);
		
		for(String variavel : listaVariaveis){
			if(!variavel.equals(VARIAVEL_INICIAL)){
				Sequencia.put("A", vazio);
			}
		}
		
		
		char NAO_TERMINAL;
		boolean alterou2 = true; 
		while(alterou2){
			for(String producao: listaA){
				String cadeiaDaProducao;
				for(int x;x<cadeiaDaProducao.length();x++){
					if(x == cadeiaDaProducao.length()){
						Sequencia.get("xn").add("Epsilon");
					}
					else if(cadeiaDaProducao.charAt(x) == NAO_TERMINAL){
						Sequencia.get("x").addAll(Primeiro.get("xi+1....Xn"));
						Sequencia.get(x).remove("Epsilon");
					}
					
					if(Primeiro.get("xi+1....Xn").contains("Epsilon")){
						Sequencia.get("xi").addAll(Sequencia.get("A"));
					}
				}
			}
		}	
	}
}
