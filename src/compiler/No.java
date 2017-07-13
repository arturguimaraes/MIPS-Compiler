package compiler;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

public class No {
    private Token token;
    private String tipo; //Os tipos devem ser escritos exatamente como proposto no Parser.cup, em maiúsculo
    private List<No> filhos;

    public No() {
    	this.token = null;
    	this.filhos = new ArrayList<>();
    }

    public No(Token token, String tipo) throws Exception{
    	if(validType(tipo)) {
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
    
    public Boolean validType(String tipo) {
    	String[] tipos = {
    						//"P",
			    			//"I",
			    			//"D",
			    			//"ARGS",
			    			//"SEQ",
			    			"EXP",
			    			"EXP_ARIT",
			    			"EXP_REL",
			    			"OP_ARIT",
			    			"OP_REL",
			    			"SE",
			    			"ENTAO",
			    			"SENAO",
			    			"DEF",
			    			"MAIOR",
			    			"MENOR",
			    			"IGUAL",
			    			"SOMA",
			    			"SUBTRACAO",
			    			"MULTIPLICACAO",
			    			"DIVISAO",
			    			"ABRE_PARENTESIS",
			    			"FECHA_PARENTESIS",
			    			"PONTO_E_VIRGULA",
			    			"VIRGULA",
			    			"IDENTIFICADOR",
			    			"INTEIRO"
			    		};
    	
    	for (String tipoDef : tipos)
    		if (tipoDef.equals(tipo))
    			return true;
    	
    	return false;
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
				// TODO Auto-generated catch block
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
            
            System.out.println(prefixo + tabulacao + this.getToken() + "(" + this.getTipo() + ")");
            
            for (int i = 0; i < filhos.size() - 1; i++) {
                if(filhos.get(i) != null)
                	filhos.get(i).imprime(prefixo + espaco, false);
            }
            
            if (filhos.size() > 0 && filhos.get(filhos.size() - 1) != null)
                filhos.get(filhos.size() - 1).imprime(prefixo + espaco, true);
        }
    }
}
