package com.kofotolios.supermarkets.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kofotolios.supermarkets.R;
import com.kofotolios.supermarkets.network.JSONDownloader;
import com.kofotolios.supermarkets.network.JSONParser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by NK on 16/8/2016.
 */
public class AddProductPhotoActivity extends AppCompatActivity {

    private Context c;
    private Toolbar mToolbar;
    private ImageView ivProductImage;
    private Button btAddProductToStore;
    String productName, productDescription, barcode, encoded_string;
    int storeId, categoryParent, categoryLevel;
    Double productPrice;
    private static final int TAKE_PICTURE = 1;
    private Uri fileUri, selectedImagePath;
    private Bitmap bitmap;
    private File file;
    ProgressDialog pd;
    private String jsonURL = LauncherActivity.globalJsonURL + "postProduct.php";
    private String barcodeURL = LauncherActivity.globalJsonURL + "barcodes.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_photo);
        c = AddProductPhotoActivity.this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            storeId = extras.getInt("storeId");
            productName = extras.getString("productName");
            productPrice = extras.getDouble("productPrice");
            productDescription = extras.getString("productDescription");
            categoryParent = extras.getInt("categoryParent");
            categoryLevel = extras.getInt("categoryLevel");
            barcode = extras.getString("barcode");
        }

        initToolbar();
        initViews();

        ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(view);
            }
        });

        btAddProductToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap == null) {
                    Snackbar.make(view, R.string.capture_photo_of_product, Snackbar.LENGTH_SHORT).show();
                } else {
                    //Post product
                    new encodeImage().execute();
                }
            }
        });

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.product_photo);
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
        ivProductImage = (ImageView) findViewById(R.id.ivAddProductPhoto);
        btAddProductToStore = (Button) findViewById(R.id.btAddProductToStore);
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


    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(), productName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        fileUri = Uri.fromFile(file);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == this.RESULT_OK) {
                    selectedImagePath = fileUri;
                    getContentResolver().notifyChange(selectedImagePath, null);
                    ContentResolver cr = getContentResolver();
                    try {

                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImagePath);

                        //scale down image to fit screen for real devices
                        int nh = (int) (bitmap.getHeight() * (800.0 / bitmap.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 600, nh, true);

//                        ivProductImage.setImageBitmap(scaled);
                        Picasso.with(c).load(selectedImagePath).fit().centerCrop().into(ivProductImage);

                    } catch (Exception e) {
                        Toast.makeText(this, R.string.failed_to_load_photo, Toast.LENGTH_SHORT).show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
    }

    private class encodeImage extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Dialog
            pd = new ProgressDialog(c);
            pd.setTitle(R.string.connection);
            pd.setMessage(c.getString(R.string.encoding_image));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            bitmap = decodeImageFile(file, 1280, 720);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);

            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array, 0);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pd.dismiss();

            makeRequest();
        }
    }

    private void makeRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, jsonURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(c, R.string.product_added, Toast.LENGTH_SHORT).show();

                        barcodeURL = barcodeURL + "?barcode=" + barcode;
                        new JSONDownloader(c, barcodeURL, null, "barcodes", null).execute();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("encoded_string", encoded_string);
                map.put("productName", productName);
                map.put("productPrice", productPrice.toString());
                map.put("productDescription", productDescription);
                map.put("barcode", barcode);
                map.put("categoryParent", String.valueOf(categoryParent));
                map.put("categoryLevel", String.valueOf(categoryLevel));
                map.put("storeId", String.valueOf(storeId));

                return map;
            }
        };
        requestQueue.add(request);
    }


    public static Bitmap decodeImageFile(File f,int WIDTH,int HIGHT){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH=WIDTH;
            final int REQUIRED_HIGHT=HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
}



