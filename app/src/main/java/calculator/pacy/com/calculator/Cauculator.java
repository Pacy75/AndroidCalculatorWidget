package calculator.pacy.com.calculator;

import android.content.Context;

/**
 * Created by pacy on 2016/3/8.
 */
public class Cauculator {
    public static final int INPUT_MAX_LENGTH = 10;
    public static final int RESP_OK = 0;
    public static final int RESP_ERROR_INPUT_TOLONG = -1;

    private static final int STATE_INIT = 0;
    private static final int STATE_STACK_ONE = 1;
    private static final int STATE_STACK_TWO = 2;
    private static final int STATE_RESULT = 3;

    private float prevValue_;
    private CalKey prevOp_;
    private String displayStr_;
    private float currValue_;
    private CalKey currOp_;
    private int state_;

    public static Cauculator getInstance(Context context, int appwidgetId) {
        float prevValue = CalPreference.getPrevValue(context, appwidgetId);
        CalKey prevOp = CalPreference.getPrevOp(context, appwidgetId);
        String displayStr = CalPreference.getDisplayStr(context, appwidgetId);
        float currValue = CalPreference.getCurrValue(context, appwidgetId);
        CalKey currOp = CalPreference.getCurrOp(context, appwidgetId);
        int state = CalPreference.getStateCode(context, appwidgetId);
        return new Cauculator(prevValue, prevOp, displayStr, currValue, currOp, state);
    }

    public static void saveInstance(Context context, int appwidgetId, Cauculator calculator) {
        CalPreference.setPrevValue(context, appwidgetId, calculator.getPrevValue());
        CalPreference.setPrevOp(context, appwidgetId, calculator.getPrevOp());
        CalPreference.setDisplayStr(context, appwidgetId, calculator.getDisplayStr());
        CalPreference.setCurrValue(context, appwidgetId, calculator.getCurrValue());
        CalPreference.setCurrOp(context, appwidgetId, calculator.getCurrOp());
        CalPreference.setStateCode(context, appwidgetId, calculator.getState());
    }

    public Cauculator(float prevValue, CalKey prevOp, String displayStr, float currValue,
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
            System.out.println("getDisplayValue:" + formatFloat(currValue_));
            if (0 == currValue_) {
                return "0";
            } else {
                return formatFloat(currValue_);
            }
        } else {
            System.out.println("getDisplayValue:" + formatFloat(prevValue_));
            if (0 == prevValue_) {
                return "0";
            } else {
                return formatFloat(prevValue_);
            }
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
            case STATE_STACK_ONE:
                if (0 == displayStr_.length()) {
                    prevValue_ = calculate(prevValue_, prevValue_, prevOp_);
                } else {
                    prevValue_ = calculate(prevValue_, Float.valueOf(displayStr_), prevOp_);
                }
                state_ = STATE_RESULT;
                displayStr_ = "";
                break;
            case STATE_STACK_TWO:
                if (0 == displayStr_.length()) {
                    currValue_ = calculate(currValue_, currValue_, currOp_);
                } else {
                    currValue_ = calculate(currValue_, Float.valueOf(displayStr_), currOp_);
                }
                prevValue_ = calculate(prevValue_, currValue_, prevOp_);
                displayStr_ = "";
                state_ = STATE_RESULT;
                break;
        }
        prevOp_ = null;
        currValue_ = 0;
        currOp_ = null;
    }

    private int inputNumeber(CalKey c) {
        if (STATE_RESULT == state_) {
            prevValue_ = 0;
            displayStr_ = "";
            state_ = STATE_INIT;
        }
        if (CalKey.DOT.equals(c) && displayStr_.contains(".")) {
            return RESP_OK;
        }
        if (displayStr_.length() == 1 && displayStr_.contains("0")) {
            if (CalKey.NUMBER_0.equals(c)) {
                return RESP_OK;
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
                    prevValue_ = Float.valueOf(displayStr_);
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
                    currValue_ = Float.valueOf(displayStr_);
                    currOp_ = c;
                    displayStr_ = "";
                    state_ = STATE_STACK_TWO;
                } else {
                    prevValue_ = calculate(prevValue_, Float.valueOf(displayStr_), prevOp_);
                    prevOp_ = c;
                    displayStr_ = "";
                }
                break;
            case STATE_STACK_TWO:
                if (displayStr_.length() == 0) {
                    currOp_ = c;
                } else if (CalKey.MUL.equals(c) || CalKey.DIV.equals(c)) {
                    currValue_ = calculate(currValue_, Float.valueOf(displayStr_), currOp_);
                    currOp_ = c;
                    displayStr_ = "";
                } else {
                    float temp = calculate(currValue_, Float.valueOf(displayStr_), currOp_);
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

    public float getPrevValue() {
        return prevValue_;
    }

    public CalKey getPrevOp() {
        return prevOp_;
    }

    public String getDisplayStr() {
        return displayStr_;
    }

    public float getCurrValue() {
        return currValue_;
    }

    public CalKey getCurrOp() {
        return currOp_;
    }

    public int getState() {
        return state_;
    }

    private float calculate(float a, float b, CalKey c) {
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

    private String formatFloat(float f) {
        if (f == (long) f) {
            return String.format("%d", (long) f);
        } else {
            return String.format("%s", f);
        }
    }

    public void printCalculator() {
        System.out.println("=========");
        System.out.println("state = " + state_);
        System.out.println("displayStr = " + displayStr_);
        System.out.println("prevValue = " + prevValue_);
        if (null != prevOp_) {
            System.out.println("prevOp = " + prevOp_.name());
        } else {
            System.out.println("prevOp = null");
        }
        System.out.println("currValue = " + currValue_);
        if (null != currOp_) {
            System.out.println("currOp = " + currOp_.name());
        } else {
            System.out.println("currOp = null");
        }
    }
}
