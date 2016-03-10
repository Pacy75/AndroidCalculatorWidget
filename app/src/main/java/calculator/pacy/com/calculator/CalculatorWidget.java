package calculator.pacy.com.calculator;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class CalculatorWidget extends AppWidgetProvider {
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
    public static final String PLUS = "calculator.pacy.com.calculator.plus";
    public static final String MINUS = "calculator.pacy.comcalculator.minus";
    public static final String MUL = "calculator.pacy.com.calculator.mul";
    public static final String DIV = "calculator.pacy.com.calculator.div";
    public static final String EQUALS = "calculator.pacy.com.calculator.equals";
    public static final String DEL = "com.numix.calculator.delete";
    public static final String AC = "calculator.pacy.com.calculator.AC";

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        System.out.println("updateAppWidget:" + appWidgetId);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.calculator_widget);
        Cauculator cauculator = Cauculator.getInstance(context, appWidgetId);
        cauculator.printCalculator();//TODO DEBUG
        String st = cauculator.getDisplayValue();
        views.setTextViewTextSize(R.id.display, TypedValue.COMPLEX_UNIT_SP, st.length() > 10 ? 25f : 40f);
        views.setTextViewText(R.id.display, st);
        views.setTextViewText(R.id.operator, cauculator.getDisplayOperator());
        setOnClickListeners(context, appWidgetId, views);
        if (null != appWidgetManager) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void setOnClickListeners(Context context, int appWidgetId, RemoteViews remoteViews) {
        final Intent intent = new Intent(context, CalculatorWidget.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        intent.setAction(NUMBER_0);
        remoteViews.setOnClickPendingIntent(R.id.number0, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(NUMBER_1);
        remoteViews.setOnClickPendingIntent(R.id.number1, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(NUMBER_2);
        remoteViews.setOnClickPendingIntent(R.id.number2, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(NUMBER_3);
        remoteViews.setOnClickPendingIntent(R.id.number3, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(NUMBER_4);
        remoteViews.setOnClickPendingIntent(R.id.number4, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(NUMBER_5);
        remoteViews.setOnClickPendingIntent(R.id.number5, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(NUMBER_6);
        remoteViews.setOnClickPendingIntent(R.id.number6, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(NUMBER_7);
        remoteViews.setOnClickPendingIntent(R.id.number7, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(NUMBER_8);
        remoteViews.setOnClickPendingIntent(R.id.number8, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(NUMBER_9);
        remoteViews.setOnClickPendingIntent(R.id.number9, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(DOT);
        remoteViews.setOnClickPendingIntent(R.id.dot, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(DIV);
        remoteViews.setOnClickPendingIntent(R.id.div, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(MUL);
        remoteViews.setOnClickPendingIntent(R.id.mul, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(MINUS);
        remoteViews.setOnClickPendingIntent(R.id.minus, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(PLUS);
        remoteViews.setOnClickPendingIntent(R.id.plus, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(EQUALS);
        remoteViews.setOnClickPendingIntent(R.id.equal, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(AC);
        remoteViews.setOnClickPendingIntent(R.id.ac, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.setAction(DEL);
        remoteViews.setOnClickPendingIntent(R.id.delete, PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("onReceive");
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        System.out.println("appWidgetId=" + appWidgetId);
        System.out.println("action=" + intent.getAction());
        CalKey calKey = CalKey.getByAction(intent.getAction());
        if (null != calKey) {
            handleReceive(context, appWidgetId, calKey);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        System.out.println("onUpdate:" + appWidgetIds.toString());
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        System.out.println("onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        System.out.println("onDisabled");

    }

    private void handleReceive(Context context, int appWidgetId, CalKey calKey) {
        Cauculator cauculator = Cauculator.getInstance(context, appWidgetId);
        cauculator.printCalculator();//TODO DEBUG
        System.out.println(calKey.name());
        int result = cauculator.input(calKey);
        cauculator.printCalculator();//TODO DEBUG
        if (result == Cauculator.INPUT_MAX_LENGTH) {
            Toast.makeText(context, "Max length of input is 10", Toast.LENGTH_SHORT).show();
        }
        Cauculator.saveInstance(context, appWidgetId, cauculator);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        updateAppWidget(context, appWidgetManager, appWidgetId);
    }
}

