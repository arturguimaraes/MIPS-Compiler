package compiler;

import java_cup.runtime.*;
import java_cup.*;
import java.util.*;

%%

%{

	private Symbol symbol(int symbol_code, int yyline, int yycolumn) {
		return new Symbol(symbol_code, yyline, yycolumn);
	}
	
	private Symbol symbol(int symbol_code, int yyline, int yycolumn, Object value) {
		return new Symbol(symbol_code, yyline, yycolumn, value);
	}
	
	private Token createToken(String valor) {
	    Token token = new Token(valor);
	    return token;
	}

%}

%cup
%public
%class Lexer
%line
%column

comeca_comentario = "/*"
conteudo_comentario = [^\*\/]*
termina_comentario = "*/"

comentario_em_bloco = {comeca_comentario}{conteudo_comentario}{termina_comentario}
comentario_curto = \|\|.*\n
branco = [\n| |\t|\r|\f]
identificador = [a-z|A-Z][a-z|A-Z|0-9|_]*
inteiro = 0|[1-9][0-9]*

%%

<YYINITIAL> {

	{comentario_em_bloco} 	{ System.out.println("COMENTARIO EM BLOCO"); }
	{comentario_curto} 		{ System.out.println("COMENTARIO CURTO"); }
	"if" 					{ System.out.println("SE"); return new Symbol(Sym.SE, createToken(yytext())); }
	"then"  				{ System.out.println("ENTAO"); return new Symbol(Sym.ENTAO, createToken(yytext())); }
	"else" 					{ System.out.println("SE NAO"); return new Symbol(Sym.SENAO, createToken(yytext())); }
	"def" 					{ System.out.println("DEF"); return new Symbol(Sym.DEF, createToken(yytext())); }
	
	">" 					{ System.out.println(">"); return new Symbol(Sym.MAIOR, createToken(yytext())); }
	"<" 					{ System.out.println("<"); return new Symbol(Sym.MENOR, createToken(yytext())); }
	"=" 					{ System.out.println("="); return new Symbol(Sym.IGUAL, createToken(yytext())); }
	
	"+"  					{ System.out.println("+"); return new Symbol(Sym.SOMA, createToken(yytext())); }
	"-" 					{ System.out.println("-"); return new Symbol(Sym.SUBTRACAO, createToken(yytext())); }
	"*" 					{ System.out.println("*"); return new Symbol(Sym.MULTIPLICACAO, createToken(yytext())); }
	"/" 					{ System.out.println("/"); return new Symbol(Sym.DIVISAO, createToken(yytext())); }
	
	"(" 					{ System.out.println("("); return new Symbol(Sym.ABRE_PARENTESIS, createToken(yytext())); }
	")" 					{ System.out.println(")"); return new Symbol(Sym.FECHA_PARENTESIS, createToken(yytext())); }
	";" 					{ System.out.println("PONTO_E_VIRGULA"); return new Symbol(Sym.PONTO_E_VIRGULA, createToken(yytext())); }
	"," 					{ System.out.println("VIRGULA"); return new Symbol(Sym.VIRGULA, createToken(yytext())); }
	
	{branco} 				{ }
	{identificador}			{ System.out.println("identificador"); return new Symbol(Sym.IDENTIFICADOR, createToken(yytext())); }
	{inteiro} 				{ System.out.println("inteiro"); return new Symbol(Sym.INTEIRO, createToken(yytext())); }

}
. { throw new RuntimeException("Caractere inválido " + yytext() + " na linha " + yyline + ", coluna " +yycolumn); }