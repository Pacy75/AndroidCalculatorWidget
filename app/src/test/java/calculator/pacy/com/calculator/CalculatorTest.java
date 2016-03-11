package calculator.pacy.com.calculator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorTest {
    Calculator calculator;

    @Before
    public void initialize() {
        calculator = new Calculator(MyBigDecimal.ZERO, null, "", MyBigDecimal.ZERO, null, 0);
        calculator.input(CalKey.AC);
    }

    @Test
    public void testInput0() throws Exception {
        assertEquals("0", calculator.getDisplayValue());
        calculator.input(CalKey.NUMBER_0);
        assertEquals("0", calculator.getDisplayValue());
        calculator.input(CalKey.NUMBER_1);
        assertEquals("1", calculator.getDisplayValue());
        calculator.input(CalKey.DEL);
        assertEquals("0", calculator.getDisplayValue());
    }

    @Test
    public void testInputDot() throws Exception {
        calculator.input(CalKey.DOT);
        assertEquals("0.", calculator.getDisplayValue());
        calculator.input(CalKey.DOT);
        assertEquals("0.", calculator.getDisplayValue());
    }

    @Test
    public void testInputMaxLength() throws Exception {
        calculator.input(CalKey.NUMBER_1);
        calculator.input(CalKey.NUMBER_2);
        calculator.input(CalKey.NUMBER_3);
        calculator.input(CalKey.NUMBER_4);
        calculator.input(CalKey.NUMBER_5);
        calculator.input(CalKey.NUMBER_6);
        calculator.input(CalKey.NUMBER_7);
        calculator.input(CalKey.NUMBER_8);
        calculator.input(CalKey.NUMBER_9);
        calculator.input(CalKey.NUMBER_0);
        calculator.input(CalKey.NUMBER_1);
        calculator.input(CalKey.NUMBER_2);
        calculator.input(CalKey.NUMBER_3);
        calculator.input(CalKey.NUMBER_4);
        calculator.input(CalKey.NUMBER_5);
        assertEquals("123456789012345", calculator.getDisplayValue());
        calculator.input(CalKey.NUMBER_0);
        assertEquals("123456789012345", calculator.getDisplayValue());
        calculator.input(CalKey.DEL);
        calculator.input(CalKey.NUMBER_0);
        assertEquals("123456789012340", calculator.getDisplayValue());
    }

    @Test
    public void testAdd() throws Exception {
        calculator.input(CalKey.NUMBER_1);
        assertEquals("1", calculator.getDisplayValue());
        assertEquals("", calculator.getDisplayOperator());
        calculator.input(CalKey.ADD);
        assertEquals("1", calculator.getDisplayValue());
        assertEquals("+", calculator.getDisplayOperator());
        calculator.input(CalKey.NUMBER_1);
        assertEquals("1", calculator.getDisplayValue());
        assertEquals("+", calculator.getDisplayOperator());
        calculator.input(CalKey.EQUAL);
        assertEquals("2", calculator.getDisplayValue());
        assertEquals("", calculator.getDisplayOperator());
    }

    @Test
    public void testSub() throws Exception {
        calculator.input(CalKey.NUMBER_2);
        calculator.input(CalKey.SUB);
        assertEquals("2", calculator.getDisplayValue());
        assertEquals("−", calculator.getDisplayOperator());
        calculator.input(CalKey.NUMBER_3);
        calculator.input(CalKey.EQUAL);
        assertEquals("-1", calculator.getDisplayValue());
        assertEquals("", calculator.getDisplayOperator());
    }

    @Test
    public void testMul() throws Exception {
        calculator.input(CalKey.NUMBER_2);
        calculator.input(CalKey.DOT);
        calculator.input(CalKey.NUMBER_4);
        calculator.input(CalKey.MUL);
        assertEquals("2.4", calculator.getDisplayValue());
        assertEquals("×", calculator.getDisplayOperator());
        calculator.input(CalKey.NUMBER_3);
        calculator.input(CalKey.DOT);
        calculator.input(CalKey.NUMBER_3);
        calculator.input(CalKey.EQUAL);
        assertEquals("7.92", calculator.getDisplayValue());
        assertEquals("", calculator.getDisplayOperator());
    }

    @Test
    public void testDiv() throws Exception {
        calculator.input(CalKey.NUMBER_1);
        calculator.input(CalKey.DIV);
        assertEquals("1", calculator.getDisplayValue());
        assertEquals("÷", calculator.getDisplayOperator());
        calculator.input(CalKey.NUMBER_3);
        calculator.input(CalKey.EQUAL);
        assertEquals("0.3333333333333333", calculator.getDisplayValue());
        assertEquals("", calculator.getDisplayOperator());


        calculator.input(CalKey.AC);
        calculator.input(CalKey.NUMBER_9);
        calculator.input(CalKey.DOT);
        calculator.input(CalKey.NUMBER_6);
        calculator.input(CalKey.DIV);
        assertEquals("9.6", calculator.getDisplayValue());
        assertEquals("÷", calculator.getDisplayOperator());
        calculator.input(CalKey.NUMBER_3);
        calculator.input(CalKey.EQUAL);
        assertEquals("3.2", calculator.getDisplayValue());
    }

    @Test
    public void testInfinity() throws Exception {
        calculator.input(CalKey.NUMBER_1);
        calculator.input(CalKey.DIV);
        calculator.input(CalKey.NUMBER_0);
        calculator.input(CalKey.EQUAL);
        assertEquals("Infinity", calculator.getDisplayValue());
        calculator.input(CalKey.DEL);
        assertEquals("Infinity", calculator.getDisplayValue());
    }

    @Test
    public void testNaN() throws Exception {
        calculator.input(CalKey.NUMBER_0);
        calculator.input(CalKey.DIV);
        calculator.input(CalKey.NUMBER_0);
        calculator.input(CalKey.EQUAL);
        assertEquals("NaN", calculator.getDisplayValue());
        calculator.input(CalKey.DEL);
        assertEquals("NaN", calculator.getDisplayValue());
    }

    @Test
    public void testIncompleteEqual() throws Exception {
        calculator.input(CalKey.NUMBER_2);
        calculator.input(CalKey.ADD);
        calculator.input(CalKey.EQUAL);
        assertEquals("4", calculator.getDisplayValue());
        assertEquals("", calculator.getDisplayOperator());

        calculator.input(CalKey.AC);
        calculator.input(CalKey.NUMBER_2);
        calculator.input(CalKey.SUB);
        calculator.input(CalKey.EQUAL);
        assertEquals("0", calculator.getDisplayValue());
        assertEquals("", calculator.getDisplayOperator());

        calculator.input(CalKey.AC);
        calculator.input(CalKey.NUMBER_2);
        calculator.input(CalKey.MUL);
        calculator.input(CalKey.EQUAL);
        assertEquals("4", calculator.getDisplayValue());
        assertEquals("", calculator.getDisplayOperator());

        calculator.input(CalKey.AC);
        calculator.input(CalKey.NUMBER_2);
        calculator.input(CalKey.DIV);
        calculator.input(CalKey.EQUAL);
        assertEquals("1", calculator.getDisplayValue());
        assertEquals("", calculator.getDisplayOperator());
    }
}