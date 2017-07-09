package compiler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class CodeGenerator {
	No tree;
	
	public CodeGenerator (No tree) throws IOException {
		this.tree = tree;
	}
	
	public String GenerateCode() throws FileNotFoundException {
		String result = GenerateCodeForNode(this.tree);
		PrintWriter printer = new PrintWriter("mips.txt");
		printer.print(result);
		printer.close();
		return result;
	}
	
	public String GenerateCodeForNode(No node) {
		String result = "";
		
		if(node.getFilhos() != null) {
		
			//Soma
			if (compareToken(node.getFilho(0), "+")) {
				result += codeGenSum(node, node.getFilho(1));
			}
			
			//Subtração
			if (compareToken(node.getFilho(0), "-")) {
				result += codeGenSubtract(node, node.getFilho(1));
			}
			
			//If
			if (compareToken(node, "if")) {
				result += codeGenIfThenElse(node.getFilho(0), node.getFilho(2), node.getFilho(4), node.getFilho(6));
			}
			
			if(compareToken(node, "def")) {
				int numberOfArgs = countArgs(node);
				result += codeGenDefFunction(node.getFilho(0), numberOfArgs, node);
			}
			
			//Gera código para todos os filhos
			for (int i = 0; i < node.getFilhos().size(); i++) {
				result += GenerateCodeForNode(node.getFilho(i));
			}
			
		}

		return result;
	}
	
	public Boolean compareToken(No node, String sentence) {
		return node.getToken().contains(sentence);
	}
	
	public int countArgs(No node) {
		if(compareToken(node.getFilho(0), "(")) {
			return recursiveCountArgs(node.getFilho(1));
		}
		if(compareToken(node.getFilho(1), "(")) {
			return recursiveCountArgs(node.getFilho(2));
		}
		return 0;
	}
	
	public int recursiveCountArgs(No node) {
		if(!compareToken(node, ",") && !compareToken(node, "(") && !compareToken(node, ")")) {
			if(node.getFilhos()!= null && compareToken(node.getFilho(0), ","))
				return 1 + recursiveCountArgs(node.getFilho(1));
			else
				return 1;
		}
		return 0;
	}
	
	//FUNÇÕES DE GERAÇÃO DE CÓDIGO cgen()

	//cgen(e)
	public String codeGenConstant(No e) {
		String result = "";

		result += "li $a0 " + e.getToken() + "\n";
		
		return result;
	}
	
	//cgen(e1 + e2)
	public String codeGenSum(No e1, No e2) {
		String result = "";

		result += codeGenConstant(e1);
		result += "sw $a0 0($sp)\n" +
				  "addiu $sp $sp - 4\n";
		result += codeGenConstant(e2);
		result += "lw $t1 4($sp)\n" + 
				  "add $a0 $t1 $a0\n" + 
				  "addiu $sp $sp 4\n";
		
		return result;
	}
	
	//cgen(e1 - e2)
	public String codeGenSubtract(No e1, No e2) {
		String result = "";

		result += codeGenConstant(e1);
		result += "sw $a0 0($sp)\n" +
				  "addiu $sp $sp - 4\n";
		result += codeGenConstant(e2);
		result += "lw $t1 4($sp)\n" + 
				  "sub $a0 $t1 $a0\n" + 
				  "addiu $sp $sp 4\n";
		
		return result;
	}
	
	//cgen(if e1 = e2 then e3 else e4)
	public String codeGenIfThenElse(No e1, No e2, No e3, No e4) {
		String ifName = "if" + e1.getToken() + e2.getToken() + "then" + e3.getToken() + "else" + e4.getToken();
		String result = "";
		
		result += codeGenConstant(e1);
		result += "sw $a0 0($sp)\n" +
				  "addiu $sp $sp - 4\n";
		result += codeGenConstant(e2);
		result += "lw $t1 4($sp)\n" +
				  "addiu $sp $sp 4\n" +
				  "beq $a0 $t1 " + ifName + "_true\n" +
				  ifName + "_false:\n";
		result += "\t" + codeGenConstant(e4);
		result += "\tb " + ifName + "_end_if\n" +
				  ifName + "_true:\n" + 
				  "\t" + codeGenConstant(e3) +
				  ifName + "_end_if:\n";
		
		return result;
	}
	
	//cgen(def f(x1,...,xn) = e)
	public String codeGenDefFunction(No functionNameNode, int numberOfArgs, No e) {
		int z = (4 * numberOfArgs) + 8;
		String result = "";

		result += functionNameNode.getToken() + "_entry:\n" +
				  "\tmove $fp $sp\n" +
				  "\tsw $ra 0($sp)\n" + 
				  "\taddiu $sp $sp -4\n";
		result += codeGenConstant(e);
		result += "\tlw $ra 4($sp)\n" +
				  "\taddiu $sp $sp " + z + "\n" +
				  "\tlw $fp 0($sp)\n" +
				  "\tjr $ra\n";
		
		return result;
	}

}
