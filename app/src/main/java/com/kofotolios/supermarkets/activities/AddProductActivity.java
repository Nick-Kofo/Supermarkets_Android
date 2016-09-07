package com.kofotolios.supermarkets.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kofotolios.supermarkets.R;
import com.kofotolios.supermarkets.network.JSONDownloader;
import com.kofotolios.supermarkets.network.JSONParser;


/**
 * Created by NK on 15/8/2016.
 */
public class AddProductActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText etAddProductName, etAddProductPrice, etAddProductDescription;
    private Button btAddProductNext;
    private MaterialSpinner spCategoryParent, spCategoryLevel;
    String[] categoryParentOptions, categoryLevelOptions1, categoryLevelOptions2, categoryLevelOptions3, categoryLevelOptions4, categoryLevelOptions5;
    private Context c;
    private String productName, productDescription;
    private Double productPrice;
    private int categoryParent = 0, categoryLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        c = AddProductActivity.this;

        initToolbar();
        initViews();
        populateSpinner();

        btAddProductNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                productName = etAddProductName.getText().toString();
                productPrice = Double.valueOf(etAddProductPrice.getText().toString());
                productDescription = etAddProductDescription.getText().toString();



                if (validate()) {
                    Intent i = new Intent(c, AddProductStoreActivity.class);
                    i.putExtra("productName", productName);
                    i.putExtra("productPrice", productPrice);
                    i.putExtra("productDescription", productDescription);
                    i.putExtra("categoryParent", categoryParent);
                    i.putExtra("categoryLevel", categoryLevel);
                    i.putExtra("barcode", LauncherActivity.barcode);
                    startActivity(i);
                } else {
                    Snackbar.make(view, R.string.fill_all_fields, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.add_product);
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
        etAddProductName = (EditText) findViewById(R.id.etAddProductName);
        etAddProductPrice = (EditText) findViewById(R.id.etAddProductPrice);
        etAddProductDescription = (EditText) findViewById(R.id.etAddProductDescription);
        btAddProductNext = (Button) findViewById(R.id.btAddProductNext);
        spCategoryParent = (MaterialSpinner) findViewById(R.id.spCategoryParent);
        spCategoryLevel = (MaterialSpinner) findViewById(R.id.spCategoryLevel);
    }

    private void populateSpinner() {
        categoryParentOptions = getResources().getStringArray(R.array.categoryParent_options);
        categoryLevelOptions1 = getResources().getStringArray(R.array.categoryLevel1_options);
        categoryLevelOptions2 = getResources().getStringArray(R.array.categoryLevel2_options);
        categoryLevelOptions3 = getResources().getStringArray(R.array.categoryLevel3_options);
        categoryLevelOptions4 = getResources().getStringArray(R.array.categoryLevel4_options);
        categoryLevelOptions5 = getResources().getStringArray(R.array.categoryLevel5_options);

        spCategoryParent.setItems(categoryParentOptions);

        spCategoryParent.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                if (item.equalsIgnoreCase(categoryParentOptions[1])) {
                    categoryParent = 1;
                    spCategoryLevel.setItems(categoryLevelOptions1);

                    spCategoryLevel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                            if (item.equalsIgnoreCase(categoryLevelOptions1[1])) {
                                categoryLevel = 1;
                            } else if (item.equalsIgnoreCase(categoryLevelOptions1[2])) {
                                categoryLevel = 2;
                            } else if (item.equalsIgnoreCase(categoryLevelOptions1[3])) {
                                categoryLevel = 3;
                            } else if (item.equalsIgnoreCase(categoryLevelOptions1[4])) {
                                categoryLevel = 4;
                            }
                        }
                    });


                } else if (item.equalsIgnoreCase(categoryParentOptions[2])) {
                    categoryParent = 2;
                    spCategoryLevel.setItems(categoryLevelOptions2);

                    spCategoryLevel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                            if (item.equalsIgnoreCase(categoryLevelOptions2[1])) {
                                categoryLevel = 1;
                            } else if (item.equalsIgnoreCase(categoryLevelOptions2[2])) {
                                categoryLevel = 2;
                            }
                        }
                    });

                } else if (item.equalsIgnoreCase(categoryParentOptions[3])) {
                    categoryParent = 3;
                    spCategoryLevel.setItems(categoryLevelOptions3);

                    spCategoryLevel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                            if (item.equalsIgnoreCase(categoryLevelOptions3[1])) {
                                categoryLevel = 1;
                            } else if (item.equalsIgnoreCase(categoryLevelOptions3[2])) {
                                categoryLevel = 2;
                            }
                        }
                    });

                } else if (item.equalsIgnoreCase(categoryParentOptions[4])) {
                    categoryParent = 4;
                    spCategoryLevel.setItems(categoryLevelOptions4);

                    spCategoryLevel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                            if (item.equalsIgnoreCase(categoryLevelOptions4[1])) {
                                categoryLevel = 1;
                            } else if (item.equalsIgnoreCase(categoryLevelOptions4[2])) {
                                categoryLevel = 2;
                            }
                        }
                    });

                } else if (item.equalsIgnoreCase(categoryParentOptions[5])) {
                    categoryParent = 5;
                    spCategoryLevel.setItems(categoryLevelOptions5);

                    spCategoryLevel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                            if (item.equalsIgnoreCase(categoryLevelOptions5[1])) {
                                categoryLevel = 1;
                            } else if (item.equalsIgnoreCase(categoryLevelOptions5[2])) {
                                categoryLevel = 2;
                            }
                        }
                    });
                }
            }
        });
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

        productName = etAddProductName.getText().toString();
        productPrice = Double.valueOf(etAddProductPrice.getText().toString());
        productDescription = etAddProductDescription.getText().toString();

        if (productName.isEmpty()) {
            etAddProductName.setError(getString(R.string.fill_name));
            valid = false;
        } else {
            etAddProductName.setError(null);
        }

        if (productPrice == null) {
            etAddProductPrice.setError(getString(R.string.fill_price));
            valid = false;
        } else {
            etAddProductPrice.setError(null);
        }

        if (productDescription.isEmpty()) {
            etAddProductDescription.setError(getString(R.string.fill_description));
            valid = false;
        } else {
            etAddProductDescription.setError(null);
        }

        if (categoryParent == 0) {
            spCategoryParent.setError(getString(R.string.fill_category));
            valid = false;
        } else {
            spCategoryParent.setError(null);
        }

        if (categoryLevel == 0) {
            spCategoryLevel.setError(getString(R.string.fill_sub_category));
            valid = false;
        } else {
            spCategoryLevel.setError(null);
        }

        return valid;
    }

}
