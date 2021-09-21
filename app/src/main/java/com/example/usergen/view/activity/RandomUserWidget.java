package com.example.usergen.view.activity;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.example.usergen.R;
import com.example.usergen.model.User;
import com.example.usergen.service.generator.RandomModelGenerator;
import com.example.usergen.service.generator.RandomUserGeneratorInput;
import com.example.usergen.util.RandomApiInfo;
import com.example.usergen.util.Tags;

import java.util.Objects;

import static com.example.usergen.service.generator.RandomUserGeneratorResolver.resolveUserGenerator;

/**
 * Implementation of App Widget functionality.
 */
public class RandomUserWidget extends AppWidgetProvider {

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        Log.d(Tags.DEBUG, "updateAppWidget: " + appWidgetId);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.random_user_widget);

        loadViews(context, views);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void loadViews(@NonNull Context context, @NonNull RemoteViews views) {

        Objects.requireNonNull(context);
        Objects.requireNonNull(views);

        User user = getUser(context);

        views.setImageViewBitmap(R.id.widgetImageView, user.getProfileImage().requireBitmap());

        views.setTextViewText(R.id.widgetNameTextView, user.getName());

        views.setTextViewText(R.id.widgetNationalityTextView,
                RandomApiInfo.NATIONALITY_NAMES.getOrDefault(
                        user.getNationality(),
                        user.getNationality()
                )
        );


    }

    private User getUser(@NonNull Context context) {

        RandomUserGeneratorInput input
                = new RandomUserGeneratorInput(
                null,
                Math.random() < 0.5 ? "male" : "female"
        );

        RandomModelGenerator<User> generator = resolveUserGenerator(context, input);

        return generator.nextRandomModel()
                .doOnSuccess(u -> u.getProfileImage().getBitmapSync())
                .blockingGet();
    }
    @Override
    public void onUpdate(
            @NonNull Context context,
            @NonNull AppWidgetManager appWidgetManager,
            @NonNull int[] appWidgetIds
    ) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(@NonNull Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(@NonNull Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}