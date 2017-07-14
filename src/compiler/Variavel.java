package compiler;

public class Variavel {

	private String identificador;
	private String registrador;
	
	public Variavel (String id, String reg) {
		this.identificador = id;
		this.registrador = reg;
	}
	
	public String getIdentificador() {
		return this.identificador;
	}
	
	public String getRegistrador() {
		return this.registrador;
	}
	
}
