package compiler;

import java.util.ArrayList;

public class Semantica {
	
	public static ArrayList<Funcao> funcs;
	public static ArrayList<Identificador> listaIds;
	public static boolean funcsDuplicadas;
	public static boolean paramsDuplicados;
	public static boolean paramsNaoExistem;
	
	public static void checaSemantica(No raiz) {
		funcs = new ArrayList<>();
		listaIds = new ArrayList<Identificador>();
		funcsDuplicadas = false;
		paramsDuplicados = false;
		paramsNaoExistem = false;
		
		preencheFuncs(raiz); // preenche a lista de funcoes com todas as funcoes do codigo
		//agora, ver se ha duas funcoes com mesmo nome
		//d) não há funções declaradas mais de uma vez. 
		if(funcsDuplicadas) {
			System.out.println("Erro semântico: duas funções com mesmo nome.");
		}
		
		
		//c) não há parâmetros repetidos (na declaração de uma função),  
		for(Funcao f1: funcs) {
			ArrayList<String> listParams = f1.params;
			for (int i = 0; i < listParams.size(); i++) {
				for (int j = i+1; j < listParams.size(); j++) {
					if(listParams.get(i).equals(listParams.get(j))) {
						paramsDuplicados = true;
					}
				}
			}
		}		
		if(paramsDuplicados) {
			System.out.print("Erro de semantica: parametros duplicados");
		}
		
		//b) as funções chamadas foram definidas e com a quantidade de parâmetros compatível,  
		
		//a) os identificadores que aparecem em cada função são passados como parâmetros, 
		//primeiro colocar todos os identificadores inicializados numa lista, percorrendo da raiz procurando comandos de atribuicao
		recuperaIds(raiz);
		//depois, percorrer o programa e, para cada identificador que acharmos, ver se ha um com o mesmo nome na lista
		//se na houver, erro
		checaIds(raiz);		
		
		System.out.println("Analise Semantica Concluida");
	}

	public static void checaIds(No raiz) {
		boolean naoEncontrou = true;
		if(!checaNaoIdentificador(raiz.getToken())) {
			if(checaNaoFuncao(raiz.getToken())) {
				for(Identificador i: listaIds) {
					if(raiz.getToken().equals(i.nome)) {
						naoEncontrou = false;
					}
				}
				if(naoEncontrou) {
					System.out.println("Erro de semantica: variavel nao declarada");
				}
			}			
		}
		if(raiz.getFilhos() != null) {
			for(No n: raiz.getFilhos()) {
				checaIds(n);
			}
		}
	}

	private static boolean checaNaoFuncao(String token) {
		for(Funcao f: funcs) {
			if(f.nome.equals(token)) {
				return false;
			}
		}
		return true;
	}

	public static void recuperaIds(No raiz) {
		
		if(!checaNaoIdentificador(raiz.getToken()) && raiz.getFilhos() != null ) {
			if(raiz.getFilho(0).getToken().equals("=")) {
				Identificador id = new Identificador(raiz.getToken(), Integer.parseInt(raiz.getFilho(1).getToken()));
				listaIds.add(id);	
			}			
		}
		if(raiz.getFilhos() != null) {
			for(No n: raiz.getFilhos()) {
				recuperaIds(n);
			}	
		}
	}	

	public static boolean checaNaoIdentificador(String token) {

		if(	token.equals(";")||
				token.equals("-")||
				token.equals("+")||
				token.equals("*")||
				token.equals("<")||
				token.equals("=")||
				token.equals("then")||
				token.equals("else")||
				token.equals(",")||
				token.equals(">")||
				token.equals("/")||
				token.equals("(")||
				token.equals(")")||
				token.equals("def")||
				token.equals("if")||
				isNumeric(token)				
		) {
			return true;
			
		}else {
			return false;
		}
	}

	@SuppressWarnings("unused")
	public static boolean isNumeric(String str) {
		try	{  
			double d = Double.parseDouble(str);  
		} catch(NumberFormatException nfe) {  
			return false;  
		}
	  return true;  
	}	

	public static void preencheFuncs(No raiz) {
		if(raiz.getToken().equals("def")) {
			//primeiro filho é o nome da funcao
			//d) não há funções declaradas mais de uma vez. - ver se nao ha outras funcoes com mesmo nome
			for(Funcao f1: funcs) {
				if(f1.nome.equals(raiz.getFilho(0).getToken())) {
					funcsDuplicadas = true;
				}
			}
			Funcao f = new Funcao(raiz.getFilho(0).getToken());			
			//o proximo filho eh "(", depois vem o primeiro parametro, cujos filhos serão virgulas e outros parametros			
			//getFilho(1) = "("
			f.params.add(raiz.getFilho(2).getToken());
			recuperaParams(raiz.getFilho(2), f);
			funcs.add(f);
		}
		
		if(raiz.getFilhos() != null) {
			for(No n: raiz.getFilhos()) {
				preencheFuncs(n);
			}	
		}	
	}

	public static void recuperaParams(No node, Funcao f) {
		f.params.add(node.getFilho(1).getToken());
		if(node.getFilho(1).getFilhos() != null) {
			recuperaParams(node.getFilho(1), f);
		}
	}

}
