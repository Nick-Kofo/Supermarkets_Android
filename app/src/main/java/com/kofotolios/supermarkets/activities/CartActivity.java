package com.kofotolios.supermarkets.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.kofotolios.supermarkets.R;
import com.kofotolios.supermarkets.adapters.CartAdapter;
import com.kofotolios.supermarkets.adapters.ProductInStoreByDistanceAdapter;
import com.kofotolios.supermarkets.adapters.ProductInStoreByPriceAdapter;
import com.kofotolios.supermarkets.models.Product;

import java.util.ArrayList;

/**
 * Created by NK on 9/8/2016.
 */
public class CartActivity extends AppCompatActivity {

    private Context c;
    private Toolbar mToolbar;
    public GridView gv;
    Double totalCartPrice = 0.0;
    String totalPrice = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        c = getApplicationContext();

        //total cart price
        for (Product cartProductByDistance : ProductInStoreByDistanceAdapter.cartProductsByDistance) {
            totalCartPrice += cartProductByDistance.getPriceInStore();
        }

        for (Product cartProductByPrice : ProductInStoreByPriceAdapter.cartProductsByPrice) {
            totalCartPrice += cartProductByPrice.getPriceInStore();
        }

        totalPrice = String.format("%.2f", totalCartPrice);


        initToolbar();
        initViews();

        new populate(CartActivity.this, gv).execute();

    }


    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.action_cart);
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
        gv = (GridView) findViewById(R.id.gvCart);
        TextView totalCartPrice = (TextView) findViewById(R.id.tvTotalCartPrice);
        totalCartPrice.setText(getResources().getString(R.string.total_price) + totalPrice);
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
        if (id == R.id.action_account) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        if (id == R.id.action_home) {
            Intent i = new Intent(this, LauncherActivity.class);
            finish();
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    public class populate extends AsyncTask<Void, Void, Boolean> {

        Context c;
        GridView gv;
        ArrayList<Product> cartProducts = new ArrayList<>();

        ProgressDialog pd;

        public populate(Context c, GridView gv) {
            this.c = c;
            this.gv = gv;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Dialog
            pd = new ProgressDialog(c);
            pd.setTitle(R.string.action_cart);
            pd.setMessage(c.getString(R.string.loading_cart));
            pd.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return true;
        }

        @Override
        protected void onPostExecute(Boolean check) {
            super.onPostExecute(check);

            pd.dismiss();

            if (check) {
                //populate adapter
                if (ProductInStoreByDistanceAdapter.cartProductsByDistance != null || ProductInStoreByPriceAdapter.cartProductsByPrice != null) {

                    if (ProductInStoreByDistanceAdapter.cartProductsByDistance != null) {
                        cartProducts.addAll(ProductInStoreByDistanceAdapter.cartProductsByDistance);
                    }

                    if (ProductInStoreByPriceAdapter.cartProductsByPrice != null) {
                        cartProducts.addAll(ProductInStoreByPriceAdapter.cartProductsByPrice);
                    }


                    CartAdapter adapter = new CartAdapter(c, cartProducts);
                    gv.setAdapter(adapter);
                } else {
                    Toast.makeText(c, R.string.cart_empty, Toast.LENGTH_LONG).show(); //TODO Snackbar
                }
            } else {
                Toast.makeText(c, R.string.parsing_error, Toast.LENGTH_SHORT).show(); //TODO Snackbar
            }

        }
    }

}
