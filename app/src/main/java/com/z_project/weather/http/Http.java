package com.z_project.weather.http;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {


    protected String getUrlString(String urlSpec) throws IOException {

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseCode() + " : with " + urlSpec);
            }
            int bytesRed = 0;
            byte buffer[] = new byte[1024];
            while ((bytesRed = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRed);
            }
            outputStream.close();
            return new String(outputStream.toByteArray());

        } finally {
            connection.disconnect();
        }
    }
}
