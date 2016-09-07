package com.kofotolios.supermarkets.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kofotolios.supermarkets.R;
import com.kofotolios.supermarkets.network.JSONDownloader;


/**
 * Created by NK on 6/8/2016.
 */
public class LauncherActivity extends AppCompatActivity {

    //    global strings to connect server - emulator
    public static final String globalJsonURL = "http://10.0.3.2:8888/supermarkets_prices_comparison/api/";
    public static final String globalImageUrl = "http://10.0.3.2:8888";
//    global strings to connect server - real device
//    public static final String globalJsonURL = "http://192.168.1.100:8888/supermarkets_prices_comparison/api/";
//    public static final String globalImageUrl = "http://192.168.1.100:8888";

    private Toolbar mToolbar;
    private Button bCategoriesLaunch, bScannerLaunch;
    private String jsonURL = globalJsonURL + "barcodes.php";
    Context c;
    public static String barcode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        c = LauncherActivity.this;

        initToolbar();
        initViews();

        bCategoriesLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LauncherActivity.this, CategoriesActivity.class);
                startActivity(i);
            }
        });

        bScannerLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(LauncherActivity.this).initiateScan();
            }
        });

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.best_price);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initViews() {
        bCategoriesLaunch = (Button) findViewById(R.id.bCategoriesLaunch);
        bScannerLaunch = (Button) findViewById(R.id.bScannerLaunch);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //Hides search action from toolbar
        menu.getItem(0).setVisible(false); // here pass the index of save menu item

        //Hides home action from toolbar
        menu.getItem(1).setVisible(false); // here pass the index of save menu item

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_account) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        if (id == R.id.action_cart) {
            Intent i = new Intent(this, CartActivity.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

    // Get the scanner results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {

                Toast.makeText(c, R.string.barcode_not_found, Toast.LENGTH_SHORT).show();

            } else {
                barcode = result.getContents();
                jsonURL = jsonURL + "?barcode=" + barcode;

                new JSONDownloader(LauncherActivity.this, jsonURL, null, "barcodes", null).execute();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
