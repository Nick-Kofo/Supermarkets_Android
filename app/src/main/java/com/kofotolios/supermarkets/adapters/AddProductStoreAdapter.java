package com.kofotolios.supermarkets.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kofotolios.supermarkets.R;
import com.kofotolios.supermarkets.activities.CartActivity;
import com.kofotolios.supermarkets.activities.LauncherActivity;
import com.kofotolios.supermarkets.models.Product;
import com.kofotolios.supermarkets.models.Store;
import com.squareup.picasso.Picasso;


import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Cooper on 21/7/2016.
 */

/**
 * 1. INFLATE CATEGORY LAYOUT
 * 2. BIND DATA TO GRIDVIEW
 */
public class AddProductStoreAdapter extends BaseAdapter {
    Context c;
    ArrayList<Store> stores;

    public AddProductStoreAdapter(Context c, ArrayList<Store> stores) {
        this.c = c;
        this.stores = stores;
    }

    @Override
    public int getCount() {
        return stores.size();
    }

    @Override
    public Object getItem(int i) {
        return stores.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            view = LayoutInflater.from(c).inflate(R.layout.addproductstore, viewGroup, false);
        }

        TextView tvAddProductStoreDistance = (TextView) view.findViewById(R.id.tvAddProductStoreDistance);
        ImageView ivAddProductStorePhoto = (ImageView) view.findViewById(R.id.ivAddProductStorePhoto);

        Store store = (Store) this.getItem(i);

        tvAddProductStoreDistance.setText(Integer.toString(store.getDistance()) + " m");
        Picasso.with(c).load(LauncherActivity.globalImageUrl+store.getPhoto()).into(ivAddProductStorePhoto);

        return view;
    }
}
