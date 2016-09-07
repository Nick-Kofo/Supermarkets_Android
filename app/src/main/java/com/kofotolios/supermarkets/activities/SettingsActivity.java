package com.kofotolios.supermarkets.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kofotolios.supermarkets.R;

/**
 * Created by ΝΚ on 27/7/2016.
 */
public class SettingsActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private MaterialSpinner spDistance;
    private Button btContact, btAbout;
    String[] distanceOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initToolbar();
        initViews();
        populateSpinner();

        btContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "nick_kofo@yahoo.gr", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.contact_subject);
                intent.putExtra(Intent.EXTRA_TEXT, R.string.contact_message);
                startActivity(Intent.createChooser(intent, getString(R.string.choose_client)));
            }
        });

        btAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutWindow();
            }
        });

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.settings);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mToolbar.setNavigationIcon(R.drawable.ic_back_action);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {
        spDistance = (MaterialSpinner) findViewById(R.id.spDistance);
        btContact = (Button) findViewById(R.id.btContact);
        btAbout = (Button) findViewById(R.id.btAbout);
    }

    private void populateSpinner() {
        distanceOptions = getResources().getStringArray(R.array.distance_options);
        spDistance.setItems(distanceOptions);

        spDistance.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //store distance to preferences
                int maxDistance = 10000000;
                if (item.equalsIgnoreCase(distanceOptions[1])) {
                    maxDistance = 10000000; //No Limit;
                } else if (item.equalsIgnoreCase(distanceOptions[2])) {
                    maxDistance = 1000;
                } else if (item.equalsIgnoreCase(distanceOptions[3])) {
                    maxDistance = 5000;
                } else if (item.equalsIgnoreCase(distanceOptions[4])) {
                    maxDistance = 10000;
                }
                SharedPreferences.Editor editor = getSharedPreferences("superMarketsPreferences", MODE_PRIVATE).edit();
                editor.putInt("maxDistance", maxDistance);
                editor.apply();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //Hides search action from toolbar
        menu.getItem(0).setVisible(false); // here pass the index of save menu item

        return super.onPrepareOptionsMenu(menu);
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
            return true;
        }

        if (id == R.id.action_cart) {
            Intent i = new Intent(this, CartActivity.class);
            startActivity(i);
        }

        if(id == R.id.action_home) {
            Intent i = new Intent(this, LauncherActivity.class);
            finish();
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public void AboutWindow() {
        AlertDialog.Builder aboutWindow = new AlertDialog.Builder(this);
        final TextView tx = new TextView(this);
        tx.setTextColor(Color.BLACK);
        tx.setTextSize(15);

        aboutWindow.setIcon(R.mipmap.ic_launcher);
        aboutWindow.setTitle(R.string.about);
        aboutWindow.setMessage(R.string.about_message);
        aboutWindow.setView(tx);

        aboutWindow.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        aboutWindow.show();
    }
}
