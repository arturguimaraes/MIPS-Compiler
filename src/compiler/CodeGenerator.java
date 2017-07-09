package compiler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CodeGenerator {
	No tree;
	
	public CodeGenerator (No tree) throws IOException {
		/*InputStream in = new FileInputStream(programPath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = reader.readLine();
		StringBuilder builder = new StringBuilder();
		while(line != null) {
			builder.append(line).append("\n");
			line = reader.readLine();
		}
		String fileAsString = builder.toString();
		reader.close();
		this.program = fileAsString;*/
		this.tree = tree;
	}
	
	public void GenerateCode() {
		String result = GenerateCodeForNode(this.tree);
		
		//String sum = codeGenSum("1","2");
		//String sub = codeGenSubtract("1","2");
		//String result = codeGenIfThenElse(sum, sub, "true", "false");
		
		System.out.println(result);
	}
	
	public Boolean compareToken(No node, String sentence) {
		return node.getToken().contains(sentence);
	}
	
	public String GenerateCodeForNode(No node) {
		String result = "";
		
		if(node.getFilhos() != null) {
		
			//Soma
			if (compareToken(node.getFilho(0), "+")) {
				result += codeGenSum(node.getToken(), node.getFilho(1).getToken());
			}
			
			//Subtração
			if (compareToken(node.getFilho(0), "-")) {
				result += codeGenSubtract(node.getToken(), node.getFilho(1).getToken());
			}
			
			//If
			if (compareToken(node, "if")) {
				result += codeGenIfThenElse(node.getFilho(0).getToken(), node.getFilho(2).getToken(), node.getFilho(4).getToken(), node.getFilho(6).getToken());
			}
			
			//Gera código para todos os filhos
			for (int i = 0; i < node.getFilhos().size(); i++) {
				result += GenerateCodeForNode(node.getFilho(i));
			}
			
		}

		return result;
	}
	
	public String codeGenConstant(String e) {
		String result = "";

		result += "li $a0 " + e + "\n";
		
		return result;
	}
	
	public String codeGenSum(String e1, String e2) {
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
	
	public String codeGenSubtract(String e1, String e2) {
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
	
	public String codeGenIfThenElse(String e1, String e2, String e3, String e4) {
		String result = "";

		result += codeGenConstant(e1);
		result += "sw $a0 0($sp)\n" +
				  "addiu $sp $sp - 4\n";
		result += codeGenConstant(e2);
		result += "lw $t1 4($sp)\n" +
				  "addiu $sp $sp 4\n" +
				  "beq $a0 $t1 true_branch\n" +
				  "false_branch:\n";
		result += "\t" + codeGenConstant(e4);
		result += "\tb end_if\n" +
				  "true_branch:\n" + 
				  "\t" + codeGenConstant(e3) +
				  "end_if:\n";
		
		return result;
	}

}
