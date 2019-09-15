package com.example.appwidgethomework;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

import static com.example.appwidgethomework.MainActivity.KEY_INPUT_STRING;
import static com.example.appwidgethomework.MainActivity.PREFS_FILE;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidgetHomework extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);

        String widgetText = prefs.getString(KEY_INPUT_STRING, "Empty");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_homework);

        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(KEY_INPUT_STRING + appWidgetId, widgetText);
        prefEditor.apply();

        Intent intent = new Intent(context, MainActivity.class);

        int[] idArray = new int[]{appWidgetId};

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        String dateString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());

        views.setOnClickPendingIntent(R.id.app_widget_layout, pendingIntent);

        views.setTextViewText(R.id.appwidget_text, widgetText);

        views.setTextViewText(R.id.time_textview, dateString);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

