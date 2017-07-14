package compiler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class CodeGenerator {
	
	private No tree;
	private PilhaVariaveis variaveis;
	
	public CodeGenerator (No tree) throws IOException {
		this.tree = tree;
		this.variaveis = new PilhaVariaveis();
	}
	
	public String GenerateCode() throws FileNotFoundException {
		String result = GenerateCodeForNode(this.tree);
		PrintWriter printer = new PrintWriter("MipsResultCode.txt");
		printer.print(result);
		printer.close();
		return result;		
	}
	
	public String GenerateCodeForNode(No node) {
		String result = codeGen(node);
		//Gera código para todos os filhos
		if(node.getFilhos() != null)			
			for (int i = 0; i < node.getFilhos().size(); i++)
				result += GenerateCodeForNode(node.getFilho(i));
		return result;
	}
	
	//FUNÇÕES DE GERAÇÃO DE CÓDIGO
	public String codeGen(No e) {
		switch (e.getTipo()) {
			case "IDENTIFICADOR":
				return codeGenId(e);
			case "IDENTIFICADOR_PARAM":
				return codeGenIdParam(e);
			case "DEF":
				return codeGenDefFunction(e);
			case "EXP_ARIT":
				return codeGenExpArit(e);
			case "SE":
				return codeGenIfThenElse(e);
			//Casos que não se faz nada
			case "IGUAL":
				return codeGenEmpty();
			case "INTEIRO":
				return codeGenEmpty();
			case "PONTO_E_VIRGULA":
				return codeGenEmpty();
			case "ABRE_PARENTESIS":
				return codeGenEmpty();
			case "VIRGULA":
				return codeGenEmpty();
			case "FECHA_PARENTESIS":
				return codeGenEmpty();
			case "OP_ARIT":
				return codeGenEmpty();
			case "IDENTIFICADOR_FUNCAO_DEF":
				return codeGenEmpty();
			case "ENTAO":
				return codeGenEmpty();
			case "SENAO":
				return codeGenEmpty();
			//Casos não tratados
			default:
				return codeGenDefault(e);
		}
	}
	
	//cgen(IDENTIFICADOR)
	public String codeGenId(No e) {
		//Ex: a = 1;
		if (e.comparaTipoFilho(0, "IGUAL") && e.comparaTipoFilho(1, "INTEIRO") && e.comparaTipoFilho(2, "PONTO_E_VIRGULA")) {
			String id = e.getToken();
			String reg = variaveis.add(id);
			int value = Integer.parseInt(e.getFilho(1).getToken());
			String result = "li " + reg + " " + value + "\n";
			return result;
		}
		else
			return codeGenDefault(e);
	}
	
	//cgen(IDENTIFICADOR_PARAM)
	public String codeGenIdParam(No e) {
		//Ex: f(a);
		String reg = this.variaveis.getReg(e.getToken());
		return "move $a0 " + reg + "\n";
	}
	
	//cgen(def f(x1,...,xn) = e)
	public String codeGenDefFunction(No e) {
		if (e.comparaTipoFilho(0, "IDENTIFICADOR_FUNCAO_DEF")) {
			String functionName = e.getFilho(0).getToken();
			int z = (4 * e.countArgs()) + 8;
			String result = functionName + "_entry:\n" +
					  		"\tmove $fp $sp\n" +
					  		"\tsw $ra 0($sp)\n" + 
					  		"\taddiu $sp $sp -4\n";
			if (e.getFilho(e.getFilhos().size()-2).comparaTipo("EXP"))
				result += codeGen(e.getFilho(e.getFilhos().size()-2));
			if (e.getFilho(e.getFilhos().size()-3).comparaTipo("EXP"))
				result += codeGen(e.getFilho(e.getFilhos().size()-3));
			result += "\tlw $ra 4($sp)\n" +
					  "\taddiu $sp $sp " + z + "\n" +
					  "\tlw $fp 0($sp)\n" +
					  "\tjr $ra\n";
			return result;
		}
		else
			return codeGenDefault(e);
	}
	
	//cgen(EXP_ARIT)
	public String codeGenExpArit(No e) {
		switch (e.getFilho(0).getToken()) {
			case "+":
				return codeGenSum(e, e.getFilho(1));
			case "-":
				return codeGenSubtract(e, e.getFilho(1));
			case "*":
				return codeGenDefault(e);
			case "/":
				return codeGenDefault(e);
			default:
				return codeGenDefault(e);
		}
	}
	
	//cgen(e1 + e2)
	public String codeGenSum(No e1, No e2) {
		String result = codeGenIdParam(e1);
		result += "sw $a0 0($sp)\n" +
				  "addiu $sp $sp - 4\n";
		result += codeGen(e2);
		result += "lw $t1 4($sp)\n" + 
				  "add $a0 $t1 $a0\n" + 
				  "addiu $sp $sp 4\n";
		return result;
	}
	
	//cgen(e1 - e2)
	public String codeGenSubtract(No e1, No e2) {
		String result = codeGenIdParam(e1);
		result += "sw $a0 0($sp)\n" +
				  "addiu $sp $sp - 4\n";
		result += codeGen(e2);
		result += "lw $t1 4($sp)\n" + 
				  "sub $a0 $t1 $a0\n" + 
				  "addiu $sp $sp 4\n";
		return result;
	}
	
	//cgen(if e1 = e2 then e3 else e4)
	public String codeGenIfThenElse(No e) {
		No e1 = e.getFilho(0);
		No e2 = e.getFilho(2);
		No e3 = e.getFilho(4);
		No e4 = e.getFilho(6);
		String ifName = "if" + e1.getToken() + e2.getToken() + "then" + e3.getToken() + "else" + e4.getToken();
		String result = codeGen(e1);
		result += "sw $a0 0($sp)\n" +
				  "addiu $sp $sp - 4\n";
		result += codeGen(e2);
		result += "lw $t1 4($sp)\n" +
				  "addiu $sp $sp 4\n" +
				  "beq $a0 $t1 " + ifName + "_true\n" +
				  ifName + "_false:\n";
		result += "\t" + codeGen(e4);
		result += "\tb " + ifName + "_end_if\n" +
				  ifName + "_true:\n" + 
				  "\t" + codeGen(e3) +
				  ifName + "_end_if:\n";
		return result;
	}
	
	//DEFAULT cgen(e)
	public String codeGenDefault(No e) {
		return "---MIPS implementation for " + e.getToken() + " (" + e.getTipo() + ")---\n";
	}
	
	//EMPTY returns ""
	public String codeGenEmpty() {
		return "";
	}

}