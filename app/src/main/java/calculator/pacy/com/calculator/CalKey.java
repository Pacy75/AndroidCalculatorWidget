package calculator.pacy.com.calculator;

/**
 * Created by pacy on 2016/3/8.
 */
public enum CalKey {
    NUMBER_1(CalculatorWidget.NUMBER_1, CalKeyType.KEY_NUMBER, '1'),
    NUMBER_2(CalculatorWidget.NUMBER_2, CalKeyType.KEY_NUMBER, '2'),
    NUMBER_3(CalculatorWidget.NUMBER_3, CalKeyType.KEY_NUMBER, '3'),
    NUMBER_4(CalculatorWidget.NUMBER_4, CalKeyType.KEY_NUMBER, '4'),
    NUMBER_5(CalculatorWidget.NUMBER_5, CalKeyType.KEY_NUMBER, '5'),
    NUMBER_6(CalculatorWidget.NUMBER_6, CalKeyType.KEY_NUMBER, '6'),
    NUMBER_7(CalculatorWidget.NUMBER_7, CalKeyType.KEY_NUMBER, '7'),
    NUMBER_8(CalculatorWidget.NUMBER_8, CalKeyType.KEY_NUMBER, '8'),
    NUMBER_9(CalculatorWidget.NUMBER_9, CalKeyType.KEY_NUMBER, '9'),
    NUMBER_0(CalculatorWidget.NUMBER_0, CalKeyType.KEY_NUMBER, '0'),
    ADD(CalculatorWidget.ADD, CalKeyType.KEY_OPERATOR, '+'),
    SUB(CalculatorWidget.SUB, CalKeyType.KEY_OPERATOR, '\u2212'),
    MUL(CalculatorWidget.MUL, CalKeyType.KEY_OPERATOR, '\u00d7'),
    DIV(CalculatorWidget.DIV, CalKeyType.KEY_OPERATOR, '\u00f7'),
    DOT(CalculatorWidget.DOT, CalKeyType.KEY_NUMBER, '.'),
    DEL(CalculatorWidget.DEL, CalKeyType.KEY_OPERATOR, '<'),
    EQUAL(CalculatorWidget.EQUALS, CalKeyType.KEY_OPERATOR, '='),
    AC(CalculatorWidget.AC, CalKeyType.KEY_OPERATOR, ' ');

    private final String action_;
    private final CalKeyType type_;
    private char ch_;

    CalKey(String action, CalKeyType type, char c) {
        action_ = action;
        type_ = type;
        ch_ = c;
    }

    public String getAction() {
        return action_;
    }

    public CalKeyType getType() {
        return type_;
    }

    public char getChar() {
        return ch_;
    }

    public static CalKey getByAction(String action) {
        for (CalKey e : values()) {
            if (e.getAction().equals(action)) {
                return e;
            }
        }
        return null;
    }
}
