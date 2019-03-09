package com.example.sirishaa.todo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;

import com.example.sirishaa.todo.Adapters.CategoriesGridAdapter;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {

    GridView gridView;
    CategoriesGridAdapter categoriesGridAdapter;
    ArrayList<String> categoryList;
    TextView hiTv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        hiTv = findViewById(R.id.hi_tv);
        Typeface heavy_tf = Typeface.createFromAsset(getAssets(), "fonts/avenir_heavy.ttf");
        hiTv.setTypeface(heavy_tf);

        gridView = findViewById(R.id.cat_grid_view);


        categoryList = new ArrayList<>();
        categoryList.add("Work");
        categoryList.add("Personal");
        categoryList.add("Shopping");
        // Instance of ImageAdapter Class

        categoriesGridAdapter = new CategoriesGridAdapter(this, categoryList);
        gridView.setAdapter(categoriesGridAdapter);


    }
}