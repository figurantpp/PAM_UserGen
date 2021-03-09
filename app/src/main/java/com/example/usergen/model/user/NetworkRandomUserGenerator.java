package com.example.usergen.model.user;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.usergen.model.exception.ProgramException;
import com.example.usergen.model.interfaces.ModelJsonManager;
import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.util.ApiInfo;
import com.example.usergen.util.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NetworkRandomUserGenerator implements RandomModelGenerator<User> {

    @NonNull
    private final Context context;

    @NonNull
    private final NetworkRandomUserGeneratorInput input;

    @NonNull
    private final ModelJsonManager<User> jsonManager;

    @NonNull
    private final ExecutorService executor;

    public NetworkRandomUserGenerator(@NonNull Context context,
                                      @NonNull NetworkRandomUserGeneratorInput input,
                                      @NonNull ExecutorService executor) {
        this.context = context;
        this.input = input;
        this.jsonManager = new UserJsonManager();
        this.executor = executor;

        checkConnectivity();
    }


    public NetworkRandomUserGenerator(@NonNull Context context,
                                      @NonNull NetworkRandomUserGeneratorInput input) {
        this.context = context;
        this.input = input;
        this.jsonManager = new UserJsonManager();
        this.executor = Executors.newSingleThreadExecutor();

        checkConnectivity();
    }

    private void checkConnectivity()
    {
        if (!hasConnectivity()) {
            throw new ProgramException("Failed to connect to network");
        }
    }

    private boolean hasConnectivity() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }


    @NonNull
    private URL getApiURL() {
        Uri.Builder uriBuilder = Uri.parse(ApiInfo.API_URL).buildUpon();

        uriBuilder.appendQueryParameter(ApiInfo.INCLUDE_QUERY_PARAMETER,
                ApiInfo.INCLUDE_QUERY_PARAMATER_LIST);

        uriBuilder.appendQueryParameter(ApiInfo.NATIONALITY_QUERY_PARAMETER, input.getNationality());

        uriBuilder.appendQueryParameter(ApiInfo.GENDER_QUERY_PARAMETER, input.getGender());

        Uri apiUri = uriBuilder.build();

        URL result;

        try {
            result = new URL(apiUri.toString());
        } catch (MalformedURLException exception) {
            Log.e(Tags.ERROR, "Malformed URL", exception);
            throw new RuntimeException(exception);
        }

        return result;
    }

    @NonNull
    private String stringFromInputStream(@NonNull InputStream stream) throws IOException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];

        while (stream.read(buffer) != -1) {
            output.write(buffer);
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

    @NonNull
    private User userFromOutputString(@NonNull String input) throws JSONException {

        JSONObject root = new JSONObject(input);

        JSONObject userObject = root.getJSONArray("results").getJSONObject(0);

        return jsonManager.getModelFromJSONObject(userObject);
    }

    @NonNull
    private User getUserFromApi(@NonNull URL apiUrl) throws IOException, JSONException {

        HttpURLConnection connection = connectFromUrl(apiUrl);

        int statusCode = connection.getResponseCode();

        if (statusCode != 200) {
            throw new RuntimeException("Server responded with status code " + statusCode);
        }

        InputStream stream = connection.getInputStream();

        String result = stringFromInputStream(stream);

        return userFromOutputString(result);
    }

    @NonNull
    private User nextRandomModelBlocked() {
        URL url = getApiURL();

        User user;

        try {

            user = getUserFromApi(url);
        } catch (IOException ex) {
            Log.e(Tags.ERROR, "Failed to get user from URL: ", ex);
            throw new RuntimeException(ex);
        } catch (JSONException ex) {
            Log.e(Tags.ERROR, "Failed to parse JSON: ", ex);
            throw new RuntimeException(ex);
        }

        return user;
    }

    @NonNull
    @Override
    public Future<User> nextRandomModel() {
        return executor.submit(this::nextRandomModelBlocked);
    }

}
