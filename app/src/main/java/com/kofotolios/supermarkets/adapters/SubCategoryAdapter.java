package com.kofotolios.supermarkets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kofotolios.supermarkets.R;
import com.kofotolios.supermarkets.activities.LauncherActivity;
import com.kofotolios.supermarkets.models.SubCategory;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by Cooper on 21/7/2016.
 */

/**
 * 1. INFLATE CATEGORY LAYOUT
 * 2. BIND DATA TO GRIDVIEW
 */
public class SubCategoryAdapter extends BaseAdapter {
    Context c;
    ArrayList<SubCategory> subCategories;

    public SubCategoryAdapter(Context c, ArrayList<SubCategory> subCategories) {
        this.c = c;
        this.subCategories = subCategories;
    }

    @Override
    public int getCount() {
        return subCategories.size();
    }

    @Override
    public Object getItem(int i) {
        return subCategories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            view = LayoutInflater.from(c).inflate(R.layout.subcategory, viewGroup, false);
        }

        TextView tvSubCategoryName = (TextView) view.findViewById(R.id.tvSubCategoryName);
        ImageView ivSubCategoryPhoto = (ImageView) view.findViewById(R.id.ivSubCategoryPhoto);

        SubCategory subCategory = (SubCategory) this.getItem(i);

        tvSubCategoryName.setText(subCategory.getName());
        Picasso.with(c).load(LauncherActivity.globalImageUrl+subCategory.getPhoto()).into(ivSubCategoryPhoto);

        return view;
    }
}
