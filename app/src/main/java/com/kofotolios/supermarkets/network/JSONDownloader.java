package com.kofotolios.supermarkets.network;

/**
 * Created by Cooper on 21/7/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.GridView;
import android.widget.Toast;

import com.kofotolios.supermarkets.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;


/**
 * 1. CONNECT TO NETWORK
 * 2. DOWNLOAD DATA IN BACKGROUND THREAD
 * 3. SEND THE DATA TO PARSER TO BE PARSED
 */
public class JSONDownloader extends AsyncTask<Void, Void, String> {

    private Context c;
    private String jsonURL;
    private GridView gv;
    private String callingActivity;
    private Location userLocation;

    private ProgressDialog pd;

    public JSONDownloader(Context c, String jsonURL, GridView gv, String callingActivity, Location userLocation) {
        this.c = c;
        this.jsonURL = jsonURL;
        this.gv = gv;
        this.callingActivity = callingActivity;
        this.userLocation = userLocation;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //Dialog
        pd = new ProgressDialog(c);
        pd.setTitle(R.string.connection);
        pd.setMessage(c.getString(R.string.downloading));
        pd.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return download();
    }

    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);

        pd.dismiss();

        if (jsonData.startsWith("Error")) {
            String error = jsonData;
            Toast.makeText(c, error, Toast.LENGTH_SHORT).show(); //TODO Snackbar

        } else {
            //PARSE DATA
            new JSONParser(c, jsonData, gv, callingActivity, userLocation).execute();
        }
    }

    private String download() {

        Object connection = Connector.connect(jsonURL);
        if (connection.toString().startsWith("Error")) {
            return connection.toString();
        }

        try {
            HttpURLConnection con = (HttpURLConnection) connection;
            if (con.getResponseCode() == con.HTTP_OK) {
                //GET INPUT FROM STREAM
                InputStream is = new BufferedInputStream(con.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String line;
                StringBuffer jsonData = new StringBuffer();

                //READ
                while ((line = br.readLine()) != null) {
                    jsonData.append(line + "\n");
                }

                //CLOSE RESOURCES
                br.close();
                is.close();

                //RETURN JSON
                return jsonData.toString();
            } else {
                return "Error " + con.getResponseMessage();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Error " + e.getMessage();
        }
    }
}
