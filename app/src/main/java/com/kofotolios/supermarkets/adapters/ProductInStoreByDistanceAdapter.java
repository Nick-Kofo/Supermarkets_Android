package com.kofotolios.supermarkets.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kofotolios.supermarkets.R;
import com.kofotolios.supermarkets.activities.LauncherActivity;
import com.kofotolios.supermarkets.models.Product;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by Cooper on 21/7/2016.
 */

/**
 * 1. INFLATE CATEGORY LAYOUT
 * 2. BIND DATA TO GRIDVIEW
 */
public class ProductInStoreByDistanceAdapter extends BaseAdapter {
    Context c;
    ArrayList<Product> products;
    public static ArrayList<Product> cartProductsByDistance = new ArrayList<>();

    public ProductInStoreByDistanceAdapter(Context c, ArrayList<Product> products) {
        this.c = c;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            view = LayoutInflater.from(c).inflate(R.layout.productinstorebydistance, viewGroup, false);
        }

        TextView tvProductName = (TextView) view.findViewById(R.id.tvProductInStoreName);
        TextView tvProductInStorePrice = (TextView) view.findViewById(R.id.tvProductInStorePrice);
        TextView tvStoreDistance = (TextView) view.findViewById(R.id.tvStoreDistance);
        ImageView ivStorePhoto = (ImageView) view.findViewById(R.id.ivStorePhoto);
        FloatingActionButton fabCart = (FloatingActionButton) view.findViewById(R.id.fabCart);

        fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add product to cart
                Product product = (Product) ProductInStoreByDistanceAdapter.this.getItem(i);
                cartProductsByDistance.add(product);

                //colored snackbar
                Snackbar snackbar;
                snackbar = Snackbar.make(view, R.string.added_to_cart, Snackbar.LENGTH_SHORT);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#00e676"));
                snackbar.show();

            }
        });

        Product product = (Product) this.getItem(i);

        tvProductName.setText(product.getName());
        tvProductInStorePrice.setText(Double.toString(product.getPriceInStore()));
        tvStoreDistance.setText(Integer.toString(product.getDistance()) + " m");
        Picasso.with(c).load(LauncherActivity.globalImageUrl+product.getStorePhoto()).into(ivStorePhoto);

        return view;
    }
}
