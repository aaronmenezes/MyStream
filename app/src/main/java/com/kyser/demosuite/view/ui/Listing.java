package com.kyser.demosuite.view.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.kyser.demosuite.R;
import com.kyser.demosuite.service.model.CategoryModel;
import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.view.ui.adaptor.MediaListAdaptor;
import com.kyser.demosuite.view.ui.components.SpaceItemDecoration;
import com.kyser.demosuite.viewmodel.CategoryListModel;
import com.kyser.demosuite.viewmodel.MediaListModel;

import java.util.ArrayList;
import java.util.List;

public class Listing extends AppCompatActivity implements MediaListAdaptor.ItemSelection, AdapterView.OnItemSelectedListener, Synopsis.OnFragmentInteractionListener {
    RecyclerView mMediaListView ;
    MediaListAdaptor mMediaListAdaptor;
    private List<CategoryModel> mCategoryList;
    private MediaListModel mListviewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavBars();
        setContentView(R.layout.activity_listing);
        findViewById(R.id.synopsis).setVisibility(View.GONE);
        setListAdaptor();
        initViewModel();
        final CategoryListModel mCategoryListModel =  ViewModelProviders.of(this).get(CategoryListModel.class);
        observeCategoryViewModel(mCategoryListModel);
        Synopsis.setListener(this);
    }

    private void observeCategoryViewModel(CategoryListModel viewModel) {
        viewModel.getVideoCatObservable().observe(this, new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(@Nullable List<CategoryModel> projects) {
                if (projects != null) {
                    mCategoryList = projects;
                    setSpinnerlist();
                }
            }
        });
    }

    private void setSpinnerlist() {
        List<String> categories = new ArrayList<String>();
        for( CategoryModel cat : mCategoryList){
            categories.add(cat.getTitle());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.category_spinner, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.cat_spinner);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setListAdaptor() {
        mMediaListView = findViewById(R.id.listing_grid);
        mMediaListView.setLayoutManager(new GridLayoutManager(this,3));
        mMediaListAdaptor = new MediaListAdaptor(this,this);
        mMediaListView.addItemDecoration(new SpaceItemDecoration(30,1));
        mMediaListView.setAdapter(mMediaListAdaptor);
    }


    private void initViewModel(){
        mListviewModel =  ViewModelProviders.of(this).get(MediaListModel.class);
        observeViewModel(mListviewModel);
    }

    protected void hideNavBars() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onItemSelection(ListingModel categoryModel, int position) {
        findViewById(R.id.synopsis).setVisibility(View.VISIBLE);
        Synopsis fragment =(Synopsis) getSupportFragmentManager().findFragmentById(R.id.synopsis);
        fragment.setSynopsisDetails(categoryModel,mListviewModel.getMediaListObservable().getValue());
    }

    private void observeViewModel(MediaListModel viewModel) {
        if(viewModel.getMediaListObservable()!=null)
            viewModel.getMediaListObservable().observe(this, projects -> {
                if (projects != null) {
                    mMediaListAdaptor.setCategoryList(projects);
                }
            });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");
        mListviewModel.setMediaCategory(mCategoryList.get(position).getCid());
        observeViewModel(mListviewModel);
        hideNavBars();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        hideNavBars();
    }

    @Override
    public void onBackPressed() {
        if(findViewById(R.id.synopsis).getVisibility()==View.VISIBLE)
            findViewById(R.id.synopsis).setVisibility(View.GONE);
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMoreSelected(ListingModel model) {
        Synopsis fragment =(Synopsis) getSupportFragmentManager().findFragmentById(R.id.synopsis);
        fragment.setSynopsisDetails(model,mListviewModel.getMediaListObservable().getValue());
    }
}
