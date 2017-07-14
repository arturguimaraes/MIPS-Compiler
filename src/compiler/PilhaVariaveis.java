package compiler;

import java.util.ArrayList;
import java.util.List;

public class PilhaVariaveis {
	
	private List<Variavel> variaveis;
	
	public PilhaVariaveis() {
		this.variaveis = new ArrayList<Variavel>();
	}
	
	public String add(String id) {
		int count = this.variaveis.size() + 1;
		Variavel var = new Variavel(id, "$r" + count);
		this.variaveis.add(var);
		return "$r" + count;
	}
	
	public String add(Variavel var) {
		this.variaveis.add(var);
		return var.getRegistrador();
	}

	public Variavel remove() {
		return this.variaveis.remove(this.variaveis.size() - 1);
	}

	public boolean empty() {
		return this.variaveis.size() == 0;
	}
	
	public String getReg(String id) {
		for(Variavel var : this.variaveis) {
			if (var.getIdentificador().equals(id))
				return var.getRegistrador();
		}
		return null;
	}
	
	public String toString() {
		String result = "";
		for(Variavel var : this.variaveis)
			result += var.getIdentificador() + ":" + var.getRegistrador() + ">";
		return result;
	}
	
}