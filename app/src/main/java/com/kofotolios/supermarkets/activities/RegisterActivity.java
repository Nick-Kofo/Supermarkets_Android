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

/**
 * Created by NK on 15/8/2016.
 */
public class RegisterActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText etRegisterEmail, etRegisterPassword;
    private Button btRegister;
    private TextView tvLinkLogin;
    private Context c;
    private String jsonURL = LauncherActivity.globalJsonURL + "register.php";
    private String registerEmail, registerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        c = RegisterActivity.this;

        initToolbar();
        initViews();

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerEmail = etRegisterEmail.getText().toString();
                registerPassword= etRegisterPassword.getText().toString();

                if(validate()){
                    //register
                    jsonURL = jsonURL +  "?email=" + registerEmail;
                    jsonURL = jsonURL +  "&password=" + registerPassword;

                    etRegisterEmail.setText("");
                    etRegisterPassword.setText("");

                    new JSONDownloader(RegisterActivity.this, jsonURL, null, "register", null).execute();

                    jsonURL = LauncherActivity.globalJsonURL + "register.php";

                }else{
                    Snackbar.make(view, R.string.register_failed, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        tvLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, LoginActivity.class);
                startActivity(i);
            }
        });

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.register);
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
        etRegisterEmail = (EditText) findViewById(R.id.etRegisterEmail);
        etRegisterPassword = (EditText) findViewById(R.id.etRegisterPassword);
        btRegister = (Button) findViewById(R.id.btRegister);
        tvLinkLogin = (TextView) findViewById(R.id.tvLinkLogin);
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

        registerEmail = etRegisterEmail.getText().toString();
        registerPassword= etRegisterPassword.getText().toString();

        if (registerEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(registerEmail).matches()) {
            etRegisterEmail.setError(getString(R.string.invalid_email));
            valid = false;
        } else {
            etRegisterEmail.setError(null);
        }

        if (registerPassword.isEmpty()) {
            etRegisterPassword.setError(getString(R.string.invalid_password));
            valid = false;
        } else {
            etRegisterPassword.setError(null);
        }

        return valid;
    }

}
