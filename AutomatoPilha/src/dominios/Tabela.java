package dominios;

public class Tabela {
	
	private String linha;
	
	private String coluna;
	
	private String dado;
	
	public Tabela(String linha,String coluna,String dado){
		this.linha = linha;
		this.coluna = coluna;
		this.dado = dado;
	}

	public String getLinha() {
		return linha;
	}

	public void setLinha(String linha) {
		this.linha = linha;
	}

	public String getColuna() {
		return coluna;
	}

	public void setColuna(String coluna) {
		this.coluna = coluna;
	}

	public String getDado() {
		return dado;
	}

	public void setDado(String dado) {
		this.dado = dado;
	}
}
