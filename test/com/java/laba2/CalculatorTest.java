package com.java.laba2;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @org.junit.jupiter.api.Test
    void lexAnalyzeTest() {
    }

    @org.junit.jupiter.api.Test
    void factorTest() {
        String expressionText = "2";
        List<Calculator.Lexeme> lexemes = Calculator.lexAnalyze(expressionText);
        Calculator.LexemeBuffer lexemeBuffer = new Calculator.LexemeBuffer(lexemes);
        assertEquals(2, Calculator.factor(lexemeBuffer));
    }

    @org.junit.jupiter.api.Test
    void multDivTest() {
        String expressionText = "2 * 3";
        List<Calculator.Lexeme> lexemes = Calculator.lexAnalyze(expressionText);
        Calculator.LexemeBuffer lexemeBuffer = new Calculator.LexemeBuffer(lexemes);
        assertEquals(6, Calculator.multDiv(lexemeBuffer));
    }

    @org.junit.jupiter.api.Test
    void addSubTest() {
        String expressionText = "2 + 3";
        List<Calculator.Lexeme> lexemes = Calculator.lexAnalyze(expressionText);
        Calculator.LexemeBuffer lexemeBuffer = new Calculator.LexemeBuffer(lexemes);
        assertEquals(5, Calculator.addSub(lexemeBuffer));
    }

    @org.junit.jupiter.api.Test
    void exprTest() {
        String expressionText = "2 +(23-11)*(18+2)-6/3";
        List<Calculator.Lexeme> lexemes = Calculator.lexAnalyze(expressionText);
        Calculator.LexemeBuffer lexemeBuffer = new Calculator.LexemeBuffer(lexemes);
        assertEquals(240, Calculator.expr(lexemeBuffer));
    }

    @org.junit.jupiter.api.Test
    void checkAndChangeTest() {
        String expressionText = "2 +(23-11)*(18+2)-6/3";
        expressionText= Calculator.checkAndChange(expressionText);
        assertEquals("2 +(23-11)*(18+2)-6/3",expressionText);
    }


}