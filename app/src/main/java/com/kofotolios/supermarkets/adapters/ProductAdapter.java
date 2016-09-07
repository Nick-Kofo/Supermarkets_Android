package com.kofotolios.supermarkets.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kofotolios.supermarkets.R;
import com.kofotolios.supermarkets.activities.LauncherActivity;
import com.kofotolios.supermarkets.activities.ProductsInStoresByDistanceActivity;
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
public class ProductAdapter extends BaseAdapter {
    Context c;
    ArrayList<Product> products;

    public ProductAdapter(Context c, ArrayList<Product> products) {
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

        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.product, viewGroup, false);
        }

        TextView tvProductName = (TextView) view.findViewById(R.id.tvProductName);
        TextView tvProductMinPrice = (TextView) view.findViewById(R.id.tvProductMinPrice);
        ImageView ivProductPhoto = (ImageView) view.findViewById(R.id.ivProductPhoto);
        FloatingActionButton fabProductStore = (FloatingActionButton) view.findViewById(R.id.fabProductStore);

        fabProductStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = (Product) ProductAdapter.this.getItem(i);

                Intent intent = new Intent(c, ProductsInStoresByDistanceActivity.class);
                intent.putExtra("productId", product.getId());
                c.startActivity(intent);
            }
        });


        Product product = (Product) this.getItem(i);

        tvProductName.setText(product.getName());
        tvProductMinPrice.setText(Double.toString(product.getMinPrice()));
        Picasso.with(c).load(LauncherActivity.globalImageUrl + product.getPhoto()).into(ivProductPhoto);

        return view;
    }
}
