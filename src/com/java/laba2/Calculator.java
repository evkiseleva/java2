package com.java.laba2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Calculator {

//    expr : plusminus* EOF ;
//    plusminus: multdiv ( ( '+' | '-' ) multdiv )* ;
//    multdiv : factor ( ( '*' | '/' ) factor )* ;
//    factor : NUMBER | '(' expr ')' ;

    public enum LexemeType {
        leftBracket, rightBracket, addition, subtraction, multiplication, division,
        number, eof;
    }

    public static class Lexeme {
        LexemeType type;
        String value;

        public Lexeme(LexemeType type, String value) {
            this.type = type;
            this.value = value;
        }

        public Lexeme(LexemeType type, Character value) {
            this.type = type;
            this.value = value.toString();
        }

    }

    public static class LexemeBuffer {
        private int pos;

        public List<Lexeme> lexemes;

        public LexemeBuffer(List<Lexeme> lexemes) {
            this.lexemes = lexemes;
        }

        public Lexeme next() {
            return lexemes.get(pos++);
        }

        public void back() {
            pos--;
        }

        public int getPos() {
            return pos;
        }
    }

    public static List<Lexeme> lexAnalyze(String expText) {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int pos = 0;
        while (pos< expText.length()) {
            char c = expText.charAt(pos);
            switch (c) {
                case '(':
                    lexemes.add(new Lexeme(LexemeType.leftBracket, c));
                    pos++;
                    //continue;
                    break;
                case ')':
                    lexemes.add(new Lexeme(LexemeType.rightBracket, c));
                    pos++;
                    //continue;
                    break;
                case '+':
                    lexemes.add(new Lexeme(LexemeType.addition, c));
                    pos++;
                    //continue;
                    break;
                case '-':
                    lexemes.add(new Lexeme(LexemeType.subtraction, c));
                    pos++;
                    //continue;
                    break;
                case '*':
                    lexemes.add(new Lexeme(LexemeType.multiplication, c));
                    pos++;
                    //continue;
                    break;
                case '/':
                    lexemes.add(new Lexeme(LexemeType.division, c));
                    pos++;
                    //continue;
                    break;
                default:
                    if (c <= '9' && c >= '0') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(c);
                            pos++;
                            if (pos >= expText.length()) {
                                break;
                            }
                            c = expText.charAt(pos);
                        } while (c <= '9' && c >= '0');
                        lexemes.add(new Lexeme(LexemeType.number, sb.toString()));
                    } else {
                        if (c != ' ') {
                            throw new RuntimeException("Unexpected character: " + c);
                        }
                        pos++;
                    }
            }
        }
        lexemes.add(new Lexeme(LexemeType.eof, ""));
        return lexemes;
    }

    public static int factor(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        switch (lexeme.type) {
            case number:
                return Integer.parseInt(lexeme.value);
            case leftBracket:
                int value = addSub(lexemes);
                lexeme = lexemes.next();
                if (lexeme.type != LexemeType.rightBracket) {
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + lexemes.getPos());
                }
                return value;
            default:
                throw new RuntimeException("Unexpected token: " + lexeme.value
                        + " at position: " + lexemes.getPos());
        }
    }

    public static int multDiv(LexemeBuffer lexemes) {
        int value = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case multiplication:
                    value *= factor(lexemes);
                    break;
                case division:
                    value /= factor(lexemes);
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    public static int addSub(LexemeBuffer lexemes) {
        int value = multDiv(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case addition:
                    value += multDiv(lexemes);
                    break;
                case subtraction:
                    value -= multDiv(lexemes);
                    break;
                default:
                    lexemes.back();
                    return value;
            }
        }
    }

    public static int expr(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.eof) {
            return 0;
        } else {
            lexemes.back();
            return addSub(lexemes);
        }
    }








    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String expressionText = "(2 * c / d) * 10 - c * 2"; // + (b * 2 - a) * ((1-d)/4)";
        for (int i = 0; i < expressionText.length(); i++)
        {
            int value;
            char symbol = expressionText.charAt(i);
            if(symbol >= 'a' && symbol <= 'z')
            {
                System.out.println("Введите значение переменной "+symbol+ " : " );
                value = in.nextInt();
                if (value<0)
                {
                    int j=i-1;
                    while (expressionText.charAt(j) == ' ' && j>=0 )
                    {
                        j--;
                    }
                    switch(expressionText.charAt(j)){
                        case '-':
                            expressionText = expressionText.substring(0, j) + "+" + expressionText.substring(j+1);
                            expressionText = expressionText.substring(0, i) + Integer. toString(-value) + expressionText.substring(i+1);
                            break;
                        case '+':
                            expressionText = expressionText.substring(0, j) + "-" + expressionText.substring(j+1);
                            expressionText = expressionText.substring(0, i) + Integer. toString(-value) + expressionText.substring(i+1);
                            break;
                        default:
                            expressionText = expressionText.substring(0, i) + "(0-"+ Integer. toString(-value) + ")" + expressionText.substring(i+1);
                            i=i+4;
                    }

                }
                else {
                    expressionText = expressionText.substring(0, i) + Integer.toString(value) + expressionText.substring(i + 1);
                }
                int index = expressionText.indexOf(symbol);
                while ( index !=-1)
                {
                    if (value<0)
                    {
                        int j=index-1;
                        while (expressionText.charAt(j) == ' ' && j>=0 )
                        {
                            j--;
                        }
                        switch(expressionText.charAt(j)){
                            case '-':
                                expressionText = expressionText.substring(0, j) + "+" + expressionText.substring(j+1);
                                expressionText = expressionText.substring(0, index) + Integer. toString(-value) + expressionText.substring(index+1);
                                break;
                            case '+':
                                expressionText = expressionText.substring(0, j) + "-" + expressionText.substring(j+1);
                                expressionText = expressionText.substring(0, index) + Integer. toString(-value) + expressionText.substring(index+1);
                                break;
                            default:
                                expressionText = expressionText.substring(0, index) + "(0-"+ Integer. toString(-value) + ")" + expressionText.substring(index+1);
                        }

                    }
                    else
                    {
                        expressionText = expressionText.substring(0, index) + Integer. toString(value) + expressionText.substring(index+1);
                    }
                    index = expressionText.indexOf(symbol);
                }
            }
        }
        System.out.println(expressionText );
        List<Lexeme> lexemes = lexAnalyze(expressionText);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
        System.out.println(expr(lexemeBuffer));
    }

}

