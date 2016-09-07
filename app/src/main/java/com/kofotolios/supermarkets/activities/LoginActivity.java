package com.kofotolios.supermarkets.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kofotolios.supermarkets.R;
import com.kofotolios.supermarkets.network.JSONDownloader;
import com.kofotolios.supermarkets.network.JSONParser;


/**
 * Created by NK on 15/8/2016.
 */
public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText etLoginEmail, etLoginPassword;
    private Button btLogin;
    private TextView tvLinkRegister;
    private Context c;
    private String jsonURL = LauncherActivity.globalJsonURL + "login.php";
    private String loginEmail = "", loginPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        c = LoginActivity.this;

        initToolbar();
        initViews();


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginEmail = etLoginEmail.getText().toString();
                loginPassword= etLoginPassword.getText().toString();

                if(validate()){
                    //login
                    jsonURL = jsonURL +  "?email=" + loginEmail;
                    jsonURL = jsonURL +  "&password=" + loginPassword;

                    etLoginEmail.setText("");
                    etLoginPassword.setText("");

                    new JSONDownloader(LoginActivity.this, jsonURL, null, "login", null).execute();

                    jsonURL = LauncherActivity.globalJsonURL + "login.php";

                }else{
                    Snackbar.make(view, R.string.login_failed, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        tvLinkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, RegisterActivity.class);
                startActivity(i);
            }
        });


    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.login);
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
        etLoginEmail = (EditText) findViewById(R.id.etLoginEmail);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        tvLinkRegister = (TextView) findViewById(R.id.tvLinkRegister);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //Hides search action from toolbar
        menu.getItem(0).setVisible(false); // here pass the index of save menu item

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
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
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

    public boolean validate() {
        boolean valid = true;

        loginEmail = etLoginEmail.getText().toString();
        loginPassword= etLoginPassword.getText().toString();

        if (loginEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()) {
            etLoginEmail.setError(getString(R.string.invalid_email));
            valid = false;
        } else {
            etLoginEmail.setError(null);
        }

        if (loginPassword.isEmpty()) {
            etLoginPassword.setError(getString(R.string.invalid_password));
            valid = false;
        } else {
            etLoginPassword.setError(null);
        }

        return valid;
    }

}
