package compiler;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);
		File file = new File("");
		String absolutePath = file.getAbsolutePath();
		int option;
		String menu = 	"\n\n------------------------ COMPILADOR - MENU ------------------------------------------------------\n\n" +
				  		"Escolha uma op��o:\n" + 
				  		"1 - Gerar Classes dos Analisadores L�xico e Sint�tico\n" +
				  		"2 - Compilar (Program.txt no root do projeto)\n" +
				  		"3 - Analise Sem�ntica\n" +
				  		"4 - Gera��o de C�digo\n" +
				  		"5 - Sair\n";
		
		No arvore = null;
		do {
            System.out.println(menu);
            option = in.nextInt();
            switch (option) {
            	//Gerar Classes dos Analisadores L�xico e Sint�tico
            	case 1:
            		//Deletar cria��es antigas
            		System.out.println("Verificando analisadores antigos...");
                    String oldParser = absolutePath + "/src/compiler/Parser.java";
                    File temp = new File(oldParser);
                    if(temp.exists()) {
                    	temp.delete();
                    	System.out.println("Deletou: " + oldParser);
                    }
                    String oldLexer = absolutePath + "/src/compiler/Lexer.java";
                    temp = new File(oldLexer);
                    if(temp.exists()) {
                    	temp.delete();
                    	System.out.println("Deletou: " + oldLexer);
                    }
                    String olderLexer = absolutePath + "/src/compiler/Lexer.java~";
                    temp = new File(olderLexer);
                    if(temp.exists()) {
                    	temp.delete();
                    	System.out.println("Deletou: " + olderLexer);
                    }
                    String oldSym = absolutePath + "/src/compiler/Sym.java";
                    temp = new File(oldSym);
                    if(temp.exists()) {
                    	temp.delete();
                    	System.out.println("Deletou: " + oldSym);
                    }
                    
                    String sintaticFile = "/src/compiler/Parser.cup";
                    String lexicalFile = "/src/compiler/Lexer.lex";
                    String sintaticPath = absolutePath + sintaticFile;
                    String lexicalPath = absolutePath + lexicalFile;
                    String[] argslexic = {lexicalPath};
                    String[] argssintatic = {"-parser", "Parser", "-symbols", "Sym", sintaticPath};
                    
                    try {
                    	System.out.println("\nGerando Analisador L�xico...");
                    	jflex.Main.main(argslexic);
                    	System.out.println("Gerou Analisador L�xico!");
                    } catch (Exception ex) {
                        System.out.println("Erro ao gerar o Analisador L�xico!");
                    }
                    
                    try {
                    	System.out.println("\nGerando Analisador Sint�tico...");
                        java_cup.Main.main(argssintatic);
                        TimeUnit.MILLISECONDS.sleep(500);
                    	System.out.println("Gerou Analisador Sint�tico!");
                    } catch (Exception ex) {
                        System.out.println("Erro ao gerar o Analisador Sint�tico!");
                    }
                    
                    File lexer = new File("Parser.java");
                    File symbols = new File("Sym.java");
                    lexer.renameTo(new File(absolutePath + "/src/compiler/Parser.java"));
                    symbols.renameTo(new File(absolutePath + "/src/compiler/Sym.java"));
                    break;
                
            	case 2:
                    try {
                    	System.out.println("\n----------------- Elementos do Programa -----------------\n");
                    	String sourcecode = absolutePath + "/Program.txt";
                    	Parser p = new Parser(new Lexer(new FileReader(sourcecode)));
                        arvore = (No)p.parse().value;
                        System.out.println("\n\n----------------- �rvore Sint�tica -----------------\n");
                        System.out.println("Compila��o e constru��o da �rvore sint�tica conclu�dos!\n" + 
                        				   "A �rvore sint�tica foi salva no arquivo SintaticTree.txt" + (arvore != null ? arvore : ""));
                        PrintWriter printer = new PrintWriter("SintaticTree.txt");
                		printer.print(arvore.retornaString());
                		printer.close();
                    } catch (Exception e) {
                    	System.out.println("Erro de compila��o!");
                        e.printStackTrace();
                    }
                    break;
                    
            	case 3:
            		if (arvore != null)
            			Semantica.checaSemantica(arvore);            		
            		else
            			System.out.println("Arvore sint�tica n�o constru�da!");
            		break;
                
                //Gera��o de C�digo
            	case 4:
            		if (arvore != null) {
            			CodeGenerator generator = new CodeGenerator((No) arvore);
                		System.out.println("\n----------------- C�digo em MIPS (tambem no arquivo mips.txt) -----------------\n\n" + generator.GenerateCode());
            		}
            		else
            			System.out.println("Arvore sint�tica n�o constru�da!");
            		break;
                   
                //Sair
                case 5:
                	System.out.println("Saindo...\n\n");
                	break;
                
                //Op��o inv�lida
                default: 
                    System.out.println("Op��o inv�lida!\n\n");
                    break;
                
            }
        } while (option != 5);
	}
}
