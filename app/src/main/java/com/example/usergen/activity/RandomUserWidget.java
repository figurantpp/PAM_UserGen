package com.example.usergen.activity;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.example.usergen.R;
import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.model.user.User;
import com.example.usergen.model.user.generator.RandomUserGeneratorInput;
import com.example.usergen.util.ApiInfo;
import com.example.usergen.util.Tags;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.example.usergen.model.user.generator.RandomUserGeneratorResolver.resolveUserGenerator;

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

        User user;

        try {
            user = getUser(context);
        } catch (ExecutionException | InterruptedException ex) {
            Log.e(Tags.ERROR, "loadViews: ", ex);
            return;
        }

        views.setImageViewBitmap(R.id.widgetImageView, user.getProfileImage().requireBitmap());

        views.setTextViewText(R.id.widgetNameTextView, user.getName());

        views.setTextViewText(R.id.widgetNationalityTextView,
                ApiInfo.NATIONALITY_NAMES.getOrDefault(
                        user.getNationality(),
                        user.getNationality()
                )
        );


    }

    private User getUser(@NonNull Context context) throws java.util.concurrent.ExecutionException, InterruptedException {

        RandomUserGeneratorInput input
                = new RandomUserGeneratorInput(
                null,
                Math.random() < 0.5 ? "male" : "female"
        );

        ExecutorService executor = Executors.newSingleThreadExecutor();

        RandomModelGenerator<User> generator = resolveUserGenerator(context, input, executor);

        Future<User> result = generator.nextRandomModel();

        User user = result.get();

        executor.submit(() -> getUserImage(user)).get();

        return user;
    }

    private void getUserImage(User user) {
        try {
            user.getProfileImage().getBitmap();
        } catch (IOException ex) {
            Log.e(Tags.ERROR, "getUser: ", ex);

            throw new RuntimeException(ex);
        }
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