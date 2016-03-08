package calculator.pacy.com.calculator;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by pacy on 2016/3/8.
 */
public class CalPreference {
    private final static String CALCULATOR_PREFERENCE = "calculator.pacy.com.calculator.CALCULATOR_PREFERENCE_";
    private final static String PREV_VALUE = "PREV_VALUE";
    private final static String PREV_OP = "PREV_OP";
    private final static String DISPLAY_STR = "DISPLAY_STR";
    private final static String CURR_VALUE = "CURR_VALUE";
    private final static String CURR_OP = "CURR_OP";
    private final static String STATE_CODE = "STATE_CODE";

    public static float getPrevValue(Context context, int appwidgetId) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(CALCULATOR_PREFERENCE + PREV_VALUE + appwidgetId, 0F);
    }

    public static void setPrevValue(Context context, int appWidgetId, float newValue) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().
                putFloat(CALCULATOR_PREFERENCE + PREV_VALUE + appWidgetId, newValue).commit();
    }

    public static CalKey getPrevOp(Context context, int appwidgetId) {
        String action = PreferenceManager.getDefaultSharedPreferences(context).getString(CALCULATOR_PREFERENCE + PREV_OP + appwidgetId, "");
        return CalKey.getByAction(action);
    }

    public static void setPrevOp(Context context, int appWidgetId, CalKey newKey) {
        String action = "";
        if (null != newKey) {
            action = newKey.getAction();
        }
        PreferenceManager.getDefaultSharedPreferences(context).edit().
                putString(CALCULATOR_PREFERENCE + PREV_OP + appWidgetId, action).commit();
    }

    public static String getDisplayStr(Context context, int appwidgetId) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(CALCULATOR_PREFERENCE + DISPLAY_STR + appwidgetId, "");
    }

    public static void setDisplayStr(Context context, int appWidgetId, String newString) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(CALCULATOR_PREFERENCE + DISPLAY_STR + appWidgetId, newString).commit();
    }

    public static float getCurrValue(Context context, int appwidgetId) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(CALCULATOR_PREFERENCE + CURR_VALUE + appwidgetId, 0F);
    }

    public static void setCurrValue(Context context, int appWidgetId, float newValue) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().
                putFloat(CALCULATOR_PREFERENCE + CURR_VALUE + appWidgetId, newValue).commit();
    }

    public static CalKey getCurrOp(Context context, int appwidgetId) {
        String action = PreferenceManager.getDefaultSharedPreferences(context).getString(CALCULATOR_PREFERENCE + CURR_OP + appwidgetId, "");
        return CalKey.getByAction(action);
    }

    public static void setCurrOp(Context context, int appWidgetId, CalKey newKey) {
        String action = "";
        if (null != newKey) {
            action = newKey.getAction();
        }
        PreferenceManager.getDefaultSharedPreferences(context).edit().
                putString(CALCULATOR_PREFERENCE + CURR_OP + appWidgetId, action).commit();
    }

    public static int getStateCode(Context context, int appwidgetId) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(CALCULATOR_PREFERENCE + STATE_CODE + appwidgetId, 0);
    }

    public static void setStateCode(Context context, int appWidgetId, int newValue) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().
                putInt(CALCULATOR_PREFERENCE + STATE_CODE + appWidgetId, newValue).commit();
    }
}
