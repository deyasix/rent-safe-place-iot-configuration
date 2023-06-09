package com.example.rentsafeplaceiotconfiguration.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HttpConnection {
    private static HttpURLConnection connection;
    private static String auth;

    public static String login(String email, String password) throws IOException {
        URL url = new URL("http://127.0.0.1:8088/realtors/login");
        String requestBody = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setUseCaches(true);
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);
            outputStream.write(requestBodyBytes);
            outputStream.flush();
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            if (!response.isEmpty()) {
                byte[] authBytes = Base64.getEncoder().encode((email + ":" + password).getBytes());
                auth = "Basic " + new String(authBytes);
            }
            connection.disconnect();
            return response.toString();
        }
    }

    public static String request(URL url, String method, String requestBody) throws IOException {
        connection = (HttpURLConnection) url.openConnection();
        // Set the request method and headers
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", auth);
        connection.setUseCaches(true);
        connection.setDoOutput(true);
        if (!requestBody.isEmpty()) {
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);
                outputStream.write(requestBodyBytes);
                outputStream.flush();
            }
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
}
