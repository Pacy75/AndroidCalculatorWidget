package calculator.pacy.com.calculator;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Implementation of App Widget functionality.
 */
public class CalculatorWidget extends AppWidgetProvider {
    public static final HashMap<String, Integer> ACTION_MAP = new HashMap<String, Integer>();
    public static final String NUMBER_0 = "calculator.pacy.com.calculator.0";
    public static final String NUMBER_1 = "calculator.pacy.com.calculator.1";
    public static final String NUMBER_2 = "calculator.pacy.com.calculator.2";
    public static final String NUMBER_3 = "calculator.pacy.com.calculator.3";
    public static final String NUMBER_4 = "calculator.pacy.com.calculator.4";
    public static final String NUMBER_5 = "calculator.pacy.com.calculator.5";
    public static final String NUMBER_6 = "calculator.pacy.com.calculator.6";
    public static final String NUMBER_7 = "calculator.pacy.com.calculator.7";
    public static final String NUMBER_8 = "calculator.pacy.com.calculator.8";
    public static final String NUMBER_9 = "calculator.pacy.com.calculator.9";
    public static final String DOT = "calculator.pacy.com.calculator.dot";
    public static final String ADD = "calculator.pacy.com.calculator.add";
    public static final String SUB = "calculator.pacy.com.calculator.sub";
    public static final String MUL = "calculator.pacy.com.calculator.mul";
    public static final String DIV = "calculator.pacy.com.calculator.div";
    public static final String EQUALS = "calculator.pacy.com.calculator.equals";
    public static final String DEL = "calculator.pacy.com.calculator.delete";
    public static final String AC = "calculator.pacy.com.calculator.AC";
    static {
        ACTION_MAP.put(NUMBER_0, R.id.number0);
        ACTION_MAP.put(NUMBER_1, R.id.number1);
        ACTION_MAP.put(NUMBER_2, R.id.number2);
        ACTION_MAP.put(NUMBER_3, R.id.number3);
        ACTION_MAP.put(NUMBER_4, R.id.number4);
        ACTION_MAP.put(NUMBER_5, R.id.number5);
        ACTION_MAP.put(NUMBER_6, R.id.number6);
        ACTION_MAP.put(NUMBER_7, R.id.number7);
        ACTION_MAP.put(NUMBER_8, R.id.number8);
        ACTION_MAP.put(NUMBER_9, R.id.number9);
        ACTION_MAP.put(DOT, R.id.dot);
        ACTION_MAP.put(ADD, R.id.add);
        ACTION_MAP.put(SUB, R.id.sub);
        ACTION_MAP.put(MUL, R.id.mul);
        ACTION_MAP.put(DIV, R.id.div);
        ACTION_MAP.put(EQUALS, R.id.equal);
        ACTION_MAP.put(DEL, R.id.del);
        ACTION_MAP.put(AC, R.id.ac);
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.calculator_widget);
        Calculator calculator = Calculator.getInstance(context, appWidgetId);
        String st = calculator.getDisplayValue();
        views.setTextViewTextSize(R.id.display, TypedValue.COMPLEX_UNIT_SP, getProperTextSize(st.length()));
        views.setTextViewText(R.id.display, st);
        views.setTextViewText(R.id.operator, calculator.getDisplayOperator());
        setOnClickListeners(context, appWidgetId, views);
        if (null != appWidgetManager) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private float getProperTextSize(int strLength) {
        if (strLength > 18) {
            return 24f;
        } else if (strLength > 12) {
            return 30f;
        } else {
            return 40f;
        }
    }

    private void setOnClickListeners(Context context, int appWidgetId, RemoteViews remoteViews) {
        final Intent intent = new Intent(context, CalculatorWidget.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        for(String action : ACTION_MAP.keySet()) {
            intent.setAction(action);
            remoteViews.setOnClickPendingIntent(ACTION_MAP.get(action),
                    PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        CalKey calKey = CalKey.getByAction(intent.getAction());
        if (null != calKey) {
            handleReceive(context, appWidgetId, calKey);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void handleReceive(Context context, int appWidgetId, CalKey calKey) {
        Calculator calculator = Calculator.getInstance(context, appWidgetId);
        int result = calculator.input(calKey);
        if (result == Calculator.INPUT_MAX_LENGTH) {
            Toast.makeText(context, "Max length of input is " + Calculator.INPUT_MAX_LENGTH, Toast.LENGTH_SHORT).show();
        }
        Calculator.saveInstance(context, appWidgetId, calculator);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        updateAppWidget(context, appWidgetManager, appWidgetId);
    }
}

