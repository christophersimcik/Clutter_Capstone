package com.clutter.note.main;

import android.app.PendingIntent;
import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import static com.clutter.note.main.R.*;

public class AppWidgetList extends android.appwidget.AppWidgetProvider {
    AppWidgetManager appWidgetManager;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Intent searchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,searchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), layout.appwidget);
            int listView = id.widget_list;
            views.setPendingIntentTemplate(id.widget_list,pendingIntent);
            Intent intent = new Intent(context, WidgetRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            views.setRemoteAdapter(id.widget_list, intent);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], listView);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            }
    }
    public static void refreshWidget(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context,AppWidgetList.class));
        context.sendBroadcast(intent);
    }
    @Override
    public void onReceive(final Context context, Intent intent){ final String action = intent.getAction();
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context,AppWidgetList.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(componentName), id.widget_list);
        }
        super.onReceive(context, intent);
    }
}