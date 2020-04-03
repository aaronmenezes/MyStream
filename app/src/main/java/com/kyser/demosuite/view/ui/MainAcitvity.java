package com.kyser.demosuite.view.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kyser.demosuite.R;
import com.kyser.demosuite.service.model.CategoryModel;
import com.kyser.demosuite.view.ui.adaptor.CategoryListAdaptor;
import com.kyser.demosuite.view.ui.components.SpaceItemDecoration;
import com.kyser.demosuite.viewmodel.CategoryListModel;

import java.io.Serializable;
import java.util.List;

public class MainAcitvity extends AppCompatActivity implements CategoryListAdaptor.ItemSelection {
    RecyclerView mCategoryListView ;
    CategoryListAdaptor mCategoryListAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_acitvity);
        hideNavBars();
        mCategoryListView  = findViewById(R.id.cat_list);
        mCategoryListAdaptor = new CategoryListAdaptor(this,this);
        mCategoryListView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        mCategoryListView.setAdapter(mCategoryListAdaptor);
        mCategoryListView.addItemDecoration(new SpaceItemDecoration(20,20));
        final CategoryListModel viewModel =  ViewModelProviders.of(this).get(CategoryListModel.class);
        observeViewModel(viewModel);

        View easter_egg = findViewById(R.id.easter_egg);
        easter_egg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Featured.class);
                startActivity(intent);
            }
        });
    }

    protected void hideNavBars() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void observeViewModel(CategoryListModel viewModel) {
        viewModel.getVideoCatObservable().observe(this, new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(@Nullable List<CategoryModel> projects) {
                if (projects != null) {
                      mCategoryListAdaptor.setCategoryList(projects);
                }
            }
        });
    }

    @Override
    public void onItemSelection(CategoryModel categoryModel, int position) {
        Intent intent = new Intent(this, Listing.class);
        intent.putExtra("Category","video");
        intent.putExtra("CategoryModel", categoryModel);
        intent.putExtra("CategoryModelList", (Serializable) mCategoryListAdaptor.getListModel());
        startActivity(intent);
    }
}
