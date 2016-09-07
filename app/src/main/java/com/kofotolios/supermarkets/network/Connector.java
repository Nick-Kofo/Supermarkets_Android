package com.kofotolios.supermarkets.network;

/**
 * Created by Cooper on 21/7/2016.
 */

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 1. SET CONNECTION PROPERTIES
 * 2. ESTABLISH AND RETURN CONNECTION OBJECT
 */

public class Connector {

    public static Object connect(String jsonURL) {
        try {
            URL url = new URL(jsonURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //CON PROPERTIES
            con.setRequestMethod("GET");
            con.setConnectTimeout(15000);
            con.setReadTimeout(15000);
            con.setDoInput(true);

            return con;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error " + e.getMessage();

        } catch (IOException e) {
            e.printStackTrace();
            return "Error " + e.getMessage();
        }
    }
}
