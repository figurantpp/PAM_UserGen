package com.example.usergen.service.generator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.usergen.model.User;
import com.example.usergen.service.json.ModelJsonManager;
import com.example.usergen.service.json.UserJsonManager;
import com.example.usergen.util.RandomApiInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class NetworkRandomUserGenerator implements RandomModelGenerator<User> {

    @NonNull
    private final Context context;

    @NonNull
    private final RandomUserGeneratorInput input;

    @NonNull
    private final ModelJsonManager<User> jsonManager;

    public NetworkRandomUserGenerator(
            @NonNull Context context,
            @NonNull RandomUserGeneratorInput input,
            @NonNull ModelJsonManager<User> jsonManager
    ) throws NoNetworkException {
        this.context = context;
        this.input = input;
        this.jsonManager = jsonManager;

        checkConnectivity();
    }

    public NetworkRandomUserGenerator(
            @NonNull Context context,
            @NonNull RandomUserGeneratorInput input
    ) throws NoNetworkException {
        this(context, input, new UserJsonManager());
    }

    @NonNull
    @Override
    public Single<User> nextRandomModel() {
        return Single.fromSupplier(() -> this.nextRandomModelsBlocked(1).get(0));
    }

    @NonNull
    @Override
    public Single<List<User>> nextModels(int limit) {
        return Single.fromSupplier(() -> this.nextRandomModelsBlocked(limit));
    }

    private void checkConnectivity() throws NoNetworkException {
        if (!hasConnectivity()) {
            throw new NoNetworkException("Failed to connect to network");
        }
    }

    private boolean hasConnectivity() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }

    @NonNull
    private List<User> nextRandomModelsBlocked(int limit) {

        URL url = uriToUrl(buildApiUri(limit));

        try {
            return getMultipleUsersFromApi(url);
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    @NonNull
    private URL uriToUrl(Uri apiUri) {
        try {
            return new URL(apiUri.toString());
        } catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Uri buildApiUri(int limit) {
        return getApiUriBuilder().appendQueryParameter("results", String.valueOf(limit)).build();
    }

    private Uri.Builder getApiUriBuilder() {

        return Uri.parse(RandomApiInfo.API_URL).buildUpon()
                .appendQueryParameter(RandomApiInfo.INCLUDE_QUERY_PARAMETER,
                        RandomApiInfo.INCLUDE_QUERY_PARAMATER_LIST)
                .appendQueryParameter(RandomApiInfo.NATIONALITY_QUERY_PARAMETER, input.getNationality())
                .appendQueryParameter(RandomApiInfo.GENDER_QUERY_PARAMETER, input.getGender());

    }

    @NonNull
    private String stringFromInputStream(@NonNull InputStream stream) throws IOException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];

        int read;

        while ((read = stream.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }

        return output.toString();
    }

    @NonNull
    private HttpURLConnection connectFromUrl(@NonNull URL apiUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

        connection.setReadTimeout(1500);
        connection.setConnectTimeout(2000);

        connection.setRequestMethod("GET");

        connection.connect();

        return connection;
    }

    private List<User> userListFromRequestOutput(@NonNull JSONObject root) throws JSONException {

        JSONArray userArray = getUserJSONArray(root);

        List<User> users = new ArrayList<>(userArray.length());

        for (int i = 0; i < userArray.length(); i++) {

            User user = jsonManager.getModelFromJSONObject(userArray.getJSONObject(i));

            users.add(user);
        }

        return users;
    }

    private JSONArray getUserJSONArray(@NonNull JSONObject root) throws JSONException {
        return root.getJSONArray("results");
    }

    private List<User> getMultipleUsersFromApi(@NonNull URL apiUrl) throws IOException, JSONException {

        return userListFromRequestOutput(new JSONObject(performRequest(apiUrl)));
    }

    private String performRequest(@NonNull URL apiUrl) throws IOException {
        HttpURLConnection connection = connectFromUrl(apiUrl);

        int statusCode = connection.getResponseCode();

        if (statusCode != 200) {
            throw new RuntimeException("Server responded with status code " + statusCode);
        }

        InputStream stream = connection.getInputStream();

        return stringFromInputStream(stream);
    }
}
