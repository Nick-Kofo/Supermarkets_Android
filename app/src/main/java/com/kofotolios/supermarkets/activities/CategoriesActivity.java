package com.kofotolios.supermarkets.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kofotolios.supermarkets.R;
import com.kofotolios.supermarkets.adapters.CategoryAdapter;
import com.kofotolios.supermarkets.models.Category;
import com.kofotolios.supermarkets.network.JSONDownloader;
import com.kofotolios.supermarkets.network.JSONParser;

import java.util.ArrayList;

/**
 * Created by Cooper on 19/7/2016.
 */

public class CategoriesActivity extends AppCompatActivity {

    private Context c;
    private Toolbar mToolbar;
    private GridView gv;
    private String jsonURL = LauncherActivity.globalJsonURL + "categories.php";
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        c = getApplicationContext();

        initToolbar();
        initViews();

        new JSONDownloader(CategoriesActivity.this, jsonURL, gv, "categories", null).execute();

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Category category = (Category) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(c, SubCategoriesActivity.class);
                intent.putExtra("categoryId", category.getId());
                startActivity(intent);

            }
        });
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.categories);
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
        gv = (GridView) findViewById(R.id.gvCategories);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        //Search functionality
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();


                ArrayList<Category> all_categories = JSONParser.getCategoriesCopy();
                ArrayList<Category> filtered_categories = new ArrayList<Category>();

                for (Category c : all_categories) {
                    if (c.getName().toLowerCase().contains(query.toLowerCase())) {
                        filtered_categories.add(c);
                    }
                }

                CategoryAdapter adapter = new CategoryAdapter(c, filtered_categories);
                gv.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();

                ArrayList<Category> all_categories = JSONParser.getCategoriesCopy();
                ArrayList<Category> filtered_categories = new ArrayList<Category>();

                for (Category c : all_categories) {
                    if (c.getName().toLowerCase().contains(newText.toLowerCase())) {
                        filtered_categories.add(c);
                    }
                }

                CategoryAdapter adapter = new CategoryAdapter(c, filtered_categories);
                gv.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                return false;
            }
        });


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

        if(id == R.id.action_home) {
            Intent i = new Intent(this, LauncherActivity.class);
            finish();
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


}
