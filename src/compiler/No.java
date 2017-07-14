package compiler;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

public class No {
    
	private Token token;
    private String tipo;
    private List<No> filhos;
	private String[] tipos = {	"EXP",
								"EXP_ARIT", //codeGen
		    					"EXP_REL",
		    					"OP_ARIT", //codeGen
		    					"OP_REL",
		    					"SE", //codeGen
		    					"ENTAO", //codeGen
		    					"SENAO", //codeGen				//Os que estão com "codeGen" estão tradados na geração de código
		    					"DEF", //codeGen				//"P", "I", "D", "ARGS", "SEQ" não são utilizados
		    					"MAIOR",
		    					"MENOR",
		    					"IGUAL", //codeGen
		    					"SOMA", //codeGen
		    					"SUBTRACAO", //codeGen
		    					"MULTIPLICACAO",
		    					"DIVISAO",
		    					"ABRE_PARENTESIS", //codeGen
		    					"FECHA_PARENTESIS", //codeGen
		    					"PONTO_E_VIRGULA", //codeGen
		    					"VIRGULA", //codeGen
		    					"IDENTIFICADOR", //codeGen
		    					"IDENTIFICADOR_FUNCAO",
		    					"IDENTIFICADOR_FUNCAO_DEF", //codeGen
		    					"IDENTIFICADOR_PARAM", //codeGen
		    					"INTEIRO" //codeGen
		    				};

    public No() {
    	this.token = null;
    	this.filhos = new ArrayList<>();
    }

    public No(Token token, String tipo) throws Exception{
    	if(tipoValido(tipo)) {
	        this.token = token;
	        this.tipo = tipo;
	        this.filhos = new ArrayList<>();
    	}
    	else
    		throw new Exception("Invalid node type");
    }
    
    public No(No no){
        this.token = no.token;
        this.tipo = no.tipo;
        this.filhos = no.filhos;
    }
    
    public No(No no, String tipo){
        this.token = no.token;
        this.tipo = tipo;
        this.filhos = no.filhos;
    }
    
    public String getToken() {
    	return this.token.toString();
    }
    
    public String getTipo() {
    	return this.tipo;
    }

    public void addFilho(Token token, String tipo) throws Exception{
        try {
			filhos.add(new No(token, tipo));
		} catch (Exception e) {
			throw new Exception(e);
		}
    }

    public void addFilho(No no){
    	filhos.add(no);
    }
    
    public void addFilho(No no, String tipo){
    	no.tipo = tipo;
    	filhos.add(no);
    }
    
    public List<No> getFilhos() {
    	if(this.filhos.size() > 0)
    		return this.filhos;
    	else
    		return null;
    }
    
    public No getFilho(int position) {
    	if(this.filhos.size() > position) {
    		if(this.filhos.get(position) != null)
    			return this.filhos.get(position);
    	}
    	return null;
    }	
    
    public No getNeto(int posFilho, int posNeto) {
    	if(this.filhos.size() > posFilho) {
    		if(this.filhos.get(posFilho).filhos.size() > posNeto) {
    			if(this.filhos.get(posFilho).filhos.get(posNeto) != null)
        			return this.filhos.get(posFilho).filhos.get(posNeto);
    		}
    	}
    	return null;
    }
    
    public No getBisneto(int posFilho, int posNeto, int posBisneto) {
    	if(this.filhos.size() > posFilho) {
    		if(this.filhos.get(posFilho).filhos.size() > posNeto) {
    			if(this.filhos.get(posFilho).filhos.get(posNeto).filhos.size() > posBisneto) {
    				if(this.filhos.get(posFilho).filhos.get(posNeto).filhos.get(posBisneto) != null)
    					return this.filhos.get(posFilho).filhos.get(posNeto).filhos.get(posBisneto);
    			}
    		}
    	}
    	return null;
    }
    
    public Boolean tipoValido(String tipo) {
    	for (String tipoDef : this.tipos)
    		if (tipoDef.equals(tipo))
    			return true;
    	return false;
    }
    
    public Boolean comparaTipo(String tipo) {
    	return this.tipo.equals(tipo);
    }
    
    public Boolean comparaTipoFilho(int posicaoFilho, String tipo) {
    	return this.getFilho(posicaoFilho).getTipo().equals(tipo);
    }
    
  	public Boolean compareToken(String token) {
  		return this.getToken().equals(token);
  	}
  	
  	public int countArgs() {
		if(this.getFilhos() != null && this.getFilho(2).getFilhos() != null)
			return this.getFilho(2).recursiveCountArgs();
		return 1;
	}
  	
  	public int recursiveCountArgs() {
  		if (this.getFilhos() == null)
  			return 1;
  		else
  			return 1 + this.getFilho(1).recursiveCountArgs();
  	}

    public String escreve(Token token) throws IOException {	  	
		switch(token.valor) {
			case "(":
				return "(";
			case ")":
				return ")";
			case "if":
				return "if ";
			case "then":
				return "then ";
			case "else":
				return "else ";
			case "def":
				return "def ";
			case "#":
				return "";
		}
		return token.valor + " ";
	}
    
    @Override
    public String toString() {
        if (this.token != null)
			try {
				imprime();
			} catch (IOException e) {
				e.printStackTrace();
			}
        return "";
    }
    
    public void imprime() throws IOException {
    	this.imprime("", false);
    }
    
    private void imprime(String prefixo, boolean folha) throws IOException {
        if(this.token != null && this.token.valor != null) {
        	String tabulacao = "", espaco = "";
            
            if(folha) {
            	tabulacao = " -- ";
            	espaco ="    ";
            } else{
            	tabulacao = "|-- ";
            	espaco = "|   ";
            }    
            
            System.out.println(prefixo + tabulacao + this.getToken() + "\t\t\t\t(" + this.getTipo() + ")");
            
            for (int i = 0; i < filhos.size() - 1; i++) {
                if(filhos.get(i) != null)
                	filhos.get(i).imprime(prefixo + espaco, false);
            }
            
            if (filhos.size() > 0 && filhos.get(filhos.size() - 1) != null)
                filhos.get(filhos.size() - 1).imprime(prefixo + espaco, true);
        }
    }
    
    public String retornaString() throws IOException {
    	return this.retornaString("", false);
    }
    
    public String retornaString(String prefixo, boolean folha) throws IOException {
        String result = "";
    	if(this.token != null && this.token.valor != null) {
        	String tabulacao = "", espaco = "";
            
            if(folha) {
            	tabulacao = " -- ";
            	espaco ="    ";
            } else {
            	tabulacao = "|-- ";
            	espaco = "|   ";
            }    
            
            result += prefixo + tabulacao + this.getToken() + "\t\t\t\t(" + this.getTipo() + ")\n";
            
            for (int i = 0; i < filhos.size() - 1; i++) {
                if(filhos.get(i) != null)
                	result += filhos.get(i).retornaString(prefixo + espaco, false);
            }
            
            if (filhos.size() > 0 && filhos.get(filhos.size() - 1) != null)
                result += filhos.get(filhos.size() - 1).retornaString(prefixo + espaco, true);
        }
    	return result;
    }
}
