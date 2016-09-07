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
import com.kofotolios.supermarkets.models.Category;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by Cooper on 21/7/2016.
 */

/**
 * 1. INFLATE CATEGORY LAYOUT
 * 2. BIND DATA TO GRIDVIEW
 */
public class CategoryAdapter extends BaseAdapter {
    Context c;
    ArrayList<Category> categories;

    public CategoryAdapter(Context c, ArrayList<Category> categories) {
        this.c = c;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            view = LayoutInflater.from(c).inflate(R.layout.category, viewGroup, false);
        }

        TextView tvCategoryName = (TextView) view.findViewById(R.id.tvCategoryName);
        ImageView ivCategoryPhoto = (ImageView) view.findViewById(R.id.ivCategoryPhoto);

        Category category = (Category) this.getItem(i);

        tvCategoryName.setText(category.getName());
        Picasso.with(c).load(LauncherActivity.globalImageUrl+category.getPhoto()).into(ivCategoryPhoto);

        return view;
    }
}
