package com.kofotolios.supermarkets.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.GridView;
import android.widget.Toast;

import com.kofotolios.supermarkets.R;
import com.kofotolios.supermarkets.activities.AccountActivity;
import com.kofotolios.supermarkets.activities.AddProductActivity;
import com.kofotolios.supermarkets.activities.ProductsInStoresByDistanceActivity;
import com.kofotolios.supermarkets.adapters.AddProductStoreAdapter;
import com.kofotolios.supermarkets.adapters.CategoryAdapter;
import com.kofotolios.supermarkets.adapters.ProductAdapter;
import com.kofotolios.supermarkets.adapters.ProductInStoreByDistanceAdapter;
import com.kofotolios.supermarkets.adapters.ProductInStoreByPriceAdapter;
import com.kofotolios.supermarkets.adapters.SubCategoryAdapter;
import com.kofotolios.supermarkets.models.Category;
import com.kofotolios.supermarkets.models.Product;
import com.kofotolios.supermarkets.models.Store;
import com.kofotolios.supermarkets.models.SubCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by Cooper on 21/7/2016.
 */
public class JSONParser extends AsyncTask<Void, Void, Boolean> {

    Context c;
    String jsonData;
    GridView gv;
    String callingActivity;
    Location userLocation;

    ProgressDialog pd;
    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<SubCategory> subCategories = new ArrayList<>();
    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Store> stores = new ArrayList<>();

    //copies used for search
    static ArrayList<Category> categoriesCopy = new ArrayList<>();
    static ArrayList<SubCategory> subCategoriesCopy = new ArrayList<>();
    static ArrayList<Product> productsCopy = new ArrayList<>();
    static ArrayList<Store> storesCopy = new ArrayList<>();


    String loginResult = "", registerResult = "";
    public static String userEmail = "";
    int productId = 0;


    public JSONParser(Context c, String jsonData, GridView gv, String callingActivity, Location userLocation) {
        this.c = c;
        this.jsonData = jsonData;
        this.gv = gv;
        this.callingActivity = callingActivity;
        this.userLocation = userLocation;
    }

    public static ArrayList<Category> getCategoriesCopy() {
        return categoriesCopy;
    }

    public static ArrayList<SubCategory> getSubCategoriesCopy() {
        return subCategoriesCopy;
    }

    public static ArrayList<Product> getProductsCopy() {
        return productsCopy;
    }

    public static ArrayList<Store> getStoresCopy() {
        return storesCopy;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //Dialog
        pd = new ProgressDialog(c);
        pd.setTitle(R.string.connection);
        pd.setMessage(c.getString(R.string.parsing));
        pd.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return parse(callingActivity);
    }

    @Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);

        pd.dismiss();

        if (isParsed) {
            //BIND DATA
            if (callingActivity.equalsIgnoreCase("categories")) {
                CategoryAdapter adapter = new CategoryAdapter(c, categories);
                gv.setAdapter(adapter);
            } else if (callingActivity.equalsIgnoreCase("subCategories")) {
                SubCategoryAdapter adapter = new SubCategoryAdapter(c, subCategories);
                gv.setAdapter(adapter);
            } else if (callingActivity.equalsIgnoreCase("products")) {
                ProductAdapter adapter = new ProductAdapter(c, products);
                gv.setAdapter(adapter);
            } else if (callingActivity.equalsIgnoreCase("productsInStoresByDistance")) {
                //sort by distance
                Collections.sort(products, new Comparator<Product>() {
                    @Override
                    public int compare(Product p1, Product p2) {
                        return (p1.distance < p2.distance) ? -1 : (p1.distance > p2.distance) ? 1 : 0;
                    }
                });

                ProductInStoreByDistanceAdapter adapter = new ProductInStoreByDistanceAdapter(c, products);
                gv.setAdapter(adapter);
            } else if (callingActivity.equalsIgnoreCase("productsInStoresByPrice")) {
                ProductInStoreByPriceAdapter adapter = new ProductInStoreByPriceAdapter(c, products);
                gv.setAdapter(adapter);
            } else if (callingActivity.equalsIgnoreCase("login")) {

                if (loginResult.equalsIgnoreCase("true")) {
                    Intent i = new Intent(c.getApplicationContext(), AccountActivity.class);
                    i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
                    c.getApplicationContext().startActivity(i);
                }
            } else if (callingActivity.equalsIgnoreCase("register")) {

                if (registerResult.equalsIgnoreCase("true")) {
                    Intent i = new Intent(c.getApplicationContext(), AccountActivity.class);
                    i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
                    c.getApplicationContext().startActivity(i);
                }
            } else if (callingActivity.equalsIgnoreCase("barcodes")) {

                if (productId != 0) {
                    Intent i = new Intent(c.getApplicationContext(), ProductsInStoresByDistanceActivity.class);
                    i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("productId", productId);
                    c.getApplicationContext().startActivity(i);
                } else {

                    Toast.makeText(c, R.string.barcode_not_found, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(c);
                    builder1.setCancelable(false);
                    builder1.setTitle(R.string.barcode_not_found);
                    builder1.setMessage(R.string.barcode_dialog_message);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(c.getApplicationContext(), AddProductActivity.class);
                                    i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
                                    c.getApplicationContext().startActivity(i);
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            } else if (callingActivity.equalsIgnoreCase("stores")) {
                //sort by distance
                Collections.sort(stores, new Comparator<Store>() {
                    @Override
                    public int compare(Store s1, Store s2) {
                        return (s1.distance < s2.distance) ? -1 : (s1.distance > s2.distance) ? 1 : 0;
                    }
                });

                AddProductStoreAdapter adapter = new AddProductStoreAdapter(c, stores);
                gv.setAdapter(adapter);

            }


        } else {
            Toast.makeText(c, R.string.parsing_error, Toast.LENGTH_SHORT).show(); //TODO Snackbar

        }

    }

    private Boolean parse(String callingActivity) {

        try {
            //Json me title
            JSONObject jo = new JSONObject(jsonData);

            if (callingActivity.equalsIgnoreCase("categories")) {
                JSONArray ja = jo.getJSONArray("categories");

                categories.clear();
                Category category;

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    int id = jo.getInt("id");
                    String name = jo.getString("name");
                    String photo = jo.getString("photo");

                    category = new Category();

                    category.setId(id);
                    category.setName(name);
                    category.setPhoto(photo);


                    categories.add(category);
                }
                categoriesCopy = categories;
            } else if (callingActivity.equalsIgnoreCase("subCategories")) {
                JSONArray ja = jo.getJSONArray("subCategories");

                subCategories.clear();
                SubCategory subCategory;

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    String name = jo.getString("name");
                    String photo = jo.getString("photo");
                    int parent = jo.getInt("parent");
                    int level = jo.getInt("level");

                    subCategory = new SubCategory();

                    subCategory.setName(name);
                    subCategory.setPhoto(photo);
                    subCategory.setParent(parent);
                    subCategory.setLevel(level);

                    subCategories.add(subCategory);
                }
                subCategoriesCopy = subCategories;
            } else if (callingActivity.equalsIgnoreCase("products")) {
                JSONArray ja = jo.getJSONArray("products");

                products.clear();
                Product product;

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    int id = jo.getInt("id");
                    String name = jo.getString("name");
                    Double minPrice = jo.getDouble("minPrice");
                    String photo = jo.getString("photo");

                    product = new Product();

                    product.setId(id);
                    product.setName(name);
                    product.setMinPrice(minPrice);
                    product.setPhoto(photo);

                    products.add(product);
                }
                productsCopy = products;
            } else if (callingActivity.equalsIgnoreCase("productsInStoresByDistance")) {
                JSONArray ja = jo.getJSONArray("productsInStores");

                products.clear();
                Product product;

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    String name = jo.getString("name");
                    Double priceInStore = jo.getDouble("priceInStore");
                    String storePhoto = jo.getString("storePhoto");
                    Double storeLatitude = jo.getDouble("storeLatitude");
                    Double storeLongitude = jo.getDouble("storeLongitude");

                    //calculate distance
                    Location storeLocation = new Location("storeLocation");
                    storeLocation.setLatitude(storeLatitude);
                    storeLocation.setLongitude(storeLongitude);
                    Float distance = userLocation.distanceTo(storeLocation);
                    int distanceMeters = Math.round(distance * 100) / 100;

                    //get maxDistance
                    SharedPreferences prefs = c.getSharedPreferences("superMarketsPreferences", Context.MODE_PRIVATE);
                    int maxDistance = prefs.getInt("maxDistance", 10000000);

                    if (distanceMeters <= maxDistance) {
                        product = new Product();

                        product.setName(name);
                        product.setPriceInStore(priceInStore);
                        product.setStorePhoto(storePhoto);
                        product.setStoreLatitude(storeLatitude);
                        product.setStoreLongitude(storeLongitude);
                        product.setDistance(distanceMeters);

                        products.add(product);
                    }
                    productsCopy = products;
                }

            } else if (callingActivity.equalsIgnoreCase("productsInStoresByPrice")) {
                JSONArray ja = jo.getJSONArray("productsInStores");

                products.clear();
                Product product;

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    String name = jo.getString("name");
                    Double priceInStore = jo.getDouble("priceInStore");
                    String storePhoto = jo.getString("storePhoto");
                    Double storeLatitude = jo.getDouble("storeLatitude");
                    Double storeLongitude = jo.getDouble("storeLongitude");

                    //calculate distance
                    Location storeLocation = new Location("storeLocation");
                    storeLocation.setLatitude(storeLatitude);
                    storeLocation.setLongitude(storeLongitude);
                    Float distance = userLocation.distanceTo(storeLocation);
                    int distanceMeters = Math.round(distance * 100) / 100;

                    //get maxDistance
                    SharedPreferences prefs = c.getSharedPreferences("superMarketsPreferences", Context.MODE_PRIVATE);
                    int maxDistance = prefs.getInt("maxDistance", 10000000);

                    if (distanceMeters <= maxDistance) {
                        product = new Product();

                        product.setName(name);
                        product.setPriceInStore(priceInStore);
                        product.setStorePhoto(storePhoto);
                        product.setStoreLatitude(storeLatitude);
                        product.setStoreLongitude(storeLongitude);
                        product.setDistance(distanceMeters);

                        products.add(product);
                    }
                    productsCopy = products;
                }

            } else if (callingActivity.equalsIgnoreCase("login")) {
                JSONArray ja = jo.getJSONArray("login");

                loginResult = "";
                registerResult = "";
                userEmail = "";


                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    loginResult = jo.getString("loginResult");
                    userEmail = jo.getString("userEmail");
                }
            } else if (callingActivity.equalsIgnoreCase("register")) {
                JSONArray ja = jo.getJSONArray("register");

                loginResult = "";
                registerResult = "";
                userEmail = "";

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    registerResult = jo.getString("registerResult");
                    userEmail = jo.getString("userEmail");
                }
            } else if (callingActivity.equalsIgnoreCase("barcodes")) {
                JSONArray ja = jo.getJSONArray("barcodes");

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    productId = jo.getInt("id");
                }
            } else if (callingActivity.equalsIgnoreCase("stores")) {
                JSONArray ja = jo.getJSONArray("stores");

                stores.clear();
                Store store;

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    int id = jo.getInt("id");
                    String storePhoto = jo.getString("storePhoto");
                    Double latitude = jo.getDouble("latitude");
                    Double longitude = jo.getDouble("longitude");

                    //calculate distance
                    Location storeLocation = new Location("storeLocation");
                    storeLocation.setLatitude(latitude);
                    storeLocation.setLongitude(longitude);
                    Float distance = userLocation.distanceTo(storeLocation);
                    int distanceMeters = Math.round(distance * 100) / 100;

                    //get maxDistance
                    SharedPreferences prefs = c.getSharedPreferences("superMarketsPreferences", Context.MODE_PRIVATE);
                    int maxDistance = prefs.getInt("maxDistance", 10000000);

                    if (distanceMeters <= maxDistance) {
                        store = new Store();

                        store.setId(id);
                        store.setPhoto(storePhoto);
                        store.setLatitude(latitude);
                        store.setLongitude(longitude);
                        store.setDistance(distanceMeters);

                        stores.add(store);
                    }
                    storesCopy = stores;
                }
            }


            return true;

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

}
