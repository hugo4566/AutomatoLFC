package dominios;

public class Entrada {

	private String completo;
	private String esquerda;
	private String direita;
	
	public Entrada(String completo, String esquerda, String direita){
		this.completo = completo;
		this.esquerda = esquerda;
		this.direita = direita;
	}
	
	public String getCompleto() {
		return completo;
	}
	public void setCompleto(String completo) {
		this.completo = completo;
	}
	public String getEsquerda() {
		return esquerda;
	}
	public void setEsquerda(String esquerda) {
		this.esquerda = esquerda;
	}
	public String getDireita() {
		return direita;
	}
	public void setDireita(String direita) {
		this.direita = direita;
	}
	
}
