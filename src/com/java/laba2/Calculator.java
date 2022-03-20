package com.java.laba2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Calculator {

//    expr : plusminus* EOF ;
//    plusminus: multdiv ( ( '+' | '-' ) multdiv )* ;
//    multdiv : factor ( ( '*' | '/' ) factor )* ;
//    factor : NUMBER | '(' expr ')' ;
    /**
     * Объединение, определяющее тип лексемы
     * */
    public enum LexemeType {
        leftBracket, rightBracket, addition, subtraction, multiplication, division,
        number, eof;
    }

    /**
     * Класс Лексема
     * */
    public static class Lexeme {
        /**
         * Поле, определяющее тип лексемы
         * */
        LexemeType type;
        /**
         * Поле, определяющее значение лексемы
         * */
        String value;
        /**
         * Конструктор
         * @param type - тип лексемы
         * @param value - значение лексемы
         * */
        public Lexeme(LexemeType type, String value) {
            this.type = type;
            this.value = value;
        }

        /**
         *Конструктор
         * @param type - тип лексемы
         * @param value - значение лексемы
         * */
        public Lexeme(LexemeType type, Character value) {
            this.type = type;
            this.value = value.toString();
        }

    }

    /**
     * Класс Буффер лексем
     * */
    public static class LexemeBuffer {
        /**
         * Позиция
         * */
        private int pos;

        /**
         * Список лексем
         * */
        public List<Lexeme> lexemes;

        /**
         * Конструктор
         * @param lexemes  - Список лексем
         * */
        public LexemeBuffer(List<Lexeme> lexemes) {
            this.lexemes = lexemes;
        }

        /**
         * Метод, возвращающий следующую лексему
         * @return следующая в списке лексема
         * */
        public Lexeme next() {
            return lexemes.get(pos++);
        }

        /**
         * Метод, сдвигающий на 1 позицию назад
         * */
        public void back() {
            pos--;
        }
        /**
         * Метод, возвращающий позицию
         * @return  позиция в буффере
         * */
        public int getPos() {
            return pos;
        }
    }

    /**
     * Метод, анализирующий выражение и возвращающий список лексем
     * @param expText - математическое выражение
     * @return список лексем
     * */
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

    /**
     * Метод, возвращающий значение или ошибку, если данное выражение некорректное
     * @param lexemes буффер лексем
     * @return число
     * */
    public static int factor(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        switch (lexeme.type) {
            case number:
                return Integer.parseInt(lexeme.value);
            case leftBracket:
                int value = addSub(lexemes);
                lexeme = lexemes.next();
                if (lexeme.type != LexemeType.rightBracket) {
                    throw new RuntimeException("Ошибка в выражении");
                }
                return value;
            default:
                throw new RuntimeException("Ошибка в выражении");
        }
    }

    /**
     * Метод, возвращающий значение произведения или деления
     * @param lexemes буффер лексем
     * @return результат произведения или деления
     * */
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

    /**
     * Метод, возвращающий значение суммы или разности
     * @param lexemes буффер лексем
     * @return результат суммы/разности
     * */
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

    /**
     * Метод, возвращающий значение выражения
     * @param lexemes буффер лексем
     * @return результат выражения
     * */
    public static int expr(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.eof) {
            return 0;
        } else {
            lexemes.back();
            return addSub(lexemes);
        }
    }


    public static String checkAndChange(String expressionText){
        Scanner in = new Scanner(System.in);
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
        return expressionText;
    }
    /**
     * Стартовая точка
     * @param args аргументы
     * */
    public static void main(String[] args) {
        String expressionText = "(2 * c / d) * 10 - c "; //* 2 + (b * 2 - a) * ((1-d)/4)";
        expressionText = checkAndChange(expressionText);
        System.out.println(expressionText );
        List<Lexeme> lexemes = lexAnalyze(expressionText);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
        System.out.println(expr(lexemeBuffer));
    }

}

