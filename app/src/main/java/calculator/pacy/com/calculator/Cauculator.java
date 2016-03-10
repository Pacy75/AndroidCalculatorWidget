package calculator.pacy.com.calculator;

import android.content.Context;

/**
 * Created by pacy on 2016/3/8.
 */
public class Cauculator {
    public static final int INPUT_MAX_LENGTH = 20;
    public static final int RESP_OK = 0;
    public static final int RESP_ERROR_INPUT_TOLONG = -1;

    private static final int STATE_INIT = 0;
    private static final int STATE_STACK_ONE = 1;
    private static final int STATE_STACK_TWO = 2;
    private static final int STATE_RESULT = 3;

    private double prevValue_;
    private CalKey prevOp_;
    private String displayStr_;
    private double currValue_;
    private CalKey currOp_;
    private int state_;

    public static Cauculator getInstance(Context context, int appwidgetId) {
        System.out.println("Cauculator.getInstance");
        double prevValue = CalPreference.getPrevValue(context, appwidgetId);
        CalKey prevOp = CalPreference.getPrevOp(context, appwidgetId);
        String displayStr = CalPreference.getDisplayStr(context, appwidgetId);
        double currValue = CalPreference.getCurrValue(context, appwidgetId);
        CalKey currOp = CalPreference.getCurrOp(context, appwidgetId);
        int state = CalPreference.getStateCode(context, appwidgetId);
        return new Cauculator(prevValue, prevOp, displayStr, currValue, currOp, state);
    }

    public static void saveInstance(Context context, int appwidgetId, Cauculator calculator) {
        System.out.println("Cauculator.saveInstance");
        CalPreference.setPrevValue(context, appwidgetId, calculator.getPrevValue());
        CalPreference.setPrevOp(context, appwidgetId, calculator.getPrevOp());
        CalPreference.setDisplayStr(context, appwidgetId, calculator.getDisplayStr());
        CalPreference.setCurrValue(context, appwidgetId, calculator.getCurrValue());
        CalPreference.setCurrOp(context, appwidgetId, calculator.getCurrOp());
        CalPreference.setStateCode(context, appwidgetId, calculator.getState());
    }

    public Cauculator(double prevValue, CalKey prevOp, String displayStr, double currValue,
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
            System.out.println("getDisplayValue:" + displayStr_);
            return displayStr_;
        } else if (state_ == STATE_STACK_TWO) {
            System.out.println("getDisplayValue:" + formatDouble(currValue_));
            return formatDouble(currValue_);
        } else {
            System.out.println("getDisplayValue:" + formatDouble(prevValue_));
            return formatDouble(prevValue_);
        }
    }

    public String getDisplayOperator() {
        if (state_ == STATE_INIT || state_ == STATE_RESULT) {
            System.out.println("getDisplayOperator:");
            return "";
        } else if (state_ == STATE_STACK_ONE) {
            System.out.println("getDisplayOperator:" + prevOp_.getChar());
            return Character.toString(prevOp_.getChar());
        } else if (state_ == STATE_STACK_TWO) {
            System.out.println("getDisplayOperator:" + currOp_.getChar());
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
            if (displayStr_.length() > 0) {
                displayStr_ = displayStr_.substring(0, displayStr_.length() - 1);
            }
        } else if (CalKeyType.KEY_NUMBER.equals(c.getType())) {
            return inputNumeber(c);
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
        currValue_ = 0;
        currOp_ = null;
        state_ = STATE_RESULT;
    }

    private int inputNumeber(CalKey c) {
        if (STATE_RESULT == state_) {
            prevValue_ = 0;
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
                    prevValue_ = 0;
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
                } else if ((CalKey.PLUS.equals(prevOp_) || CalKey.MINUS.equals(prevOp_)) &&
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
                    double temp = calculate(currValue_, formatString(displayStr_), currOp_);
                    prevValue_ = calculate(prevValue_, temp, prevOp_);
                    prevOp_ = c;
                    displayStr_ = "";
                    currValue_ = 0;
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
        prevValue_ = 0;
        prevOp_ = null;
        displayStr_ = "";
        currValue_ = 0;
        currOp_ = null;
        state_ = STATE_INIT;
    }

    public double getPrevValue() {
        return prevValue_;
    }

    public CalKey getPrevOp() {
        return prevOp_;
    }

    public String getDisplayStr() {
        return displayStr_;
    }

    public double getCurrValue() {
        return currValue_;
    }

    public CalKey getCurrOp() {
        return currOp_;
    }

    public int getState() {
        return state_;
    }

    private double calculate(double a, double b, CalKey c) {
        if (CalKey.PLUS.equals(c)) {
            return a + b;
        } else if (CalKey.MINUS.equals(c)) {
            return a - b;
        } else if (CalKey.MUL.equals(c)) {
            return a * b;
        } else {
            return a / b;
        }
    }

    private String formatDouble(double f) {
        if (f == (long) f) {
            return String.format("%d", (long) f);
        } else {
            return String.format("%s", f);
        }
    }

    private double formatString(String s) {
        if (s.endsWith(".")) {
            s = s.substring(0, s.length() - 1);
        }
        return Double.valueOf(s);
    }

    public void printCalculator() {
        System.out.println("===print===");
        System.out.println("state = " + state_+", displayStr = " + displayStr_);
        System.out.println("prevValue = " + prevValue_ + ", prevOp = " + ((prevOp_ == null) ? "" : prevOp_.name()));
        System.out.println("currValue = " + currValue_ + ", currOp = " + ((currOp_ == null) ? "" : currOp_.name()));
    }
}
