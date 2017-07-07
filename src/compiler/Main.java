package compiler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		File file = new File("");
		String absolutePath = file.getAbsolutePath();
		int option;
		String menu = 	"\n\n------------------------ COMPILADOR - MENU ------------------------------------------------------\n\n" +
				  		"Escolha uma opção:\n" + 
				  		"1 - Gerar Classes dos Analisadores Léxico e Sintático\n" +
				  		"2 - Compilar\n" +
				  		"9 - Sair\n";
		
		do {
            System.out.println(menu);
            option = in.nextInt();
            switch (option) {
            	//Gerar Classes dos Analisadores Léxico e Sintático
            	case 1:
            		//Deletar criações antigas
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
                    	System.out.println("\nGerando Analisador Léxico...");
                    	jflex.Main.main(argslexic);
                    	System.out.println("Gerou Analisador Léxico!");
                    } catch (Exception ex) {
                        System.out.println("Erro ao gerar o Analisador Léxico!");
                    }
                    
                    try {
                    	System.out.println("\nGerando Analisador Sintático...");
                        java_cup.Main.main(argssintatic);
                        TimeUnit.MILLISECONDS.sleep(500);
                    	System.out.println("Gerou Analisador Sintático!");
                    } catch (Exception ex) {
                        System.out.println("Erro ao gerar o Analisador Sintático!");
                    }
                    
                    File lexer = new File("Parser.java");
                    File symbols = new File("Sym.java");
                    lexer.renameTo(new File(absolutePath + "/src/compiler/Parser.java"));
                    symbols.renameTo(new File(absolutePath + "/src/compiler/Sym.java"));
                    break;
                
            	case 2:
                    String sourcecode = absolutePath + "/src/compiler/Program.pg";

                    try {
                        Parser p = new Parser(new Lexer(new FileReader(sourcecode)));
                        Object result = p.parse().value;
                        System.out.println("Compilação concluída!\n" + (result != null ? result : ""));
                    } catch (Exception e) {
                    	System.out.println("Erro de compilação!");
                        e.printStackTrace();
                    }
                    break;
                   
                //Sair
                case 9:
                	System.out.println("Saindo...\n\n");
                	break;
                
                //Opção inválida
                default: 
                    System.out.println("Opção inválida!\n\n");
                    break;
                
            }
        } while (option != 9);
	}
}
