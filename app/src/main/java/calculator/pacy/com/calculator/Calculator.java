package calculator.pacy.com.calculator;

import android.content.Context;

/**
 * Created by pacy on 2016/3/8.
 */
public class Calculator {
    public static final int INPUT_MAX_LENGTH = 15;
    public static final int RESP_OK = 0;
    public static final int RESP_ERROR_INPUT_TOLONG = -1;

    private static final int STATE_INIT = 0;
    private static final int STATE_STACK_ONE = 1;
    private static final int STATE_STACK_TWO = 2;
    private static final int STATE_RESULT = 3;

    private MyBigDecimal prevValue_;
    private CalKey prevOp_;
    private String displayStr_;
    private MyBigDecimal currValue_;
    private CalKey currOp_;
    private int state_;

    public static Calculator getInstance(Context context, int appwidgetId) {
        MyBigDecimal prevValue = CalPreference.getPrevValue(context, appwidgetId);
        CalKey prevOp = CalPreference.getPrevOp(context, appwidgetId);
        String displayStr = CalPreference.getDisplayStr(context, appwidgetId);
        MyBigDecimal currValue = CalPreference.getCurrValue(context, appwidgetId);
        CalKey currOp = CalPreference.getCurrOp(context, appwidgetId);
        int state = CalPreference.getStateCode(context, appwidgetId);
        return new Calculator(prevValue, prevOp, displayStr, currValue, currOp, state);
    }

    public static void saveInstance(Context context, int appwidgetId, Calculator calculator) {
        CalPreference.setPrevValue(context, appwidgetId, calculator.getPrevValue());
        CalPreference.setPrevOp(context, appwidgetId, calculator.getPrevOp());
        CalPreference.setDisplayStr(context, appwidgetId, calculator.getDisplayStr());
        CalPreference.setCurrValue(context, appwidgetId, calculator.getCurrValue());
        CalPreference.setCurrOp(context, appwidgetId, calculator.getCurrOp());
        CalPreference.setStateCode(context, appwidgetId, calculator.getState());
    }

    public Calculator(MyBigDecimal prevValue, CalKey prevOp, String displayStr, MyBigDecimal currValue,
                      CalKey currOp, int state) {
        prevValue_ = prevValue;
        prevOp_ = prevOp;
        displayStr_ = displayStr;
        currValue_ = currValue;
        currOp_ = currOp;
        state_ = state;
    }

    public String getDisplayValue() {
        if (displayStr_.length() > 0) {
            return displayStr_;
        } else if (state_ == STATE_STACK_TWO) {
            return currValue_.toString();
        } else {
            return prevValue_.toString();
        }
    }

    public String getDisplayOperator() {
        if (state_ == STATE_INIT || state_ == STATE_RESULT) {
            return "";
        } else if (state_ == STATE_STACK_ONE) {
            return Character.toString(prevOp_.getChar());
        } else if (state_ == STATE_STACK_TWO) {
            return Character.toString(currOp_.getChar());
        }
        return "";
    }

    public int input(CalKey c) {
        if (CalKey.AC.equals(c)) {
            clear();
        } else if (CalKey.EQUAL.equals(c)) {
            getResult();
        } else if (CalKey.DEL.equals(c)) {
            if (getDisplayValue().equals("Infinity") || getDisplayValue().equals("NaN")) {
                return RESP_OK;
            }
            if (state_ == STATE_RESULT) {
                displayStr_ = prevValue_.toString();
                prevValue_ = MyBigDecimal.ZERO;
                state_ = STATE_INIT;
            }
            if (displayStr_.length() > 0) {
                displayStr_ = displayStr_.substring(0, displayStr_.length() - 1);
            }
        } else if (CalKeyType.KEY_NUMBER.equals(c.getType())) {
            return inputNumber(c);
        } else if (CalKeyType.KEY_OPERATOR.equals(c.getType())) {
            inputOp(c);
        }
        return RESP_OK;
    }

    private void getResult() {
        switch (state_) {
            case STATE_INIT:
                prevValue_ = formatString(displayStr_);
                displayStr_ = "";
                break;
            case STATE_STACK_ONE:
                if (0 == displayStr_.length()) {
                    prevValue_ = calculate(prevValue_, prevValue_, prevOp_);
                } else {
                    prevValue_ = calculate(prevValue_, formatString(displayStr_), prevOp_);
                }
                displayStr_ = "";
                break;
            case STATE_STACK_TWO:
                if (0 == displayStr_.length()) {
                    currValue_ = calculate(currValue_, currValue_, currOp_);
                } else {
                    currValue_ = calculate(currValue_, formatString(displayStr_), currOp_);
                }
                prevValue_ = calculate(prevValue_, currValue_, prevOp_);
                displayStr_ = "";
                break;
        }
        prevOp_ = null;
        currValue_ = MyBigDecimal.ZERO;
        currOp_ = null;
        state_ = STATE_RESULT;
    }

    private int inputNumber(CalKey c) {
        if (STATE_RESULT == state_) {
            prevValue_ = MyBigDecimal.ZERO;
            displayStr_ = "";
            state_ = STATE_INIT;
        }
        if (CalKey.DOT.equals(c)) {
            if (displayStr_.contains(".")) {
                return RESP_OK;
            }
            if (0 == displayStr_.length()) {
                displayStr_ = "0";
            }
        }
        if (displayStr_.length() == 1 && displayStr_.contains("0")) {
            if (CalKey.NUMBER_0.equals(c)) {
                return RESP_OK;
            } else if (!CalKey.DOT.equals(c)) {
                displayStr_ = "";
            }
        }
        if (displayStr_.length() >= INPUT_MAX_LENGTH) {
            return RESP_ERROR_INPUT_TOLONG;
        }
        displayStr_ = displayStr_.concat(Character.toString(c.getChar()));
        return RESP_OK;
    }

    private void inputOp(CalKey c) {
        switch (state_) {
            case STATE_INIT:
                if (displayStr_.length() == 0) {
                    prevValue_ = MyBigDecimal.ZERO;
                } else {
                    prevValue_ = formatString(displayStr_);
                }
                prevOp_ = c;
                displayStr_ = "";
                state_ = STATE_STACK_ONE;
                break;
            case STATE_STACK_ONE:
                if (displayStr_.length() == 0) {
                    prevOp_ = c;
                } else if ((CalKey.ADD.equals(prevOp_) || CalKey.SUB.equals(prevOp_)) &&
                        (CalKey.MUL.equals(c) || CalKey.DIV.equals(c))) {
                    currValue_ = formatString(displayStr_);
                    currOp_ = c;
                    displayStr_ = "";
                    state_ = STATE_STACK_TWO;
                } else {
                    prevValue_ = calculate(prevValue_, formatString(displayStr_), prevOp_);
                    prevOp_ = c;
                    displayStr_ = "";
                }
                break;
            case STATE_STACK_TWO:
                if (displayStr_.length() == 0) {
                    currOp_ = c;
                } else if (CalKey.MUL.equals(c) || CalKey.DIV.equals(c)) {
                    currValue_ = calculate(currValue_, formatString(displayStr_), currOp_);
                    currOp_ = c;
                    displayStr_ = "";
                } else {
                    MyBigDecimal temp = calculate(currValue_, formatString(displayStr_), currOp_);
                    prevValue_ = calculate(prevValue_, temp, prevOp_);
                    prevOp_ = c;
                    displayStr_ = "";
                    currValue_ = MyBigDecimal.ZERO;
                    currOp_ = null;
                    state_ = STATE_STACK_ONE;
                }
                break;
            case STATE_RESULT:
                prevOp_ = c;
                displayStr_ = "";
                state_ = STATE_STACK_ONE;
                break;
        }
    }

    public void clear() {
        prevValue_ = MyBigDecimal.ZERO;
        prevOp_ = null;
        displayStr_ = "";
        currValue_ = MyBigDecimal.ZERO;
        currOp_ = null;
        state_ = STATE_INIT;
    }

    public MyBigDecimal getPrevValue() {
        return prevValue_;
    }

    public CalKey getPrevOp() {
        return prevOp_;
    }

    public String getDisplayStr() {
        return displayStr_;
    }

    public MyBigDecimal getCurrValue() {
        return currValue_;
    }

    public CalKey getCurrOp() {
        return currOp_;
    }

    public int getState() {
        return state_;
    }

    private MyBigDecimal calculate(MyBigDecimal a, MyBigDecimal b, CalKey c) {
        if (CalKey.ADD.equals(c)) {
            return a.add(b);
        } else if (CalKey.SUB.equals(c)) {
            return a.subtract(b);
        } else if (CalKey.MUL.equals(c)) {
            return a.multiply(b);
        } else {
            return a.divide(b);
        }
    }

    private MyBigDecimal formatString(String s) {
        if (s.endsWith(".")) {
            s = s.substring(0, s.length() - 1);
        }
        return new MyBigDecimal(s);
    }
}
