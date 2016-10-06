package org.radiokit.almanac.request;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import org.radiokit.almanac.R;
import org.radiokit.almanac.model.AccessToken;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by mateuszziomek on 20.09.16.
 */
public class AccessTokenRequest extends GoogleHttpClientSpiceRequest<AccessToken> {

    private final static String TARGET_URL = "/api/oauth2/token";
    private HttpURLConnection connection;
    private String urlParams;
    private String username, password;
    private AccessToken accessToken;
    private String baseUrl;
    private int responseCode;

    public AccessTokenRequest(Context context, String username, String password) {
        super(AccessToken.class);
        this.urlParams = "grant_type=" + context.getString(R.string.grantType) +
                "&client_id=" + context.getString(R.string.clientId) +
                "&client_secret=" + context.getString(R.string.clientSecret);
        this.username = username;
        this.password = password;
        this.baseUrl = context.getString(R.string.baseUrl);
    }

    public String getAuthorization() {
        return "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
    }

    @Override
    public AccessToken loadDataFromNetwork() throws Exception {

        try {
            // Prepare parameters
            byte[] bytes = urlParams.getBytes(StandardCharsets.UTF_8);

            URL url = new URL(baseUrl+TARGET_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Authorization", getAuthorization());
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            connection.setFixedLengthStreamingMode(bytes.length);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.write(bytes);
            responseCode = connection.getResponseCode();

            // JSON to POJO from InputStream using Jackson 2
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            ObjectMapper mapper = new ObjectMapper();
            accessToken = mapper.readValue(inputStream, AccessToken.class);
        } catch (IOException e) {
            Log.d(getClass().getSimpleName(), "PROBLEM WITH CONNECTION. CODE : " + responseCode);
        } finally {
            connection.disconnect();
        }
        if (accessToken != null) {
            return accessToken;
        } else throw new Exception("Could not parse the JSON");
    }
}
